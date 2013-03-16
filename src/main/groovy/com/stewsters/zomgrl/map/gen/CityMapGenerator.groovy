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

    public static final int BLOCKSIZE = 25


    public LevelMap preGenerate() {

        int width = 200
        int height = 200


        int[][] material = new int[width][height]
        width.times { int iX ->
            height.times { int iY ->
                material[iX][iY] = 5
            }
        }

        def intersections = []


        playerStartX = width / 2
        playerStartY = height / 2


        (width / BLOCKSIZE).times { int x ->
            (height / BLOCKSIZE).times { int y ->

//            int intersectionX = MathUtils.getIntInRange(0 + (int) (BLOCKSIZE / 2), width - (int) (BLOCKSIZE / 2))
//            int intersectionY = MathUtils.getIntInRange(0 + (int) (BLOCKSIZE / 2), height - (int) (BLOCKSIZE / 2))

                int intersectionX = BLOCKSIZE * x
                int intersectionY = BLOCKSIZE * y

                //city blocks
//                intersectionX -= (intersectionX % BLOCKSIZE)
//                intersectionY -= (intersectionY % BLOCKSIZE)

//                def collisions = intersections.find { Intersection e ->
//                    Math.abs(e.centerX - intersectionX) < BLOCKSIZE &&
//                            Math.abs(e.centerY - intersectionY) < BLOCKSIZE
//                }

//                if (!collisions) {
                Intersection intersection = new Intersection(material, intersectionX, intersectionY)
                intersections.add(intersection)

//                    if (intersections)
//                        intersection.linkWithRoads(material, MathUtils.rand(intersections))

//                }
            }
        }
        LevelMap map = convert(material)

//        constructBuildings(map, intersections)
        assumeBuildings(map, intersections)
//        growTrees(map)

//        populate(map, 100)
//        infest(map, 100)
        return map

    }


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

        int maxAttempts = 100
        def intersections = []


        playerStartX = width / 2
        playerStartY = height / 2
        intersections.add new Intersection(material, playerStartX, playerStartY)

        maxAttempts.times {
            int intersectionX = MathUtils.getIntInRange(0 + (int) (BLOCKSIZE / 2), width - (int) (BLOCKSIZE / 2))
            int intersectionY = MathUtils.getIntInRange(0 + (int) (BLOCKSIZE / 2), height - (int) (BLOCKSIZE / 2))

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

//        println intersections

        LevelMap map = convert(material)

        constructBuildings(map, intersections)
//        assumeBuildings(map, intersections)
        growTrees(map)
        populate(map, 100)
        infest(map, 30)
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
                            fighter: new Fighter(hp: 4, defense: 1,
                                    marksman: 1, power: 1,
                                    maxInfection: 2,
                                    stamina: 4,
                                    deathFunction: DeathFunctions.zombieDeath)


                    )
                } else if (d100 < 95) {

                    new Entity(map: map, x: x, y: y,
                            ch: 'H', name: 'Human', color: SColor.WHITE_MOUSE, blocks: true,
                            priority: 120, faction: Faction.human,
                            ai: new BasicCivilian(),
                            inventory: new Inventory(),
                            fighter: new Fighter(hp: 6, defense: 1,
                                    marksman: 1, power: 2,
                                    maxInfection: 2,
                                    stamina: 6,
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
                                    maxInfection: 3,
                                    stamina: 6,
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
                            fighter: new Fighter(hp: 6, defense: 1,
                                    marksman: 0, power: 2,
                                    maxInfection: 3,
                                    infection: 3,
                                    deathFunction: DeathFunctions.zombieDeath)
                    )
                } else if (d100 < 90) {

                    new Entity(map: map, x: x, y: y,
                            ch: 'Z', name: 'Large Zombie', color: SColor.LAWN_GREEN, blocks: true,
                            priority: 120, faction: Faction.zombie,
                            ai: new BasicZombie(),
                            fighter: new Fighter(hp: 10, defense: 1,
                                    marksman: 0, power: 3,
                                    maxInfection: 3,
                                    infection: 3,
                                    deathFunction: DeathFunctions.zombieDeath)

                    )
                } else {

                    new Entity(map: map, x: x, y: y,
                            ch: 'f', name: 'Fast Zombie', color: SColor.DARK_PASTEL_GREEN, blocks: true,
                            priority: 120, faction: Faction.zombie,
                            ai: new BasicZombie(),
                            fighter: new Fighter(hp: 8, defense: 2,
                                    marksman: 0, power: 3,
                                    maxInfection: 3,
                                    infection: 3,
                                    deathFunction: DeathFunctions.zombieDeath),

                    )
                }
            }
        }
    }

    public static final int MIN_BUILDING_SIZE = 5

    private def constructBuildings(LevelMap map, List<Intersection> intersections) {

        def lots = []
        for (Intersection inter : intersections) {

            for (int xMod : [-1, 1]) {
                for (int yMod : [-1, 1]) {
                    int offsetX = (inter.centerX + xMod * (residential.offsetX - (xMod > 0 ? 1 : 0)))
                    int offsetY = (inter.centerX + yMod * (residential.offsetY - (yMod > 0 ? 1 : 0)))
                    int size = MIN_BUILDING_SIZE - 1;
                    boolean clear = map.ground[offsetX][offsetY].color == SColor.GREEN

                    while (clear) {

                        if (clear)
                            size++

                        int lowX = Math.min(offsetX, offsetX + (size * xMod))
                        int lowY = Math.min(offsetY, offsetY + (size * yMod))

                        int highX = Math.max(offsetX, offsetX + (size * xMod)) //- (xMod > 0 ? 1 : 0)
                        int highY = Math.max(offsetY, offsetY + (size * yMod)) //- (yMod > 0 ? 1 : 0)

                        for (int x = lowX; x < highX; x++) {
                            for (int y = lowY; y < highY; y++) {
                                if (x < 0 || x >= map.ground.length || y < 0 || y >= map.ground[0].length) {
                                    clear = false
                                } else if (map.ground[x][y].color != SColor.GREEN) {
                                    clear = false
//                                    map.ground[x][y].representation = '^'
                                }
                                if (!clear)
                                    break
                            }
                            if (!clear)
                                break
                        }

                    }
                    size--

                    if (xMod == -1 && yMod == -1) {
                        println "test"
                    }
                    if (size >= MIN_BUILDING_SIZE) {


                        Rect rect = new Rect(Math.min(offsetX, offsetX + (size * xMod)), Math.min(offsetY, offsetY + (size * yMod)), size, size)
                        lots.add rect

                        println rect.toString()
                    }
                }
            }
        }

        //on each lot, construct a house
        lots = lots.unique({ Rect a, Rect b ->
            if (a.intersect(b))
                return 0
            else return 1
        })
        lots.each { Rect lot ->
            CityLotGenerator.generate(map, lot)
        }

    }


    private def assumeBuildings(LevelMap map, List<Intersection> intersections) {

        def lots = []

        for (Intersection inter : intersections) {

            for (int xMod : [-1, 1]) {
                for (int yMod : [-1, 1]) {
                    int offsetX = (inter.centerX + xMod * (residential.offsetX - (xMod > 0 ? 1 : 0)))
                    int offsetY = (inter.centerX + yMod * (residential.offsetY - (yMod > 0 ? 1 : 0)))
                    int size = 19;

                    int lowX = Math.min(offsetX, offsetX + (size * xMod))
                    int lowY = Math.min(offsetY, offsetY + (size * yMod))

                    int highX = Math.max(offsetX, offsetX + (size * xMod)) //-
                    int highY = Math.max(offsetY, offsetY + (size * yMod))

                    Rect rect = new Rect(lowX, lowY, highX - lowX, highY - lowY)
                    lots.add rect

                }
            }
        }

        //on each lot, construct a house
        lots.unique({ Rect a, Rect b ->
            if (a.intersect(b))
                return 0
            else return 1
        })
        lots.each { Rect lot ->
            CityLotGenerator.generate(map, lot)
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
