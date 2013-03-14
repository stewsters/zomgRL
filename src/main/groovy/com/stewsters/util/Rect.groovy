package com.stewsters.util

/**
 * int rectangle
 */
class Rect {
    int x1
    int x2
    int y1
    int y2


    public Rect(x, y, w, h) {
        this.x1 = x
        this.y1 = y
        this.x2 = x + w - 1
        this.y2 = y + h - 1
    }

    public def center() {
        int center_x = (x1 + x2) / 2
        int center_y = (y1 + y2) / 2
        return [center_x, center_y]
    }

    public boolean intersect(Rect other) {
        return (x1 <= other.x2 &&
                x2 >= other.x1 &&
                y1 <= other.y2 &&
                y2 >= other.y1)
    }

    public String toString(){
        return "$x1 $y1 $x2 $y2"
    }

}
