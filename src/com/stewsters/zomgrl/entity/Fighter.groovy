package com.stewsters.zomgrl.entity

import com.stewsters.zomgrl.graphic.MessageLog


class Fighter {
    public Entity owner

    int max_hp
    int hp
    int defense
    int power
    def deathFunction

    public Fighter(int hp, int defense, int power, def deathFunction = null) {

        this.max_hp = hp
        this.hp = hp
        this.defense = defense
        this.power = power
        this.deathFunction = deathFunction
    }

    public takeDamage(damage) {
        if (damage > 0) {
            hp -= damage
            if (hp <= 0) {
                hp = 0
                if (deathFunction)
                    deathFunction(owner)
            }
        }
    }

    public def heal(int amount) {
        hp = Math.min(amount + hp, max_hp);
    }

    public attack(Entity target) {
        int damage = power - target.fighter.defense
        if (damage > 0) {
            MessageLog.send "${owner.name} attacks ${target.name} for ${damage} hit points."
            target.fighter.takeDamage(damage)
        } else {
            MessageLog.send "${owner.name} attacks ${target.name} but it has no effect!"
        }
    }

}
