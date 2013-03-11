package com.stewsters.zomgrl.map

import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.graphic.RenderConfig
import squidpony.squidcolor.SColor
import squidpony.squidcolor.SColorFactory
import squidpony.squidgrid.gui.swing.SwingPane

class LevelMap {

    public int xSize
    public int ySize

    public Tile[][] ground
    public ArrayList<Entity> objects



    public LevelMap(int x, int y) {
        xSize = x
        ySize = y

        ground = new Tile[x][y]
        objects = new ArrayList<Entity>()
    }

    public boolean isBlocked(int x, int y) {

        if (x < 0 || x >= xSize || y < 0 || y >= ySize) {
            return true
        }

        if (ground[x][y] && ground[x][y].isBlocked) {
            return true
        }

        for (Entity entity : objects) {
            if (entity.x == x && entity.y == y && entity.blocks)
                return true
        }

        return false
    }

    /**
     * Performs the Field of View process
     *
     * @param startx
     * @param starty
     */

    public void render(params) {

        SwingPane display = params.display
        int viewX = params.viewX ?: params.player.x
        int viewY = params.viewY ?: params.player.y
        Entity player = params.player

        //first we figure out where the real translation center is
        int worldLowX = viewX - RenderConfig.windowRadiusX //low is upper left corner
        int worldHighX = viewX + RenderConfig.windowRadiusX

        int worldLowY = viewY - RenderConfig.windowRadiusY
        int worldHighY = viewY + RenderConfig.windowRadiusY

        int xRange = worldHighX - worldLowX + 1 // this is the total size of the box
        int yRange = worldHighY - worldLowY + 1

        player.ai.calculateSight()

        //repaint the level with new light map -- Note that in normal use you'd limit this to just elements that changed
        for (int x = 0; x < xRange; x++) {
            for (int y = 0; y < yRange; y++) {
                int originalX = x + worldLowX
                int originalY = y + worldLowY

                int lightX = x + player.x - viewX
                int lightY = y + player.y - viewY

                if (originalX < 0 || originalX >= ground.length || originalY < 0 || originalY >= ground[0].length) {
                    display.clearCell(x, y); //off the map

                } else if (lightX > 0 && lightX < player.ai.light.length
                        && lightY > 0 && lightY < player.ai.light[0].length
                        && player.ai.light[lightX][lightY] > 0f) {

                    double radius = Math.sqrt((originalX - player.x) * (originalX - player.x) + (originalY - player.y) * (originalY - player.y));
                    float bright = 1 - player.ai.light[lightX][lightY];
                    SColor cellLight = SColorFactory.fromPallet("light", bright);

                    SColor objectLight = SColorFactory.blend(
                            ground[originalX][originalY].gore ? SColor.RED :
                                ground[originalX][originalY].color,
                            cellLight, getTint(radius));
                    display.placeCharacter(x, y, ground[originalX][originalY].representation, objectLight);
                    ground[originalX][originalY].isExplored = true

                } else if (ground[originalX][originalY].isExplored) {
                    display.placeCharacter(x, y, ground[originalX][originalY].representation, SColor.DARK_GRAY)

                } else {
                    display.clearCell(x, y);
                }
            }
        }
        xRange.times { x ->
            display.placeCharacter(x, yRange, '-' as char, SColor.DARK_GRAY)
        }
        yRange.times { y ->
            display.placeCharacter(xRange, y, '|' as char, SColor.DARK_GRAY)
        }
        display.placeCharacter(xRange, yRange, '+' as char, SColor.DARK_GRAY)

//
        objects.each { Entity entity ->

            int screenPositionX = entity.x - worldLowX
            int screenPositionY = entity.y - worldLowY

            if (screenPositionX >= 0 && screenPositionX < xRange && screenPositionY >= 0 && screenPositionY < yRange) {
                if (player.ai.light[screenPositionX][screenPositionY] > 0f) {
                    //put the player at the origin of the FOV
                    float bright = 1 - player.ai.light[screenPositionX][screenPositionY];
                    SColor cellLight = SColorFactory.fromPallet("light", bright);
                    SColor objectLight = SColorFactory.blend(entity.color, cellLight, getTint(0f));
                    display.placeCharacter(screenPositionX, screenPositionY, entity.ch, objectLight);
                } else {
                    display.clearCell(screenPositionX, screenPositionY);
                }
            }
        }

//        MessageLog.send(startx + ' : '+starty )

    }

    /**
     * Custom method to determine tint based on radius as well as general tint
     * factor.
     *
     * @param radius
     * @return
     */
    private float getTint(double radius) {
        return (float) (0f + RenderConfig.lightTintPercentage * radius);//adjust tint based on distance
    }

    /**
     * Performs the Line of Sight calculation and paints target square with a
     * green background if it can be reached or a red if not.
     *
     * @param startx
     * @param starty
     * @param endx
     * @param endy
     */
//    public void doLOS(SwingPane display, int startx, int starty, int endx, int endy) {
//
//        //run the LOS calculation
//        boolean visible = RenderConfig.los.isReachable(resistances, startx, starty, endx, endy);
//        Queue<Point> path = RenderConfig.los.getLastPath();
//
//        //draw out background for path followed
//        for (Point p : path) {
//            display.setCellBackground(p.x, p.y, SColor.BLUE_GREEN_DYE);
//        }
//
//        //mark the start location
//        display.setCellBackground(startx, starty, SColor.AMBER_DYE);
//
//        //mark end point
//        if (visible) {
//            display.setCellBackground(endx, endy, SColor.BRIGHT_GREEN);
//        } else {
//            display.setCellBackground(endx, endy, SColor.RED_PIGMENT);
//        }
//        display.refresh();
//    }

}
