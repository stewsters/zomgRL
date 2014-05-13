package com.stewsters.zomgrl.entity.components.ai

import com.stewsters.zomgrl.entity.Entity

public interface Ai {

    public void takeTurn()

    //getters and setters
    public Entity getOwner()

    public void setOwner(Entity owner)

    public float[][] getLight()

    public Entity findClosestVisibleEnemy(Map params)

    public Entity findClosestVisibleItem(Map params)

    public void calculateSight()

    public void hearNoise(int x, int y)
}