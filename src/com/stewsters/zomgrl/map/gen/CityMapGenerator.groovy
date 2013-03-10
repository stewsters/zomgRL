package com.stewsters.zomgrl.map.gen

import com.stewsters.util.MathUtils
import com.stewsters.util.Rect
import com.stewsters.util.Simplex2d
import com.stewsters.zomgrl.ai.BasicCivilian
import com.stewsters.zomgrl.ai.BasicZombie
import com.stewsters.zomgrl.ai.Faction
import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.entity.Fighter
import com.stewsters.zomgrl.item.Inventory
import com.stewsters.zomgrl.map.LevelMap
import com.stewsters.zomgrl.map.Tile
import com.stewsters.zomgrl.sfx.DeathFunctions
import squidpony.squidcolor.SColor


class CityMapGenerator implements MapGenerator {

    int playerStartX = 0
    int playerStartY = 0

    public static final int BLOCKSIZE = 20

    @Override
    public LevelMap reGenerate() {

        int width = 200
        int height = 200


        int[][] material = new int[width][height]
        width.times { int iX ->
            height.times { int iY ->
                material[iX][iY] = 5
            }
        }

//        int roadGridSize
//        int[][] roadgrid = new int[width % roadGridSize][height % roadGridSize]

        int maxAttempts = 100
        def intersections = []

        intersections.add new Intersection(material, 100, 100)

        playerStartX = 100
        playerStartY = 100

        maxAttempts.times {
            int intersectionX = MathUtils.getIntInRange(0 + BLOCKSIZE, width - BLOCKSIZE)
            int intersectionY = MathUtils.getIntInRange(0 + BLOCKSIZE, height - BLOCKSIZE)

            //city blocks
            intersectionX -= (intersectionX % BLOCKSIZE)
            intersectionY -= (intersectionY % BLOCKSIZE)

            def collisions = intersections.find { Intersection e ->
                Math.abs(e.centerX - intersectionX) < BLOCKSIZE &&
                        Math.abs(e.centerY - intersectionY) < BLOCKSIZE
            }

            if (!collisions) {
                Intersection intersection = new Intersection(material, intersectionX, intersectionY)

                intersection.linkWithRoads(material, MathUtils.rand(intersections))
                intersections.add(intersection)
            }
        }

        LevelMap map = convert(material)

        constructBuildings(map, intersections)
        growTrees(map)
        populate(map, 100)
        infest(map, 100)
        return map
    }

    def mainStreetBit = [4, 3, 3, 2, 2, 1, 1, 1, 1, 1, 2, 2, 3, 3, 4]

    Map alleyBit = [
            pattern: [[4, 4, 4, 4, 4, 4, 4],
                    [4, 3, 3, 3, 3, 3, 4],
                    [4, 3, 1, 1, 1, 3, 4],
                    [4, 3, 1, 1, 1, 3, 4],
                    [4, 3, 1, 1, 1, 3, 4],
                    [4, 3, 3, 3, 3, 3, 4],
                    [4, 4, 4, 4, 4, 4, 4]],
            offsetX: 4,
            offsetY: 4
    ]

    Map residential = [
            pattern: [[3, 3, 3, 3, 3, 3, 3],
                    [3, 5, 5, 5, 5, 5, 3],
                    [3, 5, 1, 1, 1, 5, 3],
                    [3, 5, 1, 1, 1, 5, 3],
                    [3, 5, 1, 1, 1, 5, 3],
                    [3, 5, 5, 5, 5, 5, 3],
                    [3, 3, 3, 3, 3, 3, 3]],
            offsetX: 4,
            offsetY: 4
    ]

    def carveRoad(int[][] material, def bit, centerX, centerY) {

        //find bit center

        for (int x = 0; x < bit.pattern.size(); x++) {
            for (int y = 0; y < bit.pattern[0].size(); y++) {

                int realX = centerX + x - bit.offsetX
                int realY = centerY + y - bit.offsetY

                int newDepth = bit.pattern[x][y]
                int oldDepth = material[realX][realY]
                material[realX][realY] = Math.min(newDepth, oldDepth)
            }
        }

    }

