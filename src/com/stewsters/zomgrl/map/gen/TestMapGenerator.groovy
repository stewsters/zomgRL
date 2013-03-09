package com.stewsters.zomgrl.map.gen


import com.stewsters.util.MathUtils
import com.stewsters.util.Rect
import com.stewsters.zomgrl.ai.BasicZombie
import com.stewsters.zomgrl.ai.Faction
import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.entity.Fighter
import com.stewsters.zomgrl.map.LevelMap
import com.stewsters.zomgrl.map.Tile
import com.stewsters.zomgrl.sfx.DeathFunctions
import squidpony.squidcolor.SColor

class TestMapGenerator implements MapGenerator {

    int playerStartX = 0
    int playerStartY = 0

    @Override
    public LevelMap reGenerate() {
        int width = 20
        int height = 20
        LevelMap map = new LevelMap(width, height);

        map.xSize.times { iX ->
            map.ySize.times { iY ->
                map.ground[iX][iY] = new Tile(true, 1f, '#' as char, SColor.SLATE_GRAY)
            }
        }

        createRoom(map, new Rect(0, 0, map.xSize - 1, map.ySize - 1))
        playerStartX = map.xSize / 2 - 1
        playerStartY = map.ySize / 2 - 1

        new Entity(map: map, x: playerStartX + 2, y: playerStartY + 2,
                ch: 'T', name: 'Troll', color: SColor.DARK_PASTEL_GREEN, blocks: true,
                priority: 120, faction: Faction.zombie,
                fighter: new Fighter(15, 1, 3, DeathFunctions.zombieDeath),
                ai: new BasicZombie()
        )
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


