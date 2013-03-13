package com.stewsters.zomgrl.graphic

import squidpony.squidcolor.SColor
import squidpony.squidgrid.gui.swing.SwingPane

/**
 * This is a quick horizontal status bar
 */
class StatusBar {

/**
 *
 * @param display
 * @param x
 * @param y
 * @param totalWidth
 * @param name
 * @param value
 * @param maximum
 * @param barColor
 * @param backColor
 */
    public static void render(SwingPane display, int x, int y, int totalWidth, String name, int value, int maximum, SColor barColor) {//, SColor backColor

        double ratio = (double) value / (double) maximum
        int barWidth = (int) Math.ceil(ratio * (double) totalWidth)

        (totalWidth + 1).times { xPos ->
            2.times { yPos ->
                display.clearCell(xPos + x, yPos + y)
            }
        }


        (0..(totalWidth)).each { int xOffset ->
            if (xOffset < barWidth || value == maximum)
                display.placeCharacter(x + xOffset, y + 1, '#' as char, barColor)
            else
                display.placeCharacter(x + xOffset, y + 1, '-' as char, barColor)
        }
        display.placeHorizontalString(x, y, "${name}: ${value}/${maximum}")
    }

    //width 20
    public static void renderTextOnly(SwingPane display, int x, int y, String name, int value, int maximum) {

        (20).times { xPos ->
            display.clearCell(xPos + x, y)
        }

        display.placeHorizontalString(x, y, "${name}: ${value}/${maximum}")
    }

}
