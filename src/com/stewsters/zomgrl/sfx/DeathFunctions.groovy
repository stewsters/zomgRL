package com.stewsters.zomgrl.sfx

import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.game.Game
import com.stewsters.zomgrl.game.GameState
import com.stewsters.zomgrl.graphic.MessageLog
import squidpony.squidcolor.SColor

class DeathFunctions {


    public static Closure playerDeath = { Entity player ->

        MessageLog.send("You are dead.")
        Game.state = GameState.dead
        player.ch = '%'
        player.color = SColor.BLOOD_RED
        player.priority = 80
        if (player.inventory)
            player.inventory.dump()

    }


    public static Closure monsterDeath = { Entity monster ->
        MessageLog.send("${monster.name} is dead!")
        monster.ch = '%'
        monster.color = SColor.BLOOD_RED
        monster.blocks = false
        monster.fighter = null
        monster.ai = null
        monster.name = "Remains of ${monster.name}"
        monster.priority = 80

        if (monster.inventory)
            monster.inventory.dump()
    }

}
