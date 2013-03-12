package com.stewsters.zomgrl.game

class Game {
    public static GameState state = GameState.setup
    public static int gameTurn = 1


    public static passTime() {
        gameTurn++
    }

    public static final int DAYLENGTH = 100

    public static boolean isDay() {
        return (gameTurn % (2 * DAYLENGTH)) < DAYLENGTH
    }

}
