package com.stewsters.zomgrl.ai

import com.stewsters.util.math.MatUtils
import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.graphic.MessageLog
import squidpony.squidcolor.SColor

class ConfusedZombie extends BaseAi implements Ai {
    Ai oldAI = null
    int numTurns = 0
    Entity castor

    public ConfusedZombie(params) {
        oldAI = params.oldAi
        numTurns = params.numTurns
        castor = params.castor
    }

    public void takeTurn() {

        if (numTurns > 0) {
            owner.move(MatUtils.getIntInRange(-1, 1), MatUtils.getIntInRange(-1, 1))
            numTurns--
        } else {
            owner.ai = oldAI
            MessageLog.send("The ${owner.name} is no longer confused!", SColor.RED, [owner, castor])
        }

    }

}
