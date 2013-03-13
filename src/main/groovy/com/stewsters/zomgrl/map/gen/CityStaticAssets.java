package com.stewsters.zomgrl.map.gen;

import com.stewsters.util.Rect;
import com.stewsters.zomgrl.map.LevelMap;
import com.stewsters.zomgrl.map.Tile;
import squidpony.squidcolor.SColor;

public class CityStaticAssets {



    // 10*10 fountain
    private static final String[] fountain = new String[]{
            "..........",
            "..........",
            "..XXXXXX..",
            "..X~~~~X..",
            "..X~XX~X..",
            "..X~XX~X..",
            "..X~~~~X..",
            "..XXXXXX..",
            "..........",
            ".........."
    };


    public boolean populate(LevelMap map, Rect rect)
    {
        int rectWidth = rect.getX2() - rect.getX1();
        int rectHeight = rect.getY2() - rect.getY1();

        if( rectWidth > 10 && rectHeight >10){
            //for x
            //for y
//            char c = fountain[y].charAt(x);
//            map.ground[x][y] = buildCell(c);
        }

        return false;
    }

    private Tile buildCell(char c) {
        float resistance = 0f;//default is transparent
        SColor color;
        boolean blocks = false;
        switch (c) {
            case '.'://stone ground
                color = SColor.SLATE_GRAY;
                break;
            case '`'://parking lane
                color = SColor.SLATE_GRAY;
                c = '.';
                break;
            case ',':
                color=SColor.GRAY;
                break;
            case '+'://door
                color = SColor.NEW_BRIDGE;
                resistance = 1f;
                break;
            case '₤':
                blocks = true;
                color = SColor.FOREST_GREEN;
                resistance = 0.7f;
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
                c='.';
                resistance = 0f;//unknown is opaque
                color = SColor.GREEN;
        }
        return new Tile(blocks, resistance, c, color);
    }
}
