package com.stewsters.zomgrl.ai

import com.stewsters.zomgrl.entity.Entity

class BasicZombie extends BaseAi implements Ai {


    private boolean active = true;

    public void takeTurn() {

        if (!active) {
            if (owner.levelMap.ground[owner.x][owner.y].isExplored)
                active = true
        } else {
            //nearest opponent
            Entity enemy = findClosestVisibleEnemy()

            if (enemy) {
                owner.moveTowardsAndAttack(enemy.x, enemy.y)
            }
        }
    }


}
