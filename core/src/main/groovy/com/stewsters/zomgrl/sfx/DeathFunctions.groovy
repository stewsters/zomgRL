package com.stewsters.zomgrl.sfx

import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.entity.components.ai.BasicZombie
import com.stewsters.zomgrl.entity.components.ai.Faction
import com.stewsters.zomgrl.game.Game
import com.stewsters.zomgrl.game.GameState
import com.stewsters.zomgrl.graphic.MessageLog
import squidpony.squidcolor.SColor

class DeathFunctions {


    public static Closure playerDeath = { Entity owner ->

        MessageLog.send("${owner.name} is dead.", SColor.RED, [owner])
        Game.state = GameState.dead
        owner.ch = '%'
        owner.color = SColor.BLOOD_RED
        owner.priority = 80
        owner.faction = null
        if (owner.inventory) {
            owner.inventory.dump()
        }
    }


    public static Closure zombieDeath = { Entity owner ->
        MessageLog.send("${owner.name} is dead!", SColor.RED, [owner])
        owner.ch = '%'
        owner.color = SColor.BLOOD_RED
        owner.blocks = false
        owner.fighter = null
        owner.ai = null
        owner.name = "Remains of ${owner.name}"
        owner.priority = 80
        owner.faction = null
        if (owner.inventory)
            owner.inventory.dump()
    }


    public static Closure zombify = { Entity owner ->
        MessageLog.send("${owner.name} is changing!", SColor.RED, [owner])
        owner.faction = Faction.zombie
        owner.ch = 'z'
        owner.color = SColor.GREEN_BAMBOO
        owner.fighter.deathFunction = zombieDeath
        owner.fighter.baseMarksman = 0;
        owner.ai = new BasicZombie()
        owner.ai.owner = owner
        owner.name = "Zombie of ${owner.name}"
        owner.priority = 120

        if (owner.inventory) {
            owner.inventory.dump()
            owner.inventory = null
        }
    }
}
