package com.stewsters.zomgrl.map.gen

import com.stewsters.util.MathUtils
import com.stewsters.util.Rect
import com.stewsters.zomgrl.item.spawner.RandomItemGen
import com.stewsters.zomgrl.map.LevelMap
import squidpony.squidcolor.SColor

class CityLotGenerator {

    private static final int MAX_ROOM_WINDOWS = 3
    private static final int MIN_ROOM_WINDOWS = 1

    private static final int MAX_ROOM_DOORS = 3
    private static final int MIN_ROOM_DOORS = 1


    public static void generate(LevelMap map, Rect lot) {

        for (int x = lot.x1; x <= lot.x2; x++) {
            for (int y = lot.y1; y <= lot.y2; y++) {
                if (x == lot.x1 || x == lot.x2 || y == lot.y1 || y == lot.y2) {
                    map.ground[x][y].color = SColor.WHITE
                    map.ground[x][y].representation = '#' as char
                    map.ground[x][y].opacity = 1f
                    map.ground[x][y].isBlocked = true
                } else {
                    map.ground[x][y].color = SColor.BROWN
                    map.ground[x][y].representation = '.' as char
                    map.ground[x][y].opacity = 0f
                    map.ground[x][y].isBlocked = false
                }
            }
        }

        MathUtils.getIntInRange(MIN_ROOM_WINDOWS, MAX_ROOM_WINDOWS).times {
            switch (MathUtils.getIntInRange(0, 3)) {
                case (0): //top
                    cutWindowInHorizontalWall(map, lot.x1, lot.x2, lot.y1)
                    break;
                case (1)://right
                    cutWindowInVerticalWall(map, lot.x2, lot.y1, lot.y2)
                    break;
                case (2)://bottom
                    cutWindowInVerticalWall(map, lot.x1, lot.y1, lot.y2)
                    break;
                default://left
                    cutWindowInHorizontalWall(map, lot.x1, lot.x2, lot.y2)
                    break;
            }
        }


        MathUtils.getIntInRange(MIN_ROOM_DOORS, MAX_ROOM_DOORS).times {
            switch (MathUtils.getIntInRange(0, 3)) {
                case (0): //top
                    cutDoorInHorizontalWall(map, lot.x1, lot.x2, lot.y1)
                    break;
                case (1)://right
                    cutDoorInVerticalWall(map, lot.x2, lot.y1, lot.y2)
                    break;
                case (2)://bottom
                    cutDoorInVerticalWall(map, lot.x1, lot.y1, lot.y2)
                    break;
                default://left
                    cutDoorInHorizontalWall(map, lot.x1, lot.x2, lot.y2)
                    break;
            }
        }

        addItems(map, lot)

    }



    private static int MAX_ROOM_ITEMS = 10 //this can depend on room type
    private static void addItems(LevelMap map, Rect room) {

        int numItems = MathUtils.getIntInRange(0, MAX_ROOM_ITEMS)
        numItems.times {

            int x = MathUtils.getIntInRange(room.x1 + 1, room.x2 - 1)
            int y = MathUtils.getIntInRange(room.y1 + 1, room.y2 - 1)
            if (!map.isBlocked(x, y)) {
                RandomItemGen.getRandomItem(map, x, y)

            }
        }

    }


    private static void cutWindowInHorizontalWall(LevelMap map, int x1, int x2, int y) {
        int x = MathUtils.getIntInRange(x1 + 1, x2 - 1)
        map.ground[x][y].opacity = 0.25f
        map.ground[x][y].color = SColor.BLUE
        map.ground[x][y].isBlocked = true
        map.ground[x][y].representation = '#'
    }

    private static void cutWindowInVerticalWall(LevelMap map, int x, int y1, int y2) {
        int y = MathUtils.getIntInRange(y1 + 1, y2 - 1)
        map.ground[x][y].opacity = 0.25f
        map.ground[x][y].color = SColor.BLUE
        map.ground[x][y].isBlocked = true
        map.ground[x][y].representation = '#'
    }


    private static void cutDoorInHorizontalWall(LevelMap map, int x1, int x2, int y) {
        int x = MathUtils.getIntInRange(x1 + 1, x2 - 1)
        map.ground[x][y].opacity = 0f
        map.ground[x][y].color = SColor.NEW_BRIDGE
        map.ground[x][y].isBlocked = false
        map.ground[x][y].representation = '+'
    }

    private static void cutDoorInVerticalWall(LevelMap map, int x, int y1, int y2) {
        int y = MathUtils.getIntInRange(y1 + 1, y2 - 1)
        map.ground[x][y].opacity = 0f
        map.ground[x][y].color = SColor.NEW_BRIDGE
        map.ground[x][y].isBlocked = false
        map.ground[x][y].representation = '+'
    }


}
