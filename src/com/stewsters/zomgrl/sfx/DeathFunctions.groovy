package com.stewsters.zomgrl.sfx

import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.game.Game
import com.stewsters.zomgrl.game.GameState
import com.stewsters.zomgrl.graphic.MessageLog
import squidpony.squidcolor.SColor

class DeathFunctions {


    public static Closure playerDeath = { Entity owner ->

        MessageLog.send("You are dead.")
        Game.state = GameState.dead
        owner.ch = '%'
        owner.color = SColor.BLOOD_RED
        owner.priority = 80
        if (owner.inventory)
            owner.inventory.dump()

    }


    public static Closure zombieDeath = { Entity owner ->
        MessageLog.send("${owner.name} is dead!")
        owner.ch = '%'
        owner.color = SColor.BLOOD_RED
        owner.blocks = false
        owner.fighter = null
        owner.ai = null
        owner.name = "Remains of ${owner.name}"
        owner.priority = 80

        if (owner.inventory)
            owner.inventory.dump()
    }

}
