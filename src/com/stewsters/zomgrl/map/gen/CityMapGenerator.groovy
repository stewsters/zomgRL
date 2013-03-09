package com.stewsters.zomgrl.map.gen

import com.stewsters.util.MathUtils
import com.stewsters.zomgrl.ai.BasicZombie
import com.stewsters.zomgrl.ai.Faction
import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.entity.Fighter
import com.stewsters.zomgrl.map.LevelMap
import com.stewsters.zomgrl.map.Tile
import com.stewsters.zomgrl.sfx.DeathFunctions
import squidpony.squidcolor.SColor


class CityMapGenerator implements MapGenerator {

    int playerStartX = 0
    int playerStartY = 0


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
            int intersectionX = MathUtils.getIntInRange(0 + Intersection.INTERSECTION_WIDTH, width - Intersection.INTERSECTION_WIDTH)
            int intersectionY = MathUtils.getIntInRange(0 + Intersection.INTERSECTION_WIDTH, height - Intersection.INTERSECTION_WIDTH)

            def collisions = intersections.find { Intersection e ->
                Math.abs(e.centerX - intersectionX) < Intersection.INTERSECTION_WIDTH &&
                        Math.abs(e.centerY - intersectionY) < Intersection.INTERSECTION_WIDTH
            }

            if (!collisions) {
                Intersection intersection = new Intersection(material, intersectionX, intersectionY)

                intersection.linkWithRoads(material, MathUtils.rand(intersections))
                intersections.add(intersection)
            }
        }


        LevelMap map = convert(material)

        infest(map,100)
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
                if (iX == 0 || iY == 0 || iX == map.xSize || iY == map.ySize) {
                    tile = new Tile(true, 0.7f, '₤' as char, SColor.FOREST_GREEN)
                }else{

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

                }}

                map.ground[iX][iY] = tile
            }
        }
        return map
    }

    private void growTrees(){
        //Randomly place trees on grass squares
    }

    private void infest(LevelMap map, int maximum){
        //fill in zombies
        maximum.times{
            int x = MathUtils.getIntInRange(1,map.xSize-2)
            int y = MathUtils.getIntInRange(1,map.ySize-2)

            if(!map.isBlocked(x,y)){
                int d100 = MathUtils.getIntInRange(0, 100)
                if (d100 < 70) {
                    new Entity(map: map, x: x, y: y,
                            ch: 'z', name: 'Zombie', color: SColor.SEA_GREEN, blocks: true,
                            fighter: new Fighter(4, 0, 1, DeathFunctions.zombieDeath),
                            ai: new BasicZombie(),
                            priority: 120, faction: Faction.zombie
                    )
                } else if (d100 < 90) {

                    new Entity(map: map, x: x, y: y,
                            ch: 'Z', name: 'Large Zombie', color: SColor.LAWN_GREEN, blocks: true,
                            priority: 120, faction: Faction.zombie,
                            fighter: new Fighter(10, 0, 2, DeathFunctions.zombieDeath),
                            ai: new BasicZombie()
                    )
                } else {

                    new Entity(map: map, x: x, y: y,
                            ch: 'f', name: 'Fast Zombie', color: SColor.DARK_PASTEL_GREEN, blocks: true,
                            priority: 120, faction: Faction.zombie,
                            fighter: new Fighter(4, 1, 3, DeathFunctions.zombieDeath),
                            ai: new BasicZombie()
                    )
                }
            }
        }


    }


    private class Intersection {

        public static final INTERSECTION_WIDTH = 20

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
