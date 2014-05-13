package com.stewsters.zomgrl.entity.components.item

import com.stewsters.util.math.MatUtils
import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.graphic.MessageLog
import com.stewsters.zomgrl.graphic.RenderConfig
import squidpony.squidcolor.SColor
import squidpony.squidgrid.gui.swing.SwingPane

public class Inventory {

    List<Entity> items = []
    int capacity = 8
    Entity owner

    //this if for counter items.
    public int maxAmmo = 100
    Map<AmmoType, Integer> pouch = [:]

    public pickUp(Entity item) {
        if (items.size() >= capacity) {
            MessageLog.send("Inventory full, cannot pick up ${item.name}", SColor.RED, [owner])
        } else {
            items.add item
            item.levelMap.remove(item)
            MessageLog.send("${owner.name} picked up ${item.name}", SColor.GREEN, [owner])

            if (item.equipment) {
                Equipment oldEquipment = owner.inventory.getEquippedInSlot(item.equipment.slot)
                if (!oldEquipment)
                    item.equipment.equip(owner)
            }
        }
    }

    public boolean isFull() {
        return capacity <= items.size() - 1
    }

    public dump() {

        for (Entity item : owner.inventory.items) {

            int xPos = MatUtils.getIntInRange(-1, 1) + owner.x
            int yPos = MatUtils.getIntInRange(-1, 1) + owner.y
            if (!owner.levelMap.isBlocked(xPos, yPos)) {
                item.x = xPos
                item.y = yPos
            } else {
                item.x = owner.x
                item.y = owner.y
            }
            if (item.equipment?.isEquiped)
                item.equipment.dequip(owner)
            owner.levelMap.add(item)
        }
        owner.inventory.items.clear()
    }

    public static clear(SwingPane display) {
        (0..RenderConfig.inventoryWidth).each { int x ->
            (0..RenderConfig.inventoryHeight).each { int y ->
                display.clearCell(x + RenderConfig.inventoryX, y + RenderConfig.inventoryY)
            }
        }
    }

    public render(SwingPane display) {
        clear(display)

        items.eachWithIndex { Entity item, Integer i ->
            display.placeHorizontalString(RenderConfig.inventoryX, i + RenderConfig.inventoryY,
                    "${i + 1}:${item.equipment?.isEquiped ? "e" : ' '} ${item.name.substring(0, Math.min(RenderConfig.inventoryWidth - 2, item.name.length()))}")
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

    public Equipment getEquippedInSlot(Slot slot) {
        for (Entity item : items) {
            if (item.equipment && item.equipment.slot == slot && item.equipment.isEquiped) {
                return item.equipment
            }
        }
        return null
    }

    public List<Equipment> getAllEquiped() {
        return items.findAll { item -> item.equipment && item.equipment.isEquiped }.equipment
    }

    public int getAmmoCount(AmmoType ammoType) {
        return pouch[ammoType] ?: 0
    }

    public addAmmo(AmmoType ammoType, int quantity) {
        pouch[ammoType] = MatUtils.limit((pouch[ammoType] ?: 0) + quantity, 0, maxAmmo)
    }

    public boolean useAmmo(AmmoType ammoType) {
        if (pouch[ammoType]) {
            pouch[ammoType]--;
            return true
        } else return false;

    }

}
