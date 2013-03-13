package com.stewsters.zomgrl.ai

import com.stewsters.util.MathUtils
import com.stewsters.zomgrl.entity.Entity


class BasicCivilian extends BaseAi implements Ai {

    public void takeTurn() {

        //nearest opponent
        Entity enemy = findClosestVisibleEnemy()


        if (enemy) {
            //if we have a gun, and they are getting too close, shoot them

            if (owner.distanceTo(enemy) < 2) {
                owner.moveTowardsAndAttack(enemy.x, enemy.y)
            } else {
                owner.moveAway(enemy.x, enemy.y)
            }
        } else if (owner.inventory) {

            //if we are standing on an item and we have room, pick it up
            if (owner.inventory.isFull()) {
                owner.randomMovement()
            } else {
                //find nearest visible item
                Entity item = owner.ai.findClosestVisibleItem()

                //if we are standing on it, pickUp
                if (item) {
                    if (item.x == owner.x) {
                        owner.inventory.pickUp(item)
                    } else {
                        owner.moveTowardsAndAttack(item.x, item.y)
                    }
                } else {
                    owner.randomMovement()
                }

            }
        } else if (MathUtils.boolean) {
            owner.randomMovement();
        }

    }
}
