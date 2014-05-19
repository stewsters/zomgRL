package com.stewsters.zomgrl.entity.components.ai

import com.stewsters.util.math.MatUtils
import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.entity.components.item.Equipment
import com.stewsters.zomgrl.entity.components.item.Slot

public class AdvancedStats extends BaseAi implements Ai {

    private float morale
    private float chargeProbability
    private float retreatProbability

    public AdvancedStats(Map params) {
        morale = params.morale ?: 0.5f
        chargeProbability = params.chargeProbability ?: 0.5f
        retreatProbability = params.retreatProbability ?: 0.5f
    }

    //http://dillingers.com/blog/2014/05/10/roguelike-ai/
    public void takeTurn() {

        //nearest opponent
        Entity enemy = findClosestVisibleEnemy()

        if (enemy) {
            //if we have a gun, and they are getting too close, shoot them
//            if(owner.fighter && ((owner.fighter.hp/owner.fighter.maxHP ) < morale )){
//                //flee
//                if(!owner.moveAway(enemy.x,enemy.y)){
//                    owner.moveTowardsAndAttack(enemy.x,enemy.y)
//                }
//            }
//            else if too-far-from-player
//                AND can-attack-player
//                AND can-move-toward-player
//                if  random < charge-probability
//                    move-toward-player
//                else attack-player
//            else if too-close-to-character
//                AND can-attack-player
//                AND can-move-away-from-player
//                if random < retreat-probability
//                    move-away-from-player
//                else attack-player
//            else if can-attack-player
//                attack-player
//            else if too-far-from-player
//                AND can-move-toward-player
//                move-toward-player
//            else if too-close-to-player
//                AND can-move-away-from-player
//                move-away-from-player
//            else if (MatUtils.boolean) {
//                owner.randomMovement();
//            }




            if (owner.distanceTo(enemy) < 2) {
                owner.moveTowardsAndAttack(enemy.x, enemy.y)
            } else {
                if (owner.inventory) {
                    Equipment weapon = owner.inventory.getEquippedInSlot(Slot.rightHand)
                    if (weapon && weapon.owner.itemComponent.useFunction != null) {
                        weapon.owner.itemComponent.useHeldItem(owner)
                        println "Bang!"
                    } else {
                        owner.moveAway(enemy.x, enemy.y)
                    }
                } else {
                    owner.moveAway(enemy.x, enemy.y)
                }
            }
        } else if (owner.inventory) {

            //if we are standing on an item and we have room, pick it up
            if (owner.inventory.isFull()) {
                owner.randomMovement()
            } else {
                //find nearest visible item
                Entity item = owner.ai.findClosestVisibleItem()

                //if we are standing on it, pickUp
                if (item) {
                    if (item.x == owner.x && item.y == owner.y) {
                        owner.inventory.pickUp(item)
                    } else {
                        owner.moveTowardsAndAttack(item.x, item.y)
                    }
                } else {
                    owner.randomMovement()
                }

            }
        } else if (MatUtils.boolean) {
            owner.randomMovement();
        }

    }
}
