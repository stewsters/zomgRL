package com.stewsters.zomgrl.sfx

import com.stewsters.util.math.MatUtils
import com.stewsters.zomgrl.ai.Ai
import com.stewsters.zomgrl.ai.ConfusedZombie
import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.graphic.MessageLog
import com.stewsters.zomgrl.item.AmmoType
import squidpony.squidcolor.SColor

class ItemFunctions {

    public static final int HEAL_AMOUNT = 4

    public static Closure castHeal = { Entity user ->

        if (user.fighter.hp == user.fighter.maxHP) {
            MessageLog.send("You are already at full health.", SColor.RED, [user])
            return false
        } else {
            MessageLog.send("Your wounds seal up.", SColor.LIGHT_VIOLET)
            user.fighter.heal(ItemFunctions.HEAL_AMOUNT)
            return true
        }
    }

    public static final int LIGHTNING_DAMAGE = 20
    public static final int LIGHTNING_RANGE = 5

    public static Closure castLightning = { Entity user ->

        Entity enemy = user.ai.findClosestVisibleEnemy(maxRange: ItemFunctions.LIGHTNING_RANGE)
        if (!enemy) {
            MessageLog.send('No enemy is close enough to strike.', SColor.RED, [user])
            return false
        } else {
            MessageLog.send("A lightning bolt strikes the ${enemy.name} with a loud thunder! The damage is ${ItemFunctions.LIGHTNING_DAMAGE} hit points.", SColor.LIGHT_BLUE, [user, enemy])
            enemy.fighter.takeDamage(ItemFunctions.LIGHTNING_DAMAGE)
            return true
        }
    }

    public static final int DOMINATION_RANGE = 3
    public static Closure castDomination = { Entity user ->
        Entity enemy = user.ai.findClosestVisibleEnemy(maxRange: ItemFunctions.DOMINATION_RANGE)
        if (!enemy) {
            MessageLog.send('No enemy is close enough to dominate.', SColor.RED)
            return false
        } else {
            MessageLog.send("Dark magic takes over ${enemy.name}.", SColor.LIGHT_BLUE, [user, enemy])
            enemy.faction = user.faction
            return true
        }

    }

    public static final int CONFUSE_RANGE = 10
    public static final int CONFUSE_NUM_TURNS = 10

    public static Closure castConfuse = { Entity user ->
        Entity enemy = user.ai.findClosestVisibleEnemy(maxRange: ItemFunctions.CONFUSE_RANGE)
        if (!enemy) {
            MessageLog.send('No enemy is close enough to confused.', SColor.RED, [user, enemy])
            return false
        } else {
            Ai oldID = enemy.ai
            enemy.ai = new ConfusedZombie(oldAI: oldID, castor: user, numTurns: CONFUSE_NUM_TURNS)
            enemy.ai.owner = enemy
            MessageLog.send("${enemy.name} becomes confused.", SColor.LIGHT_BLUE)

            return true
        }

    }


    public static Closure antiviral = { Entity user ->
        //removes infection from the system.
        user.fighter.infection = 0
        MessageLog.send("${user.name} self medicates.", [user])
    }

    public static final int BANDAGE_HEAL_AMOUNT = 6
    public static Closure bandage = { Entity user ->
        if (user.fighter.hp == user.fighter.maxHP) {
            MessageLog.send("You aren't bleeding.", SColor.RED, [user])
            return false
        } else {
            MessageLog.send("${user.name}'s wounds seal up.", SColor.LIGHT_VIOLET, [user])
            user.fighter.heal(ItemFunctions.BANDAGE_HEAL_AMOUNT)
            return true
        }
    }

    public static Closure heartExplosion = { Entity user ->
        //see further for a few rounds, increase armor+attack.
        MessageLog.send("${user.name} drinks an entire 24 pack of monster.", SColor.RED, [user])
        // after a few rounds, your heart explodes
        return true

    }

    public static final int EAT_STAMINA_BOOST = 4
    public static Closure eat = { Entity user ->
        if (user.fighter.stamina == user.fighter.maxStamina) {
            MessageLog.send("${user.name} isn't hungry.", SColor.RED, [user])
            return false
        } else {
            MessageLog.send("${user.name} feasts on beef jerkey.", SColor.LIGHT_VIOLET, [user])
            user.fighter.raiseStamina(ItemFunctions.BANDAGE_HEAL_AMOUNT)
            return true
        }
    }

