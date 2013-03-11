package com.stewsters.zomgrl.entity

import com.stewsters.util.MathUtils
import com.stewsters.zomgrl.ai.Ai
import com.stewsters.zomgrl.ai.Faction
import com.stewsters.zomgrl.graphic.MessageLog
import com.stewsters.zomgrl.item.Equipment
import com.stewsters.zomgrl.item.Inventory
import com.stewsters.zomgrl.item.Item
import com.stewsters.zomgrl.map.LevelMap
import squidpony.squidcolor.SColor

/**
 * this is a generic object: the player, a zombie, an item, the stairs...
 * it's always represented by a character on screen.
 */
public class Entity {

    public LevelMap levelMap

    public int priority = 100

    public String name
    public boolean blocks
    public int x
    public int y
    public char ch
    public def color

    public Fighter fighter
    public Ai ai
    public Faction faction
    public Item itemComponent
    public Equipment equipment
    public Inventory inventory

    /**
     * LevelMap map, int x, int y, char ch, String name, def color,
     blocks = false, Fighter fighter = null, Ai ai = null, Faction faction = null,
     Item itemComponent
     * @param params
     */

    public Entity(params) {

        levelMap = params.map
        if (levelMap)
            levelMap.objects.add(this)

        x = params.x ?: 0
        y = params.y ?: 0

        ch = (params.ch ?: '@') as char
        name = params.name ?: 'Unnamed'
        blocks = params.blocks ?: false

        color = params.color ?: SColor.WHITE
        faction = params.faction

        priority = params.priority ?: 100

        if (params.fighter) {
            fighter = params.fighter
            fighter.owner = this
        }

        if (params.ai) {
            ai = params.ai
            ai.owner = this
        }



        if (params.inventory) {
            inventory = params.inventory
            inventory.owner = this
        }

        if (params.itemComponent) {
            itemComponent = params.itemComponent
            itemComponent.owner = this
        }

        if (params.equipment){
            equipment = params.equipment
            equipment.owner = this
            if (!itemComponent){
                itemComponent = new Item()
                itemComponent.owner = this
            }
        }
    }

    public void move(int xDif, int yDif) {
        int newX = xDif + x
        int newY = yDif + y

        if (!(levelMap.isBlocked(newX, newY))) {
            x = newX
            y = newY
        }
    }

    public void moveOrAttack(int dx, int dy) {
        int newX = dx + x
        int newY = dy + y

        if (x < 0 || x >= levelMap.xSize || y < 0 || y >= levelMap.ySize) {
            return;
        }
        if (fighter) {
            Entity target = null
            levelMap.objects.each { Entity entity ->
                if (entity.fighter && faction?.hates(entity?.faction) && entity.x == newX && entity.y == newY) {
                    target = entity
                }
            }

            if (target) {
                fighter.attack(target)
            } else {
                move(dx, dy)
            }
        } else {
            move(dx, dy)
        }
    }


    void moveTowardsAndAttack(int targetX, int targetY) {
        int dx = targetX - x
        int dy = targetY - y
        float distance = Math.sqrt(dx ** 2 + dy ** 2)

        dx = (int) Math.round(dx / distance)
        dy = (int) Math.round(dy / distance)
        moveOrAttack(dx, dy)
    }

    void moveAway(int targetX, int targetY) {
        int dx = targetX - x
        int dy = targetY - y

        dx = Math.max(Math.min(dx, 1), -1)
        dy = Math.max(Math.min(dy, 1), -1)
        move(-dx, -dy)
    }


    public int distanceTo(Entity other) {
        int dx = other.x - this.x
        int dy = other.y - this.y
        return (int) Math.round(Math.sqrt(dx * dx + dy * dy))
    }

    public void grab() {
        if (!inventory) {
            MessageLog.send("${name} can't hold items.")
            return
        }

        //if holding item, message you cant
        levelMap.objects.findAll { entity ->
            entity.itemComponent && entity.x == x && entity.y == y
        }.each { entity ->
            inventory.pickUp(entity)
        }
    }

    public void drop() {
        //take held item and put it on the ground where you stand

        if (inventory.items.size()) {
            Entity item = inventory.items.pop()
            item.x = x
            item.y = y
            if (item.equipment?.isEquiped)
                item.equipment.dequip()
            levelMap.objects.add(item)
        } else {
            MessageLog.send("${name} has nothing to drop.")
        }

    }

    public void randomMovement() {
        move(MathUtils.getIntInRange(-1, 1), MathUtils.getIntInRange(-1, 1))
    }
}
