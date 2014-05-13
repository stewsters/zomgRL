package com.stewsters.zomgrl.map.gen

import com.stewsters.util.math.MatUtils
import com.stewsters.util.math.Point2i
import com.stewsters.util.math.geom.Rect
import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.entity.components.Fighter
import com.stewsters.zomgrl.entity.components.ai.BasicZombie
import com.stewsters.zomgrl.entity.components.ai.Faction
import com.stewsters.zomgrl.entity.components.item.Item
import com.stewsters.zomgrl.graphic.MessageLog
import com.stewsters.zomgrl.map.LevelMap
import com.stewsters.zomgrl.map.Tile
import com.stewsters.zomgrl.sfx.DeathFunctions
import com.stewsters.zomgrl.sfx.ItemFunctions
import squidpony.squidcolor.SColor

class SimpleMapGenerator implements MapGenerator {
/* 4*4
        ++++
        +..+
        +..+
        ++++
*/
    //This room includes the walls
    private static int ROOM_MAX_SIZE = 12
    private static int ROOM_MIN_SIZE = 6
    private static int MAX_ROOMS = 30
    private static int MAX_ROOM_MONSTERS = 3
    private static int MAX_ROOM_ITEMS = 3

    int playerStartX = 0
    int playerStartY = 0

    @Override
    public LevelMap reGenerate() {

        int width = 100
        int height = 100
        LevelMap map = new LevelMap(width, height);

        map.xSize.times { iX ->
            map.ySize.times { iY ->
                map.ground[iX][iY] = new Tile(true, 1f, '#' as char, SColor.SLATE_GRAY)
            }
        }

        List<Rect> rooms = []
        int num_rooms = 0


        MAX_ROOMS.times { roomNo ->

            int w = MatUtils.getIntInRange(ROOM_MIN_SIZE, ROOM_MAX_SIZE)
            int h = MatUtils.getIntInRange(ROOM_MIN_SIZE, ROOM_MAX_SIZE)

            int roomX = MatUtils.getIntInRange(0, (map.xSize - w) - 1)
            int roomY = MatUtils.getIntInRange(0, (map.ySize - h) - 1)

            Rect new_room = new Rect(roomX, roomY, w, h)
            boolean failed = false
            for (Rect otherRoom : rooms) {
                if (new_room.intersect(otherRoom))
                    failed = true
                break
            }
            if (!failed) {

                createRoom(map, new_room)

                Point2i center = new_room.center()

                if (num_rooms == 0) {
                    //set player start
                    playerStartX = center.x
                    playerStartY = center.y

                } else {
                    placeObjects(map, new_room)

                    Point2i lastCenter = rooms[(num_rooms - 1)].center()
                    Point2i prev = new Point2i(lastCenter.x, lastCenter.y)

                    if (MatUtils.getBoolean()) {
                        createHTunnel(map, prev.x, center.x, prev.y)
                        createVTunnel(map, prev.y, center.y, center.x)
                    } else {
                        createVTunnel(map, prev.y, center.y, prev.x)
                        createHTunnel(map, prev.x, center.x, center.y)
                    }
                }
                rooms.add(new_room)
                num_rooms++
            } else {
                MessageLog.log("failed to place room " + roomNo)
            }
        }
        return map
    }

    /**
     * Paint a room onto the map's tiles
     * @return
     */
    private static void createRoom(LevelMap map, Rect room) {

        ((room.x1 + 1)..(room.x2 - 1)).each { int x ->
            ((room.y1 + 1)..(room.y2 - 1)).each { int y ->
                map.ground[x][y].isBlocked = false
                map.ground[x][y].representation = '.' as char
                map.ground[x][y].color = SColor.WHITE
                map.ground[x][y].opacity = 0f;
            }
        }
    }

    private static void createHTunnel(LevelMap map, int x1, int x2, int y) {
        (Math.min(x1, x2)..Math.max(x1, x2)).each { int x ->
            map.ground[x][y].isBlocked = false
            map.ground[x][y].representation = '.' as char
            map.ground[x][y].color = SColor.WHITE
            map.ground[x][y].opacity = 0f;
        }
    }

