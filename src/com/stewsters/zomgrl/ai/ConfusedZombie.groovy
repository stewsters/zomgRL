package com.stewsters.zomgrl.ai

import com.stewsters.util.MathUtils
import com.stewsters.zomgrl.graphic.MessageLog
import squidpony.squidcolor.SColor

class ConfusedZombie extends BaseAi implements Ai {
    Ai oldAI = null
    int numTurns = 0

    public ConfusedZombie(params) {
        oldAI = params.oldAi
        numTurns = params.numTurns
    }

    public void takeTurn() {

        if (numTurns > 0) {
            owner.move(MathUtils.getIntInRange(-1, 1), MathUtils.getIntInRange(-1, 1))
            numTurns--
        } else {
            owner.ai = oldAI
            MessageLog.send("The ${owner.name} is no longer confused!", SColor.RED)
        }

    }

}
