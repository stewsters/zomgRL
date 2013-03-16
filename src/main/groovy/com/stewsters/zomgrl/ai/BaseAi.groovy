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


    public ArrayList<Entity> findAllVisibleEnemies(Map params) {
        int maxDistance = params?.maxRange ?: sightRange

        int lowX = owner.x - maxDistance
        int highX = owner.x + maxDistance
        int lowY = owner.x - maxDistance
        int highY = owner.x + maxDistance

        return owner.levelMap.objects.findAll { Entity entity ->
            entity.x > lowX && entity.x < highX &&
                    entity.y > lowY && entity.y < highY &&
                    entity.fighter && owner.owner.faction.hates(entity.faction) &&
                    owner.owner.distanceTo(entity) < maxDistance
        }

    }

    /**
     * Irrespective of distance
     * @param params
     * @return
     */
    public Entity findClosestEnemy(Map params) {
        int distance = params?.maxRange ?: Integer.MAX_VALUE
        Entity enemy = null

        calculateSight()

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

    public Entity findClosestVisibleEnemy(Map params) {
        if (!owner.faction) return null
//        int maxDistance = params?.maxRange ?: sightRange
        calculateSight()

        int lowX = owner.x - sightRange
        int highX = owner.x + sightRange
        int lowY = owner.y - sightRange
        int highY = owner.y + sightRange

        Entity enemy = null
        int distance = params?.maxRange ?: sightRange

//        if (owner.faction==Faction.human)
//            println "zomg"

        for (Entity entity : owner.levelMap.objects) {
            if (entity.x > lowX && entity.x < highX && entity.y > lowY && entity.y < highY && entity.fighter && entity.faction && owner.faction.hates(entity.faction)) {
                int lightX = entity.x - lowX
                int lightY = entity.y - lowY
                if (light[lightX][lightY] > 0f) {

                    int tempDist = owner.distanceTo(entity)
                    if (tempDist <= distance) {
                        enemy = entity
                        distance = tempDist
                    }
                }
            }
        }
        return enemy

    }

    public Entity findClosestVisibleItem(Map params) {

        calculateSight()

        int lowX = owner.x - sightRange
        int highX = owner.x + sightRange
        int lowY = owner.y - sightRange
        int highY = owner.y + sightRange

        Entity item = null

        int distance = params?.maxRange ?: sightRange

        for (Entity entity : owner.levelMap.objects) {

            if (entity.x > lowX && entity.x < highX &&
                    entity.y > lowY && entity.y < highY &&
                    entity.itemComponent) {
                int lightX = entity.x - lowX
                int lightY = entity.y - lowY
                if (light[lightX][lightY] > 0f) {

                    int tempDist = owner.distanceTo(entity)
                    if (tempDist <= distance) {
                        item = entity
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
        lightLastCalculated = Game.gameTurn
    }

}
