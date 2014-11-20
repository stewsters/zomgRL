package com.stewsters.zomgrl.map.gen;


import com.stewsters.zomgrl.map.LevelMap;
import com.stewsters.zomgrl.map.Tile;
import squidpony.squidcolor.SColor;

/**
 * This is squid's default FOV test reimplemented as a MapGenerator
 */

public class StaticMapGenerator implements MapGenerator {

    public static final String[] DEFAULT_MAP = new String[]{//in order to be in line with GUI coordinate pairs, this appears to be sideways in this style constructor.
        "øøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøøø########################################øøøøøøøøøøøøøøøøøøøøøøøøø",
        "øøøøøøøøøøøøøøøøøø#########øøøøøøøø#,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,#..m.mmmmmmmmmmmmmm..m...ø",
        "øøøøøøøøøøø########.......##øøøøøøø#,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,#.mmTmmmmmmmmmmmmmm......ø",
        "øøøøø#######.......₤.......###øøøøø#¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸,TTT¸,,¸¸¸¸¸¸m.TmmmmmmmmmTmmmmmmm..m..ø",
        "øøø###₤₤₤₤₤..................#øøøøø#¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸¸,TTT,,¸¸¸¸¸¸mmmmm≈≈≈mm..mmmmmmmmm....ø",
        "øøø#₤₤₤₤₤.₤₤....₤............##øøøø#¸¸¸¸¸¸¸¸¸¸¸TTT¸¸¸¸¸¸¸¸¸¸¸¸¸,¸¸,,¸¸¸¸¸¸mmm≈≈≈≈≈mm.m.mmmmmmm.....ø",
        "øø##.₤₤₤₤₤₤₤₤.................####ø#¸¸¸¸¸¸¸TTTTTT¸¸¸¸¸¸¸¸¸¸¸¸¸¸c,¸,,¸¸¸¸¸¸mm≈≈m≈mmmmmmmmm≈≈≈m..m...ø",
        "øø#..₤₤₤₤₤₤₤.....................###¸¸¸TTTTTT¸¸¸¸¸¸¸¸¸¸¸¸¸¸ct¸ctc,,,¸¸¸¸¸¸m≈≈mmmmmmmTmmm≈≈≈≈≈≈≈≈...ø",
        "øø#...₤₤₤₤₤............₤............¸¸¸¸¸TTTTTT¸¸¸¸¸¸¸¸¸¸¸¸ctt¸c¸¸,,¸¸¸¸¸¸mm≈≈mmmmmTmmm≈≈≈m≈≈mmmm..ø",
        "øø#.₤₤₤₤₤₤₤...........₤₤............¸¸TTTTT¸¸TTT¸¸¸¸¸¸¸¸¸¸¸¸cc¸¸¸¸,,¸¸¸¸¸¸mmm≈mmmmmmmmm≈≈mTm≈≈mmm..ø",
        "ø##₤₤₤₤₤₤₤₤₤.......................###############################,,¸¸¸¸S¸#mmmm≈m≈≈≈mm≈≈≈≈mmmmm≈mmmø",
        "ø#₤₤₤₤₤₤₤₤₤₤..₤....................#.....#.....#.....#.....#.....#+/#¸¸¸S¸#.T.mm≈≈≈≈≈≈≈≈≈mmmTmmmmm.ø",
        "ø#₤₤₤₤..₤₤₤₤₤......................#.....#.....#.....#.....#.....#..#¸¸¸¸¸#.TT.mmm≈≈≈≈≈≈≈≈mmm.mmT.mø",
        "ø#.₤₤₤.₤₤₤₤₤₤......₤...............#.....#.....#.....#.....#.....#..#######.TTTTmmm≈≈≈≈≈≈mmT..mmm.mø",
        "ø#₤₤₤₤₤₤₤₤₤........₤...............#.....#.....#.....#.....#.....#/+#.....#..TTmmmm≈≈≈≈≈≈TTmTmmTmmmø",
        "ø#₤₤₤₤₤₤₤₤₤₤.......................#.....#.....#.....#.....#.....#..#.....#..T.mmmm≈≈≈≈≈≈≈mmTmTm≈≈≈ø",
        "ø#₤₤₤₤₤₤₤₤₤₤.......................#.....###+#####+#####/#####+###..+.....#...Tmmmmm≈≈≈≈≈≈≈≈mmmmT≈≈ø",
        "##₤₤₤₤₤₤₤₤₤₤.......................#######..........................#.....#mT..mmmmmm≈≈≈≈≈≈≈mmm≈≈≈mø",
        "#₤₤..₤₤₤₤₤₤₤₤......................#.....#..........................#.....#m..mmmmmmm≈≈≈≈≈≈≈Tm≈≈≈mmø",
        "#₤..₤₤₤₤₤₤₤₤₤......................#.....#..........................#######..mmmmmmmmm≈≈≈≈≈≈m≈≈mmmmø",
        "#..................................#.....#...####################...#...#E#..mmm.mmmmm≈≈≈≈≈≈≈≈mmmmmø",
        "#..................................#.....#...+..E#..............#.../.../.#.......mmmm≈≈≈≈≈mmmmmmmmø",
        "#..................................#.....#...#####..............#...#...#E#........mm≈≈≈≈≈mmmmmmmmmø",
        "#..................................#.....#...#..................#...#######...m.....m≈≈≈≈mmmmmmmmmmø",
        "#..................................#.....#...#..................#.........+......mmmm≈≈mmm....mm≈≈mø",
        "#..................................#.....#...#..................#........./...uu...um≈≈mu.....m≈≈≈mø",
        "#..................................#.....#...#..................#...#+###+#..uuuuuuuu≈≈uu.u.ummmmuuø",
        "#..................................#.....#...#.................##...#..#c.#uuuuuuuuuu≈≈uuuAuuuuuuuuø",
        "#..................................#.....#...#................#.#...#E.#t.#uuuuuAuuA≈≈≈≈≈uuuuuuuuuuø",
        "#..................................#.....#...#...............#..#...#E<#c.#uuAuAuuu≈≈≈≈≈≈≈AuAAuuAuuø",
        "#..................................#.....#...#.............##.../...#######uAuAAA≈≈≈≈≈≈≈≈≈≈AAAAAAAuø",
        "#..................................#.....#...#............#.....#...#.....#AAAuA≈≈≈≈≈≈≈≈≈≈≈AAAAAAAAø",
        "#..................................#.....#...#............#.....#...#.....#AAAA≈≈≈≈≈≈≈≈≈≈≈≈≈AAAAAAAø",
        "#..................................#.....#...####################...#.....#AAAAu≈≈≈≈≈≈≈≈≈≈≈≈≈≈AAAAAø",
        "#..................................#.....#.......EEEEEEEEEEE........#.....#AAAAuu.≈≈≈≈mmm≈≈≈≈≈AAuAAø",
        "#..................................#.....#..........................#.....#AAAuuuu≈≈≈≈≈mm≈≈≈≈AAuuAAø",
        "#..................................#.....#..........................#.....#AAAAuuuu≈≈≈≈≈≈≈≈≈AAuuuAAø",
        "#..................................#.....####+###+#####+#####+#####/#.....#AAAAAAAuu..≈≈≈≈≈AAAAuAAAø",
        "#..................................#.....#E.+.#.....#.....#.....#.........#AAAAAAAAA.AAA≈≈AAAAAAAAAø",
        "#..................................#.....####.#.....#.....#.....#tttt+#...#AAAAAAAA..AAAuuu.uAAAAAAø",
        "#..................................#.....#E.+.#.....#.....#.....#..c..#...#AAAAAAA....AAAAu..uAAAAAø",
        "#..................................#.....####.#.....#.....#.....###..E#...#AAAAAA...AAAAAuuu.uAAAAAø",
        "#..................................#.....#E.+.#.....#.....#.....#E+.EE#...#AAAAAAAAAAAAAAAAAuuAAAAAø",
        "###########################################################################AAAAAAAAAAAAAAAAAAAAAAAAø"
    };

