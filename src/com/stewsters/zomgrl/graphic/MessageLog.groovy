package com.stewsters.zomgrl.graphic

import org.apache.commons.lang3.text.WordUtils
import squidpony.squidcolor.SColor
import squidpony.squidgrid.gui.swing.SwingPane

class MessageLog {


    private def static gameMessages = [] as LinkedList

    public static void send(String message, def color = SColor.WHITE) {
        WordUtils.wrap('>' + message, RenderConfig.messageWidth).eachLine {
            gameMessages.addLast([message: it, color: color])

            if (gameMessages.size() > RenderConfig.messageHeight)
                gameMessages.poll()
        }
    }

    public static void render(SwingPane display) {

        (0..RenderConfig.messageWidth).each { x ->
            (0..RenderConfig.messageHeight).each { y ->
                display.clearCell(x + RenderConfig.messageX, y + RenderConfig.messageY)
            }
        }


        gameMessages.eachWithIndex { Map msg, int index ->
            display.placeHorizontalString(RenderConfig.messageX, RenderConfig.messageY + index, msg.message, msg.color, SColor.BLACK)
        }


    }

    /* Should consider actually using some kind of logging framework*/

    static void log(String message) {
        println message
    }
}
