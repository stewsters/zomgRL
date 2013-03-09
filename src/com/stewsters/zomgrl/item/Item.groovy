package com.stewsters.zomgrl.item

import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.graphic.MessageLog
import squidpony.squidcolor.SColor

class Item {
    public Entity owner
    Closure useFunction

    public Item(params) {
        useFunction = params?.useFunction
    }

    /**
     *
     * @return true if the item should be used up
     */
    public boolean useItem(Entity target) {
        if (useFunction) {
            return useFunction(target)
        } else {
            MessageLog.send("${owner.name} cannot be used.", SColor.RED)
            return false
        }
    }

}
