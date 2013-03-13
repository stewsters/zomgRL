package com.stewsters.zomgrl.map.gen


import com.stewsters.util.MathUtils
import com.stewsters.util.Rect
import com.stewsters.zomgrl.ai.BasicZombie
import com.stewsters.zomgrl.ai.Faction
import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.entity.Fighter
import com.stewsters.zomgrl.item.spawner.RandomItemGen
import com.stewsters.zomgrl.map.LevelMap
import com.stewsters.zomgrl.map.Tile
import com.stewsters.zomgrl.sfx.DeathFunctions
import squidpony.squidcolor.SColor

class TestMapGenerator implements MapGenerator {

    int playerStartX = 0
    int playerStartY = 0

    @Override
    public LevelMap reGenerate() {
        int width = 40
        int height = 40
        LevelMap map = new LevelMap(width, height);

        map.xSize.times { iX ->
            map.ySize.times { iY ->
                map.ground[iX][iY] = new Tile(true, 1f, '#' as char, SColor.SLATE_GRAY)
            }
        }

        createRoom(map, new Rect(0, 0, map.xSize - 1, map.ySize - 1))
        playerStartX = map.xSize / 2 - 1
        playerStartY = map.ySize / 2 - 1

        new Entity(map: map, x:playerStartX + 10, y: playerStartY + 10,
                ch: 'Z', name: 'Large Zombie', color: SColor.LAWN_GREEN, blocks: true,
                priority: 120, faction: Faction.zombie,
                ai: new BasicZombie(),
                fighter: new Fighter(hp: 10, defense: 1,
                        marksman: 0, power: 3,
                        maxInfection: 3,
                        infection: 3,
                        deathFunction: DeathFunctions.zombieDeath)
        )

        /**
         * Items
         */

        RandomItemGen.spawnChance.keySet().eachWithIndex{String name, int i ->
            RandomItemGen.createFromName(map,2,2+i,name)
        }

        return map
    }

    /**
     * Paint a room onto the map's tiles
     * @return
     */
    private void createRoom(LevelMap map, Rect room) {

        ((room.x1 + 1)..(room.x2 - 1)).each { int x ->
            ((room.y1 + 1)..(room.y2 - 1)).each { int y ->
                map.ground[x][y].isBlocked = false
                map.ground[x][y].representation = '.' as char
                map.ground[x][y].color = SColor.WHITE
                map.ground[x][y].opacity = 0f;
            }
        }

        int x = MathUtils.getIntInRange(room.x1 + 1, room.x2 - 1)
        int y = MathUtils.getIntInRange(room.y1 + 1, room.y2 - 1)

        map.ground[x][y].isBlocked = true
        map.ground[x][y].representation = '|' as char
        map.ground[x][y].color = SColor.LIGHT_BLUE
        map.ground[x][y].opacity = 0.5f;
    }

}


