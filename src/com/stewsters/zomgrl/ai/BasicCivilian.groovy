package com.stewsters.zomgrl.ai

import com.stewsters.util.MathUtils
import com.stewsters.zomgrl.entity.Entity


class BasicCivilian extends BaseAi implements Ai {

    public void takeTurn() {

        //nearest opponent
        Entity enemy = findClosestVisibleEnemy()


        if (enemy) {
            if (owner.distanceTo(enemy) < 2) {
                owner.moveTowardsAndAttack(enemy.x, enemy.y)
            } else {
                owner.moveAway(enemy.x, enemy.y)
            }
        }
        else{
            if (MathUtils.boolean)
                owner.move(MathUtils.getIntInRange(-1, 1), MathUtils.getIntInRange(-1, 1))
        }

    }
}
