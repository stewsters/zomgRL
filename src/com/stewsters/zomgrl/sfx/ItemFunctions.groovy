package com.stewsters.zomgrl.sfx

import com.stewsters.util.MathUtils
import com.stewsters.zomgrl.ai.ConfusedZombie
import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.graphic.MessageLog
import squidpony.squidcolor.SColor

class ItemFunctions {

    public static final int HEAL_AMOUNT = 4

    public static Closure castHeal = { Entity target ->

        if (target.fighter.hp == target.fighter.max_hp) {
            MessageLog.send("You are already at full health.", SColor.RED)
            return false
        } else {
            MessageLog.send("Your wounds seal up.", SColor.LIGHT_VIOLET)
            target.fighter.heal(ItemFunctions.HEAL_AMOUNT)
            return true
        }
    }

    public static final int LIGHTNING_DAMAGE = 20
    public static final int LIGHTNING_RANGE = 5

    public static Closure castLightning = { Entity castor ->

        Entity enemy = castor.ai.findClosestEnemy(ItemFunctions.LIGHTNING_RANGE)
        if (!enemy) {
            MessageLog.send('No enemy is close enough to strike.', SColor.RED)
            return false
        } else {
            MessageLog.send("A lightning bolt strikes the ${enemy.name} with a loud thunder! The damage is ${ItemFunctions.LIGHTNING_DAMAGE} hit points.", SColor.LIGHT_BLUE)
            enemy.fighter.takeDamage(ItemFunctions.LIGHTNING_DAMAGE)
            return true
        }
    }

    public static final int DOMINATION_RANGE = 3
    public static Closure castDomination = { Entity castor ->
        Entity enemy = castor.ai.findClosestEnemy(ItemFunctions.DOMINATION_RANGE)
        if (!enemy) {
            MessageLog.send('No enemy is close enough to dominate.', SColor.RED)
            return false
        } else {
            MessageLog.send("Dark magic takes over ${enemy.name}.", SColor.LIGHT_BLUE)
            enemy.faction = castor.faction
            return true
        }

    }

    public static final int CONFUSE_RANGE = 10
    public static final int CONFUSE_NUM_TURNS = 10

    public static Closure castConfuse = { Entity castor ->
        Entity enemy = castor.ai.findClosestEnemy(ItemFunctions.CONFUSE_RANGE)
        if (!enemy) {
            MessageLog.send('No enemy is close enough to confused.', SColor.RED)
            return false
        } else {
            def oldID = enemy.ai
            enemy.ai = new ConfusedZombie(oldAI: oldID, numTurns: CONFUSE_NUM_TURNS)
            enemy.ai.owner = enemy
            MessageLog.send("${enemy.name} becomes confused.", SColor.LIGHT_BLUE)

            return true
        }

    }



    public static Closure antiviral = {Entity user->
        //removes infection from the system.

    }

    public static Closure bandage = {Entity user->
        //stops bleeding.

    }

    public static Closure heartExplosion = {Entity user ->
        //see further for a few rounds, increase armor+attack.
        MessageLog.send("${user.name} drinks an entire 24 pack of monster.", SColor.RED)
        // after a few rounds, your heart explodes
        return true

    }

    public static Closure eat = { Entity user ->
        //restores some stamina
    }



    //GUNS
    private static final int BERRETA_GUN_BONUS = 4
    private static final int BERRETA_MAX_RANGE = 8
    public static Closure gunBerreta = { Entity user ->
        //find closest target
        Entity enemy = user.ai.findClosestEnemy(ItemFunctions.BERRETA_MAX_RANGE)
        if (!enemy) {
            MessageLog.send("${user.name} doesn't see a target.", SColor.RED)
            return false
        } else {
            //enemy defense and range vs your marksman and gun bonus
            int range = user.distanceTo(enemy)
            int damage = MathUtils.getIntInRange(0,user.fighter.marksman + BERRETA_GUN_BONUS) - MathUtils.getIntInRange(0,enemy.fighter.defense + range)

            String message = "You fire a round at ${enemy.name}."

            if (damage>0){
                message+=" The damage is ${damage} hit points."
            }else{
                message+=" You missed."
            }

            MessageLog.send(message, SColor.LIGHT_BLUE)

            enemy.fighter.takeDamage(damage)
            return true
        }
    }

    private static final int AR15_GUN_BONUS = 6
    private static final int AR15_MAX_RANGE = 12
    public static Closure gunAR15 = {Entity user->
        //find closest target
        Entity enemy = user.ai.findClosestEnemy(ItemFunctions.AR15_MAX_RANGE)
        if (!enemy) {
            MessageLog.send("${user.name} doesn't see a target.", SColor.RED)
            return false
        } else {
            //enemy defense and range vs your marksman and gun bonus
            int range = user.distanceTo(enemy)
            int damage = MathUtils.getIntInRange(0,user.fighter.marksman + AR15_GUN_BONUS) - MathUtils.getIntInRange(0,enemy.fighter.defense + range)

            String message = "You fire a round at ${enemy.name}."

            if (damage>0){
                message+=" The damage is ${damage} hit points."
            }else{
                message+=" You missed."
            }

            MessageLog.send(message, SColor.LIGHT_BLUE)

            enemy.fighter.takeDamage(damage)
            return true
        }
    }




    private static final int PUMP_GUN_BONUS = 10
    private static final int PUMP_MAX_RANGE = 4
    public static Closure gunPumpShotGun = {Entity user->
        //find closest target
        Entity enemy = user.ai.findClosestEnemy(ItemFunctions.PUMP_MAX_RANGE)
        if (!enemy) {
            MessageLog.send("${user.name} doesn't see a target.", SColor.RED)
            return false
        } else {
            //enemy defense and range vs your marksman and gun bonus
            int range = user.distanceTo(enemy)
            int damage = MathUtils.getIntInRange(0,user.fighter.marksman + PUMP_GUN_BONUS) - MathUtils.getIntInRange(0,enemy.fighter.defense + range)

            String message = "You fire a round at ${enemy.name}."

            if (damage>0){
                message+=" The damage is ${damage} hit points."
            }else{
                message+=" You missed."
            }

            MessageLog.send(message, SColor.LIGHT_BLUE)

            enemy.fighter.takeDamage(damage)
            return true
        }
    }
}
