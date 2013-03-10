package com.stewsters.zomgrl.map.gen

import com.stewsters.util.MathUtils
import com.stewsters.util.Rect
import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.item.Item
import com.stewsters.zomgrl.map.LevelMap
import com.stewsters.zomgrl.sfx.ItemFunctions
import squidpony.squidcolor.SColor


class RoomGenerator {

    private static final int MAX_ROOM_DOORS
    private static final int MIN_ROOM_DOORS

    public static void generate(LevelMap map, Rect lot) {

        //what sides should have doors? what should have windows?

        // test room TODO:doors


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

        addItems(map, lot)

    }



    private static int MAX_ROOM_ITEMS = 3 //this can depend on room type
    private static void addItems(LevelMap map, Rect room) {

        int numItems = MathUtils.getIntInRange(0, MAX_ROOM_ITEMS)
        numItems.times {

            int x = MathUtils.getIntInRange(room.x1 + 1, room.x2 - 1)
            int y = MathUtils.getIntInRange(room.y1 + 1, room.y2 - 1)
            if (!map.isBlocked(x, y)) {

                int d100 = MathUtils.getIntInRange(0, 100)
                if (d100 < 10) {
                    new Entity(map: map, x: x, y: y,
                            ch: 'p', name: 'AntiViral', color: SColor.AZURE,
                            itemComponent: new Item(useFunction: ItemFunctions.antiviral)
                    )
                } else if (d100 < 30) {
                    new Entity(map: map, x: x, y: y,
                            ch: 'p', name: 'Bandages', color: SColor.AZURE,
                            itemComponent: new Item(useFunction: ItemFunctions.bandage)
                    )
                } else if (d100 < 40) {
                    new Entity(map: map, x: x, y: y,
                            ch: 's', name: 'Snacks', color: SColor.AZURE,
                            itemComponent: new Item(useFunction: ItemFunctions.eat)
                    )
                } else if (d100 < 60) {
                    new Entity(map: map, x: x, y: y,
                            ch: '?', name: "24pk Monster", color: SColor.GREEN,
                            itemComponent: new Item(useFunction: ItemFunctions.heartExplosion)
                    )
                } else if (d100 < 70) {
                    new Entity(map: map, x: x, y: y,
                            ch: 'w', name: "Pump Shotgun", color: SColor.ORANGE,
                            itemComponent: new Item(useFunction: ItemFunctions.gunPumpShotGun)
                    )
                } else if (d100 < 80) {
                    new Entity(map: map, x: x, y: y,
                            ch: 'w', name: "AR-15", color: SColor.AMBER,
                            itemComponent: new Item(useFunction: ItemFunctions.gunAR15)
                    )
                } else if (d100 < 90) {
                    new Entity(map: map, x: x, y: y,
                            ch: 'w', name: "Beretta 9mm", color: SColor.AMARANTH,
                            itemComponent: new Item(useFunction: ItemFunctions.gunBerreta)
                    )
                } else {
                    new Entity(map: map, x: x, y: y,
                            ch: 'w', name: 'Baseball Bat', color: SColor.GOLD,
                            itemComponent: new Item())
                }
            }
        }

    }


    private static void cutDoorInHorizontalWall(LevelMap map, int x1, int x2, int y) {
        int x = MathUtils.getIntInRange(x1+1, x2-1)
        map.ground[x][y].opacity = 0f
        map.ground[x][y].color = SColor.NEW_BRIDGE
        map.ground[x][y].isBlocked = false
        map.ground[x][y].representation = '+'
    }

    private static void cutDoorInVerticalWall(LevelMap map, int x, int y1, int y2) {
        int y = MathUtils.getIntInRange(y1+1, y2-1)
        map.ground[x][y].opacity = 0f
        map.ground[x][y].color = SColor.NEW_BRIDGE
        map.ground[x][y].isBlocked = false
        map.ground[x][y].representation = '+'
    }

}
