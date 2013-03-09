package com.stewsters.zomgrl.ai

import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.graphic.RenderConfig


/**
 * These are functions common to all AIs
 */
abstract class BaseAi {

    Entity owner
    float[][] light
    int sightRange = 20

    public Entity findClosestEnemy(int maxRange = Integer.MAX_VALUE) {
        int distance = maxRange
        Entity enemy = null

        for (Entity other : owner.levelMap.objects) {
            if (other.fighter && other.fighter.hp > 0 && other.faction && owner.faction?.hates(other?.faction)) {
                int thisDistance = owner.distanceTo(other)
                if (distance > thisDistance) {
                    distance = thisDistance
                    enemy = other
                }
            }
        }
        return enemy
    }


    public Entity findAllVisibleEnemies(params) {
        int distance = params.maxRange
    }



    public void calculateSight() {
        int worldLowX = owner.x - sightRange //low is upper left corner
        int worldLowY = owner.y - sightRange

        int range = 2 * sightRange + 1 // this is the total size of the box

        //Get resistance from map
        float[][] resistances = new float[range][range];
        for (int x = 0; x < range; x++) {
            for (int y = 0; y < range; y++) {
                int originalX = x + worldLowX
                int originalY = y + worldLowY

                if (originalX >= 0 && originalX < owner.levelMap.ground.length
                        && originalY >= 0 && originalY < owner.levelMap.ground[0].length) {
                    resistances[x][y] = owner.levelMap.ground[originalX][originalY].opacity;
                } else {
                    resistances[x][y] = 1f
                }
            }
        }

        //manually set the radius to equal the force
        light = RenderConfig.fov.calculateFOV(resistances, RenderConfig.windowRadiusX, RenderConfig.windowRadiusY, 1f, (1f / RenderConfig.lightForce) as float, RenderConfig.strat);

    }

}