    //GUNS
    private static final int BERRETA_GUN_BONUS = 4
    private static final int BERRETA_MAX_RANGE = 10
    public static Closure gunBerreta = { Entity user ->
        //find closest target
        Entity enemy = user.ai.findClosestVisibleEnemy(maxRange: ItemFunctions.BERRETA_MAX_RANGE)
        if (!enemy) {
            MessageLog.send("${user.name} doesn't see a target.", SColor.RED, [user])
            return false
        } else {
            //enemy defense and range vs your baseMarksman and gun bonus
            int range = user.distanceTo(enemy)
            int damage = MatUtils.getIntInRange(0, user.fighter.marksman + ItemFunctions.BERRETA_GUN_BONUS) - MatUtils.getIntInRange(0, enemy.fighter.defense + range)

            String message
            if (user.inventory && user.inventory.useAmmo(AmmoType.pistol)) {
                message = "${user.name} fires a round at ${enemy.name}."
                user.levelMap.makeNoise(user.x, user.y, 1000)

                if (damage > 0) {
                    message += " and the damage is ${damage} hit points."
                    enemy.fighter.takeDamage(damage)
                } else {
                    message += " and missed."
                }
            } else {
                message = "${user.name} doesnt have ammo."
            }
            MessageLog.send(message, SColor.LIGHT_BLUE, [user, enemy])

            return true
        }
    }

    private static final int AR15_GUN_BONUS = 6
    private static final int AR15_MAX_RANGE = 15
    public static Closure gunAR15 = { Entity user ->
        //find closest target
        Entity enemy = user.ai.findClosestVisibleEnemy(maxRange: ItemFunctions.AR15_MAX_RANGE)
        if (!enemy) {
            MessageLog.send("${user.name} doesn't see a target.", SColor.RED, [user])
            return false
        } else {
            //enemy defense and range vs your baseMarksman and gun bonus
            int range = user.distanceTo(enemy)
            int damage = MatUtils.getIntInRange(0, user.fighter.marksman + AR15_GUN_BONUS) - MatUtils.getIntInRange(0, enemy.fighter.defense + range)

            String message
            if (user.inventory && user.inventory.useAmmo(AmmoType.rifle)) {
                message = "${user.name} fires a round at ${enemy.name}"
                user.levelMap.makeNoise(user.x, user.y, 1000)

                if (damage > 0) {
                    message += " and the damage is ${damage} hit points."
                    enemy.fighter.takeDamage(damage)
                } else {
                    message += " and missed."
                }
            } else {
                message = "${user.name} doesnt have ammo."
            }

            MessageLog.send(message, SColor.LIGHT_BLUE, [user, enemy])

            return true
        }
    }


    private static final int PUMP_GUN_BONUS = 10
    private static final int PUMP_MAX_RANGE = 4
    public static Closure gunPumpShotGun = { Entity user ->
        //find closest target
        Entity enemy = user.ai.findClosestVisibleEnemy(maxRange: ItemFunctions.PUMP_MAX_RANGE)
        if (!enemy) {
            MessageLog.send("${user.name} doesn't see a target.", SColor.RED, [user])
            return false
        } else {
            //enemy defense and range vs your baseMarksman and gun bonus
            int range = user.distanceTo(enemy)
            int damage = MatUtils.getIntInRange(0, user.fighter.marksman + ItemFunctions.PUMP_GUN_BONUS) - MatUtils.getIntInRange(0, enemy.fighter.defense + range)

            String message
            if (user.inventory && user.inventory.useAmmo(AmmoType.shotgun)) {
                message = "${user.name} fires a round at ${enemy.name}"
                user.levelMap.makeNoise(user.x, user.y, 1000)

                if (damage > 0) {
                    message += " and the damage is ${damage} hit points."
                    enemy.fighter.takeDamage(damage)
                } else {
                    message += " and missed."
                }
            } else {
                message = "${user.name} doesnt have ammo."
            }
            MessageLog.send(message, SColor.LIGHT_BLUE, [user, enemy])

            return true
        }
    }

    public static Closure rifleAmmoBox = { Entity user ->
        int quantity = MatUtils.getIntInRange(12, 20)
        if (user.inventory) {
            user.inventory.addAmmo(AmmoType.rifle, quantity)
            MessageLog.send("${user.name} picked up ${quantity} rounds.")
            return true
        } else {
            MessageLog.send("${user.name} has no use for bullets.", SColor.RED, [user])
            return false
        }
    }
    public static Closure pistolAmmoBox = { Entity user ->
        int quantity = MatUtils.getIntInRange(12, 20)
        if (user.inventory) {
            user.inventory.addAmmo(AmmoType.pistol, quantity)
            MessageLog.send("${user.name} picked up ${quantity} rounds.")
            return true
        } else {
            MessageLog.send("${user.name} has no use for bullets.", SColor.RED, [user])
            return false
        }
    }
    public static Closure shotgunAmmoBox = { Entity user ->
        int quantity = MatUtils.getIntInRange(8, 18)
        if (user.inventory) {
            user.inventory.addAmmo(AmmoType.shotgun, quantity)
            MessageLog.send("${user.name} picked up ${quantity} shells.")
            return true
        } else {
            MessageLog.send("${user.name} has no use for shells.", SColor.RED, [user])
            return false
        }
    }
}