    public LevelMap reGenerate() {

        int width = StaticMapGenerator.DEFAULT_MAP[0].length();
        int height = StaticMapGenerator.DEFAULT_MAP.length;
        LevelMap map = new LevelMap(width, height);

        for (int iX = 0; iX < map.xSize; iX++) {
            for (int iY = 0; iY < map.ySize; iY++) {
                map.ground[iX][iY] = new Tile(true, 1f, '#', SColor.WHITE);
            }
        }

        for (int x = 0; x < map.xSize; x++) {
            for (int y = 0; y < map.ySize; y++) {
                char c = DEFAULT_MAP[y].charAt(x);
                map.ground[x][y] = buildCell(c);
            }
        }
        return map;

    }


    /**
     * Builds a cell based on the character in the map.
     *
     * @param c
     * @return
     */
    private Tile buildCell(char c) {
        float resistance = 0f;//default is transparent
        SColor color;
        boolean blocks = false;
        switch (c) {
            case '.'://stone ground
                color = SColor.SLATE_GRAY;
                break;
            case '¸'://grass
                color = SColor.GREEN;
                break;
            case ','://pathway
                color = SColor.STOREROOM_BROWN;
                c = '.';
                break;
            case 'c':
                blocks = true;
                color = SColor.SEPIA;
                break;
            case '/':
                color = SColor.BROWNER;
                break;
            case '≈':
                color = SColor.AZUL;
                break;
            case '<':
            case '>':
                color = SColor.SLATE_GRAY;
                break;
            case 't':
                blocks = true;
                color = SColor.BROWNER;
                resistance = 0.3f;
                break;
            case 'm':
                color = SColor.BAIKO_BROWN;
                resistance = 0.1f;
                break;
            case 'u':
                color = SColor.TAN;
                resistance = 0.2f;
                break;
            case 'T':
            case '₤':
                blocks = true;
                color = SColor.FOREST_GREEN;
                resistance = 0.7f;
                break;
            case 'E': //ELF?
                blocks = true;
                color = SColor.SILVER;
                resistance = 0.8f;
                break;
            case 'S':
                color = SColor.BREWED_MUSTARD_BROWN;
                resistance = 0.9f;
                break;
            case '#': //wall
                blocks = true;
                color = SColor.SLATE_GRAY;
                resistance = 1f;
                break;
            case '+': //door?
                color = SColor.BROWNER;
                resistance = 1f;
                break;
            case 'A':
                color = SColor.ALICE_BLUE;
                resistance = 1f;
                break;
            case 'ø':
                c = ' ';
                color = SColor.BLACK;
                resistance = 1f;
                break;
            default://opaque items
                resistance = 1f;//unknown is opaque
                color = SColor.DEEP_PINK;
        }
        return new Tile(blocks, resistance, c, color);
    }

}
