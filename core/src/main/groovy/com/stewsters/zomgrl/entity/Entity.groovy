package com.stewsters.zomgrl.entity

import com.stewsters.util.math.MatUtils
import com.stewsters.zomgrl.entity.components.Fighter
import com.stewsters.zomgrl.entity.components.ai.Ai
import com.stewsters.zomgrl.entity.components.ai.Faction
import com.stewsters.zomgrl.entity.components.item.Equipment
import com.stewsters.zomgrl.entity.components.item.Inventory
import com.stewsters.zomgrl.entity.components.item.Item
import com.stewsters.zomgrl.graphic.MessageLog
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
    public SColor color

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

        if (params.equipment) {
            equipment = params.equipment
            equipment.owner = this
            if (!itemComponent) {
                itemComponent = new Item([:])
                itemComponent.owner = this
            }
        }

        //This is last, because it needs to add the locations to the index
        levelMap = params.map
        if (levelMap)
            levelMap.add(this)
    }

    public void move(int xDif, int yDif) {
        int newX = xDif + x
        int newY = yDif + y

        if (!(levelMap.isBlocked(newX, newY))) {
            x = newX
            y = newY
            levelMap.update(this);
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
            levelMap.getEntitiesAtLocation(newX, newY).each { Entity entity ->
                if (entity.fighter && faction?.hates(entity?.faction)) {
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
        float distance = Math.sqrt(dx**2 + dy**2)

        dx = (int) Math.round(dx / distance)
        dy = (int) Math.round(dy / distance)
        moveOrAttack(dx, dy)
    }

    void moveAway(int targetX, int targetY) {
        int dx = targetX - x
        int dy = targetY - y

        dx = MatUtils.limit(dx, -1, 1)
        dy = MatUtils.limit(dy, -1, 1)
        move(-dx, -dy)
    }


    public int distanceTo(Entity other) {
        int dx = other.x - this.x
        int dy = other.y - this.y
        return (int) Math.round(Math.sqrt(dx * dx + dy * dy))
    }

    public void grab() {
        if (!inventory) {
            MessageLog.send("${name} can't hold items.", SColor.WHITE, [this])
            return
        }
        //if holding item, message you cant
        Entity topItem = levelMap.getEntitiesAtLocation(x, y).sort { it.priority }.find { it.itemComponent }

        if (topItem) {
            inventory.pickUp(topItem)
        }
    }

    public void drop() {
        //take held item and put it on the ground where you stand

        if (inventory.items.size()) {
            Entity item = inventory.items.pop()
            item.x = x
            item.y = y
            if (item.equipment?.isEquiped)
                item.equipment.dequip(this)
            levelMap.add(item)
        } else {
            MessageLog.send("${name} has nothing to drop.", SColor.WHITE, [this])
        }

    }

    public void randomMovement() {
        move(MatUtils.getIntInRange(-1, 1), MatUtils.getIntInRange(-1, 1))
    }
}