    def LevelMap convert(int[][] material) {
        LevelMap map = new LevelMap(material.length, material[0].length);
        map.xSize.times { iX ->
            map.ySize.times { iY ->


                Tile tile
                if (iX == 0 || iY == 0 || iX == map.xSize - 1 || iY == map.ySize - 1) {
                    tile = new Tile(true, 0.7f, '₤' as char, SColor.FOREST_GREEN)
                } else {

                    switch (material[iX][iY]) {
                        case 1: //asphalt road
                            tile = new Tile(false, 0f, '.' as char, SColor.SLATE_GRAY)
                            break;
                        case 2: //parking lane
                            tile = new Tile(false, 0f, '.' as char, SColor.SLATE_GRAY)
                            break;
                        case 3: //sidewalk
                            tile = new Tile(false, 0f, ',' as char, SColor.GRAY)
                            break;
                        case 4: //wall
                            tile = new Tile(true, 1f, '#' as char, SColor.LIGHT_GRAY)
                            break;
                        default:
                            tile = new Tile(false, 0f, '.' as char, SColor.GREEN)
                            break;

                    }
                }

                map.ground[iX][iY] = tile
            }
        }
        return map
    }

    private void growTrees(LevelMap map) {
        //Randomly place trees on grass squares

        (0..map.xSize - 1).each { int x ->
            (0..map.ySize - 1).each { int y ->

                //if grass
                if (map.ground[x][y].color == SColor.GREEN) {

                    if (Simplex2d.noise((double) x / 10.0, (double) y / 10.0) > 0.5) {
                        map.ground[x][y].color = SColor.FOREST_GREEN
                        map.ground[x][y].isBlocked = true
                        map.ground[x][y].opacity = 0.7f
                        map.ground[x][y].representation = '₤' as char
                    }

                }


            }

        }


    }

    private void populate(LevelMap map, int maximum) {
        maximum.times {
            int x = MathUtils.getIntInRange(1, map.xSize - 2)
            int y = MathUtils.getIntInRange(1, map.ySize - 2)

            if (!map.isBlocked(x, y)) {
                int d100 = MathUtils.getIntInRange(0, 100)
                if (d100 < 70) {
                    new Entity(map: map, x: x, y: y,
                            ch: 'h', name: 'Human', color: SColor.WHITE_TEA_DYE, blocks: true,
                            priority: 120, faction: Faction.human,
                            ai: new BasicCivilian(),
                            inventory: new Inventory(),
                            fighter: new Fighter(hp: 4, defense: 0,
                                    marksman: 1, power: 1,
                                    max_infection: 2,
                                    max_stamina: 4,
                                    deathFunction: DeathFunctions.zombieDeath)


                    )
                } else if (d100 < 95) {

                    new Entity(map: map, x: x, y: y,
                            ch: 'H', name: 'Human', color: SColor.WHITE_MOUSE, blocks: true,
                            priority: 120, faction: Faction.human,
                            ai: new BasicCivilian(),
                            inventory: new Inventory(),
                            fighter: new Fighter(hp: 6, defense: 0,
                                    marksman: 1, power: 2,
                                    max_infection: 2,
                                    max_stamina: 6,
                                    deathFunction: DeathFunctions.zombieDeath)

                    )
                } else {

                    new Entity(map: map, x: x, y: y,
                            ch: 'P', name: 'Police', color: SColor.WHITE, blocks: true,
                            priority: 120, faction: Faction.human,
                            ai: new BasicCivilian(),
                            inventory: new Inventory(),
                            fighter: new Fighter(hp: 6, defense: 1,
                                    marksman: 3, power: 2,
                                    max_infection: 3,
                                    max_stamina: 6,
                                    deathFunction: DeathFunctions.zombieDeath)


                    )
                }
            }
        }

    }


