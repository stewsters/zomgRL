package com.stewsters.zomgrl.ai

import com.stewsters.zomgrl.entity.Entity

class BasicMonster extends BaseAi implements Ai {


    private boolean active = false;

    public void takeTurn() {

        if (!active) {
            if (owner.levelMap.ground[owner.x][owner.y].isExplored)
                active = true
        } else {
            //nearest opponent
            Entity enemy = findClosestEnemy()

            if (enemy) {
                owner.moveTowardsAndAttack(enemy.x, enemy.y)
            }
        }
    }


}
