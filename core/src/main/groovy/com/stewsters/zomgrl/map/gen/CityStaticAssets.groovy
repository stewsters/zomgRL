package com.stewsters.zomgrl.map.gen

import com.stewsters.util.math.geom.Rect
import com.stewsters.zomgrl.map.LevelMap
import com.stewsters.zomgrl.map.Tile
import squidpony.squidcolor.SColor

public class CityStaticAssets {

    public static boolean populate(LevelMap map, Rect rect) {

        int rectWidth = rect.x2 - rect.x1;
        int rectHeight = rect.y2 - rect.y1;

        def lots = StaticLots.manifest.findAll {
            it.x <= rectWidth && it.y <= rectHeight
        }

        Collections.shuffle(lots)
        def lot = lots[0]

        if (lot) {
            for (int x = 0; x < lot.x; x++) {
                for (int y = 0; y < lot.y; y++) {
                    char c = lot.map[y].charAt(x);
                    map.ground[rect.x1 + x][rect.y1 + y] = buildCell(c);
                }
            }
            return true;
        }
        return false;
    }

    private static Tile buildCell(char c) {
        float resistance = 0f;//default is transparent
        SColor color;
        boolean blocks = false;
        switch (c) {
            case '.'://grass
                color = SColor.GREEN;
                break;
            case '`'://parking lane
                color = SColor.SLATE_GRAY;
                c = '.';
                break;
            case ','://pavement
                color = SColor.GRAY;
                break;
            case '_':
                color = SColor.BROWN
                c = '.'
                break
            case '=':
                color = SColor.BLUE
                resistance = 0.25f
                blocks = true
                c = '#'
                break
            case '~':
                color = SColor.BLUE;
                blocks = true
                break;
            case '+'://door
                color = SColor.NEW_BRIDGE;
                resistance = 0.5f;
                break;
            case '₤':
                blocks = true;
                color = SColor.FOREST_GREEN;
                resistance = 0.7f;
                break;
            case 'X': //chest high wall
                blocks = true;
                color = SColor.INDIGO_WHITE;
                resistance = 0.5f;
                break;
            case '#': //wall
                blocks = true;
                color = SColor.SLATE_GRAY;
                resistance = 1f;
                break;
            case '/':
                color = SColor.BROWNER;
                break;
            default://opaque items
                c = '.';
                //unknown is grass
                color = SColor.GREEN;
        }
        return new Tile(blocks, resistance, c, color);
    }
}
