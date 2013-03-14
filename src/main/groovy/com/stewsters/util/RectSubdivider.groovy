package com.stewsters.util

public class RectSubdivider {


    public static ArrayList<Rect> divide(Rect rect, Map rules) {

        int minSize = rules.min ?: 4
        int usableWidth = (rect.x2 - rect.x1) - 2
        int usableHeight = (rect.y2 - rect.y1) - 2


        if (usableHeight <= minSize * 2 || usableWidth <= minSize * 2) {
            return [rect]
        }

        //find the larger end, divide
        if (usableWidth > usableHeight) {
            //vertical wall added

            def x = MathUtils.getIntInRange(rect.x1 + minSize, rect.x2 - minSize)

            def y1 = rect.y1
            def h = rect.y2 - rect.y1

            def child1 = new Rect(rect.x1, y1, x - rect.x1 + 1, h + 1)

            def child2 = new Rect(x, y1, rect.x2 - x + 1, h + 1)

            return divide(child1, rules) + divide(child2, rules)

        } else {
            //horizontal wall added

            def y = MathUtils.getIntInRange(rect.y1 + minSize, rect.y2 - minSize)
            def x1 = rect.x1
            def w = rect.x2 - rect.x1

            def child1 = new Rect(x1, rect.y1,  w + 1,   y -rect.y1 + 1)
            def child2 = new Rect(x1, y,        w + 1,  rect.y2 - y + 1)

            return divide(child1, rules) + divide(child2, rules)
        }
    }


}
