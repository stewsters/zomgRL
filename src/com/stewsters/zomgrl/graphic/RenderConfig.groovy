package com.stewsters.zomgrl.graphic

import squidpony.squidcolor.SColor
import squidpony.squidcolor.SColorFactory
import squidpony.squidgrid.fov.LOSSolver
import squidpony.squidgrid.fov.RadiusStrategy
import squidpony.squidgrid.fov.TranslucenceWrapperFOV

/**
 * This file just really stores some configs
 */

class RenderConfig {

    public static SColor litNear = SColorFactory.asSColor(SColor.WHITE);
    public static SColor litFarDay = SColorFactory.asSColor(SColor.LIGHT_YELLOW_DYE);
    public static SColor litFarNight = SColorFactory.asSColor(SColor.DARK_BLUE);

    public static float lightForce = 20f; //controls how far the light will spread
    public static float lightTintPercentage = 0.03f; //0 to 1

    public static LOSSolver los;
    public static TranslucenceWrapperFOV fov;
    public static RadiusStrategy strat;


    public static final int windowRadiusX = 20
    public static final int windowRadiusY = 20

    //total
    public static final int screenWidth = 80
    public static final int screenHeight = 50

    //Messaging sizes
    public static final int messageX = 42
    public static final int messageY = 11
    public static final int messageHeight = 30
    public static final int messageWidth = 35

    //
    public static final int inventoryX = 42
    public static final int inventoryY = 0
    public static final int inventoryHeight = 8
    public static final int inventoryWidth = 35

}
