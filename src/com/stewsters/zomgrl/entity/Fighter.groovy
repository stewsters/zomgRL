package com.stewsters.zomgrl.entity

import com.stewsters.util.MathUtils
import com.stewsters.zomgrl.ai.Faction
import com.stewsters.zomgrl.graphic.MessageLog
import com.stewsters.zomgrl.sfx.DeathFunctions


class Fighter {
    public Entity owner

    int max_hp
    int hp      //can take more hits

    int max_infection //maximum infection you can withstand
    int infection

    int max_stamina
    int stamina // used to make attacks and sprint

    int defense //make it harder to hit
    int power   //strength with melee weapons
    int marksman // how good with ranged weapons


    def deathFunction

    public Fighter(params) {

        max_hp = params.hp ?: 1
        hp = params.hp ?: max_hp
        max_infection = params.max_infection ?: 1
        infection = params.infection ?: 0
        defense = params.defense ?: 1
        power = params.power ?: 1
        marksman = params.marksman ?: 1

        max_stamina = params.max_stamina ?: 1
        stamina = max_stamina

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
                int range = damage/2
                int xPos = MathUtils.getIntInRange(-range, range) + owner.x
                int yPos = MathUtils.getIntInRange(-range, range) + owner.y
                owner.levelMap.ground[MathUtils.limit(xPos,0,owner.levelMap.xSize-1)][MathUtils.limit(yPos,0,owner.levelMap.ySize-1)].gore = true
            }

        }
    }

    public def heal(int amount) {
        hp = Math.min(amount + hp, max_hp);
    }

    public def raiseStamina(int amount) {
        stamina = Math.min(amount + stamina, max_stamina)
    }

    public def infect(int amount) {
        infection = Math.min(amount + infection, max_infection)
        if (infection == max_infection) {
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

}
