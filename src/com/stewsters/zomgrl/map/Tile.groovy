package com.stewsters.zomgrl.map

import squidpony.squidcolor.SColor

public class Tile {
    public Boolean isBlocked = true
    public Boolean isExplored = false

    public char representation
    public SColor color
    public float opacity;

    public Tile(Boolean blocked, float opacity, char representation, SColor color) {
        this.isBlocked = blocked
        this.representation = representation
        this.color = color
        this.opacity = opacity
    }

}