    private static void createVTunnel(LevelMap map, int y1, int y2, int x) {
        (Math.min(y1, y2)..Math.max(y1, y2)).each { int y ->
            map.ground[x][y].isBlocked = false
            map.ground[x][y].representation = '.' as char
            map.ground[x][y].color = SColor.WHITE
            map.ground[x][y].opacity = 0f;
        }
    }

    private static void placeObjects(LevelMap map, Rect room) {

        int numMonsters = MatUtils.getIntInRange(0, MAX_ROOM_MONSTERS)

        numMonsters.times {
            int x = MatUtils.getIntInRange(room.x1 + 1, room.x2 - 1)
            int y = MatUtils.getIntInRange(room.y1 + 1, room.y2 - 1)

            if (!map.isBlocked(x, y)) {

                int d100 = MatUtils.getIntInRange(0, 100)
                if (d100 < 70) {
                    new Entity(map: map, x: x, y: y,
                            ch: 'g', name: 'Goblin', color: SColor.SEA_GREEN, blocks: true,
                            fighter: new Fighter(hp: 4, defense: 0, power: 1, stamina: 4, deathFunction: DeathFunctions.zombieDeath),
                            ai: new BasicZombie(),
                            priority: 120, faction: Faction.zombie
                    )
                } else if (d100 < 90) {

                    new Entity(map: map, x: x, y: y,
                            ch: 'o', name: 'Orc', color: SColor.LAWN_GREEN, blocks: true,
                            priority: 120, faction: Faction.zombie,
                            fighter: new Fighter(hp: 10, defense: 0, power: 2, stamina: 4, deathFunction: DeathFunctions.zombieDeath),
                            ai: new BasicZombie()
                    )
                } else {

                    new Entity(map: map, x: x, y: y,
                            ch: 'T', name: 'Troll', color: SColor.DARK_PASTEL_GREEN, blocks: true,
                            priority: 120, faction: Faction.zombie,
                            fighter: new Fighter(hp: 16, defense: 1, power: 3, stamina: 4, deathFunction: DeathFunctions.zombieDeath),
                            ai: new BasicZombie()
                    )
                }
            }
        }

        //now items
        int numItems = MatUtils.getIntInRange(0, MAX_ROOM_ITEMS)
        numItems.times {
            int x = MatUtils.getIntInRange(room.x1 + 1, room.x2 - 1)
            int y = MatUtils.getIntInRange(room.y1 + 1, room.y2 - 1)
            if (!map.isBlocked(x, y)) {

                int d100 = MatUtils.getIntInRange(0, 100)
                if (d100 < 40) {
                    new Entity(map: map, x: x, y: y,
                            ch: 'p', name: 'Healing Potion', color: SColor.AZURE,
                            itemComponent: new Item(useFunction: ItemFunctions.castHeal)
                    )
                } else if (d100 < 60) {
                    new Entity(map: map, x: x, y: y,
                            ch: '?', name: "Scroll Of Fireball", color: SColor.RED,
                            itemComponent: new Item(useFunction: ItemFunctions.castFireball)
                    )
                } else if (d100 < 70) {
                    new Entity(map: map, x: x, y: y,
                            ch: '?', name: "Scroll Of Lightning", color: SColor.ORANGE,
                            itemComponent: new Item(useFunction: ItemFunctions.castLightning)
                    )
                } else if (d100 < 80) {
                    new Entity(map: map, x: x, y: y,
                            ch: '?', name: "Scroll Of Domination", color: SColor.AMBER,
                            itemComponent: new Item(useFunction: ItemFunctions.castDomination)
                    )
                } else if (d100 < 90) {
                    new Entity(map: map, x: x, y: y,
                            ch: '?', name: "Scroll Of Confustion", color: SColor.AMARANTH,
                            itemComponent: new Item(useFunction: ItemFunctions.castConfuse)
                    )
                } else {
                    new Entity(map: map, x: x, y: y,
                            ch: 's', name: 'Sword', color: SColor.GOLD,
                            itemComponent: new Item([:]))
                }
            }
        }
    }
}
