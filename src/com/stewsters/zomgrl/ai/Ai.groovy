package com.stewsters.zomgrl.ai

import com.stewsters.zomgrl.entity.Entity

public interface Ai {

    public void takeTurn()

    //getters and setters
    public Entity getOwner()

    public void setOwner(Entity owner)

    public float[][] getLight()

    public Entity findClosestEnemy(int maxRange)

    public void calculateSight()

}