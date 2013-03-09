package com.stewsters.zomgrl.item

import com.stewsters.grl.Entity
import com.stewsters.grl.graphic.MessageLog
import com.stewsters.grl.graphic.RenderConfig
import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.graphic.MessageLog
import com.stewsters.zomgrl.graphic.RenderConfig
import squidpony.squidcolor.SColor
import squidpony.squidgrid.gui.swing.SwingPane

public class Inventory {

    List<Entity> items = []
    def capacity = 8
    Entity owner

    public pickUp(Entity item) {
        if (items.size() >= capacity) {
            MessageLog.send("Inventory full, cannot pick up ${item.name}", SColor.RED)
        } else {
            items.add item
            item.levelMap.objects.remove(item)
            MessageLog.send("You picked up ${item.name}", SColor.GREEN)
        }
    }

    public dump() {

        for (Entity item : owner.inventory.items) {
            item.x = owner.x
            item.y = owner.y
            owner.levelMap.objects.add(item)
        }
        owner.inventory.items.clear()
    }

    public render(SwingPane display) {

        (0..RenderConfig.inventoryWidth).each { int x ->
            (0..RenderConfig.inventoryHeight).each { int y ->
                display.clearCell(x + RenderConfig.inventoryX, y + RenderConfig.inventoryY)
            }
        }
        items.eachWithIndex { Entity item, int i ->
            display.placeHorizontalString(RenderConfig.inventoryX, i + RenderConfig.inventoryY,
                    "${i + 1}:${item.name.substring(0, Math.min(RenderConfig.inventoryWidth - 2, item.name.length()))}")
        }
    }

    public boolean useById(int id) {
        if (items.size() > id) {
            Entity item = items.get(id)
            if (item) {
                if (item.itemComponent.useItem(owner)) {
                    items.remove(item)
                    return true
                }
            }
        }
        return false
    }

}