    private void infest(LevelMap map, int maximum) {
        //fill in zombies
        maximum.times {
            int x = MathUtils.getIntInRange(1, map.xSize - 2)
            int y = MathUtils.getIntInRange(1, map.ySize - 2)

            if (!map.isBlocked(x, y)) {
                int d100 = MathUtils.getIntInRange(0, 100)
                if (d100 < 70) {
                    new Entity(map: map, x: x, y: y,
                            ch: 'z', name: 'Zombie', color: SColor.SEA_GREEN, blocks: true,
                            priority: 120, faction: Faction.zombie,
                            ai: new BasicZombie(),
                            fighter: new Fighter(hp: 6, defense: 0,
                                    marksman: 0, power: 2,
                                    max_infection: 3,
                                    infection: 3,
                                    deathFunction: DeathFunctions.zombieDeath)


                    )
                } else if (d100 < 90) {

                    new Entity(map: map, x: x, y: y,
                            ch: 'Z', name: 'Large Zombie', color: SColor.LAWN_GREEN, blocks: true,
                            priority: 120, faction: Faction.zombie,
                            ai: new BasicZombie(),
                            fighter: new Fighter(hp: 10, defense: 0,
                                    marksman: 0, power: 3,
                                    max_infection: 3,
                                    infection: 3,
                                    deathFunction: DeathFunctions.zombieDeath)

                    )
                } else {

                    new Entity(map: map, x: x, y: y,
                            ch: 'f', name: 'Fast Zombie', color: SColor.DARK_PASTEL_GREEN, blocks: true,
                            priority: 120, faction: Faction.zombie,
                            ai: new BasicZombie(),
                            fighter: new Fighter(hp: 8, defense: 1,
                                    marksman: 0, power: 3,
                                    max_infection: 3,
                                    infection: 3,
                                    deathFunction: DeathFunctions.zombieDeath),

                    )
                }
            }
        }
    }

    public static final int MIN_BUILDING_SIZE = 4

    private def constructBuildings(LevelMap map, List<Intersection> intersections) {

        def lots = []
        for (Intersection inter : intersections) {

            int offsetX = inter.centerX + residential.offsetX - 1
            int offsetY = inter.centerX + residential.offsetY - 1

            int size = MIN_BUILDING_SIZE - 1; //
            boolean clear = map.ground[offsetX][offsetY].color == SColor.GREEN
            while (clear) { //TODO: this could be more efficient

                for (int x = offsetX; x < offsetX + size; x++) {
                    for (int y = offsetY; y < offsetY + size; y++) {
                        clear &= map.ground[x][y].color == SColor.GREEN
                    }
                }

                if (clear)
                    size++
            }
            if (size >= MIN_BUILDING_SIZE) {
                lots.add new Rect(offsetX, offsetY, size, size)
            }

            //see if offset if blocked.  if it isnt, start drawing a box until it is
            //this is a lot
        }

        //on each lot, construct a house
        lots.each { Rect lot ->
            RoomGenerator.generate(map, lot)
        }

    }


    private class Intersection {

        int centerX
        int centerY

        Intersection(int[][] material, x, y) {
            centerX = x
            centerY = y
            carveRoad(material, residential, centerX, centerY)
        }


        void linkWithRoads(int[][] material, Intersection other) {

            if (MathUtils.getBoolean()) {
                createHRoad(material, other.centerX, centerX, other.centerY)
                createVRoad(material, other.centerY, centerY, centerX)
            } else {
                createVRoad(material, other.centerY, centerY, other.centerX)
                createHRoad(material, other.centerX, centerX, centerY)
            }

        }

        private void createHRoad(int[][] material, int x1, int x2, int y) {
            (Math.min(x1, x2)..Math.max(x1, x2)).each { int x ->
                carveRoad(material, residential, x, y)
            }
        }

        private void createVRoad(int[][] material, int y1, int y2, int x) {
            (Math.min(y1, y2)..Math.max(y1, y2)).each { int y ->
                carveRoad(material, residential, x, y)
            }
        }


    }

}
