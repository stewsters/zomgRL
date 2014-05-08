package com.stewsters.zomgrl.ai

import com.stewsters.util.MathUtils
import com.stewsters.zomgrl.entity.Entity
import squidpony.squidgrid.util.Direction

class BasicZombie extends BaseAi implements Ai {


    private boolean active = true;

    public void takeTurn() {
        if (!owner)
            return //you died this turn, sorry bro
        if (!active) {
            if (owner.levelMap.ground[owner.x][owner.y].isExplored)
                active = true
        } else {
            //nearest opponent
            Entity enemy = findClosestVisibleEnemy()

            if (enemy) {
                owner.moveTowardsAndAttack(enemy.x, enemy.y)
            } else {

                if (MathUtils.boolean) {
                    owner.move(MathUtils.getIntInRange(-1, 1), MathUtils.getIntInRange(-1, 1))
                } else {
                    Direction d = owner.levelMap.noiseMap.getNoiseDir(owner.x, owner.y)
                    owner.move(d.deltaX, d.deltaY)
                }
//


            }
        }
    }


}
