package com.stewsters.zomgrl.ai

import com.stewsters.zomgrl.entity.Entity

public interface Ai {

    public void takeTurn()

    //getters and setters
    public Entity getOwner()

    public void setOwner(Entity owner)

    public float[][] getLight()

    public Entity findClosestEnemy(Map maxRange)

    public Entity findClosestVisibleEnemy(Map params)

    public Entity findClosestVisibleItem(Map params)

    public void calculateSight()

}