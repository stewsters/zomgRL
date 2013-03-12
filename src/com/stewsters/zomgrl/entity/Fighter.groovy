package com.stewsters.zomgrl.entity

import com.stewsters.util.MathUtils
import com.stewsters.zomgrl.ai.Faction
import com.stewsters.zomgrl.graphic.MessageLog
import com.stewsters.zomgrl.sfx.DeathFunctions


class Fighter {
    public Entity owner

    int baseMaxHp
    int hp      //can take more hits

    int baseMaxInfection //maximum infection you can withstand
    int infection

    int baseMaxStamina
    int stamina // used to make attacks and sprint

    int baseDefense //make it harder to hit
    int basePower   //strength with melee weapons
    int baseMarksman // how good with ranged weapons


    Closure deathFunction

    public Fighter(params) {

        baseMaxHp = params.hp ?: 1
        hp = params.hp ?: baseMaxHp

        baseMaxInfection = params.maxInfection ?: 1
        infection = params.infection ?: 0

        baseDefense = params.defense ?: 1
        basePower = params.power ?: 1
        baseMarksman = params.marksman ?: 1

        baseMaxStamina = params.stamina ?: 1
        stamina = baseMaxStamina

        deathFunction = params.deathFunction ?: null
    }

    public takeDamage(damage) {
        if (damage > 0) {
            hp -= damage
            if (hp <= 0) {
                hp = 0
                if (deathFunction)
                    deathFunction(owner)
            }
            for (int i = 0; i < damage; i++) {
                int range = Math.min((int)(damage / 2),5)
                int xPos = MathUtils.getIntInRange(-range, range) + owner.x
                int yPos = MathUtils.getIntInRange(-range, range) + owner.y
                owner.levelMap.ground[MathUtils.limit(xPos,0,owner.levelMap.xSize-1)][MathUtils.limit(yPos,0,owner.levelMap.ySize-1)].gore = true
            }

        }
    }

    public def heal(int amount) {
        hp = Math.min(amount + hp, maxHP);
    }

    public def raiseStamina(int amount) {
        stamina = Math.min(amount + stamina, maxStamina)
    }

    public def infect(int amount) {
        infection = Math.min(amount + infection, maxInfection)
        if (infection == maxInfection) {
            DeathFunctions.zombify(owner)
        }
    }

    public attack(Entity target) {
        int damage = MathUtils.getIntInRange(0, power) - MathUtils.getIntInRange(0, target.fighter.defense)

        if (damage > 0) {
            MessageLog.send "${owner.name} attacks ${target.name} for ${damage} hit points."
            if (owner.faction == Faction.zombie) {
                target.fighter.infect(1)
            }
            target.fighter.takeDamage(damage)

        } else if (damage < 0) {
            owner.fighter.takeDamage(Math.abs(damage))
            MessageLog.send "${owner.name} attacks ${target.name}, but is countered, receiving ${Math.abs(damage)} damage."
        } else {
            MessageLog.send "${owner.name} attacks ${target.name} but it has no effect!"
        }
    }

    /* Equipment functions */

    public int getMaxHP(){
        int bonus =  0
        if (owner.inventory){
            bonus +=  owner.inventory.getAllEquiped()?.bonusMaxHp.sum()?:0
        }
        return baseMaxHp + bonus
    }

    public int getMaxInfection(){
        int bonus =  0
        if (owner.inventory){
            bonus +=  owner.inventory.getAllEquiped().bonusMaxInfection.sum()?:0
        }
        return baseMaxInfection + bonus
    }

    public int getMaxStamina(){
        int bonus =  0
        if (owner.inventory){
            bonus +=  owner.inventory.getAllEquiped().bonusMaxStamina.sum()?:0
        }
        return baseMaxStamina + bonus
    }


    public int getPower(){
        int bonus =  0
        if (owner.inventory){
            bonus +=  owner.inventory.getAllEquiped().bonusPower.sum()?:0
        }
        return basePower + bonus
    }

    public int getDefense(){
        int bonus =  0
        if (owner.inventory){
            bonus +=  owner.inventory.getAllEquiped().bonusDefense.sum()?:0
        }
        return baseDefense + bonus
    }

    public int getMarksman(){
        int bonus =  0
        if (owner.inventory){
            bonus +=  owner.inventory.getAllEquiped().bonusMarksman.sum()?:0
        }
        return baseMarksman + bonus
    }

}
