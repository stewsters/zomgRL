package com.stewsters.zomgrl.ai

import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.game.Game
import com.stewsters.zomgrl.graphic.RenderConfig


/**
 * These are functions common to all AIs
 */
abstract class BaseAi {

    Entity owner
    float[][] light
    int lightLastCalculated = 0
    int sightRange = 20




    public ArrayList<Entity> findAllVisibleEnemies(params) {
        int maxDistance = params?.maxRange ?: sightRange

        int lowX = owner.x - maxDistance
        int highX = owner.x + maxDistance
        int lowY = owner.x - maxDistance
        int highY = owner.x + maxDistance

        return owner.levelMap.objects.findAll { Entity it ->
            it.x > lowX && it.x < highX &&
                    it.y > lowY && it.y < highY &&
                    it.fighter && owner.owner.faction.hates(it.faction) &&
                    owner.owner.distanceTo(it) < maxDistance
        }

    }


    public Entity findClosestEnemy(int maxRange = Integer.MAX_VALUE) {
        int distance = maxRange
        Entity enemy = null

        calculateSight() // todo:this could be moved to go once per

        for (Entity other : owner.levelMap.objects) {
            if (other.fighter && other.fighter.hp > 0 &&
                    other.faction && owner.faction?.hates(other?.faction)) {

                int thisDistance = owner.distanceTo(other)
                if (distance > thisDistance) {
                    distance = thisDistance
                    enemy = other
                }

            }
        }
        return enemy
    }

    public Entity findClosestVisibleEnemy(params) {
        int maxDistance = params?.maxRange ?: sightRange
        calculateSight()

        int lowX = owner.x - maxDistance
        int highX = owner.x + maxDistance
        int lowY = owner.y - maxDistance
        int highY = owner.y + maxDistance

        Entity enemy = null
        int distance = maxDistance

        for (Entity it : owner.levelMap.objects) {

            if (it.x > lowX && it.x < highX &&
                    it.y > lowY && it.y < highY &&
                    it.fighter && owner.faction.hates(it.faction)) {
                int lightX = it.x - lowX
                int lightY = it.y - lowY
                if (light[lightX][lightY]>0) {

                    int tempDist = owner.distanceTo(it)
                    if (tempDist <= distance) {
                        enemy = it
                        distance = tempDist
                    }
                }
            }
        }
        return enemy

    }

    public Entity findClosestVisibleItem(params){
        int maxDistance = params?.maxRange ?: sightRange
        calculateSight()

        int lowX = owner.x - maxDistance
        int highX = owner.x + maxDistance
        int lowY = owner.y - maxDistance
        int highY = owner.y + maxDistance

        Entity item = null
        int distance = maxDistance

        for (Entity it : owner.levelMap.objects) {

            if (it.x > lowX && it.x < highX &&
                    it.y > lowY && it.y < highY &&
                    it.itemComponent) {
                int lightX = it.x - lowX
                int lightY = it.y - lowY
                if (light[lightX][lightY]>0) {

                    int tempDist = owner.distanceTo(it)
                    if (tempDist <= distance) {
                        item = it
                        distance = tempDist
                    }
                }
            }
        }
        return item

    }

    public void calculateSight() {
        if (lightLastCalculated == Game.gameTurn)
            return

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
