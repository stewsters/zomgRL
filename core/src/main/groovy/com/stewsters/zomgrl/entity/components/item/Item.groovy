package com.stewsters.zomgrl.entity.components.item

import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.graphic.MessageLog
import squidpony.squidcolor.SColor

class Item {
    public Entity owner
    Closure useFunction

    int minRange
    int maxRange

    public Item(params) {
        useFunction = params?.useFunction

        minRange = params?.minRange ?: 1
        maxRange = params?.minRange ?: 1
    }

    /**
     *
     * @return true if the item should be used up
     */
    public boolean useItem(Entity user) {

        if (owner.equipment) {
            owner.equipment.toggleEquip(user)
            return false
        } else if (useFunction) {
            return useFunction(user)
        } else {
            MessageLog.send("${owner.name} cannot be used.", SColor.RED, [user])
            return false
        }
    }

    public boolean useHeldItem(Entity user) {
        if (useFunction) {
            return useFunction(user)
        } else {
            MessageLog.send("${owner.name} cannot be used.", SColor.RED, [user])
            return false
        }
    }

}
