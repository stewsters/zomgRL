package com.stewsters.zomgrl.map

import com.stewsters.util.spatial.IntervalKDTree2d
import com.stewsters.zomgrl.entity.Entity
import com.stewsters.zomgrl.game.Game
import com.stewsters.zomgrl.graphic.RenderConfig
import squidpony.squidcolor.SColor
import squidpony.squidcolor.SColorFactory
import squidpony.squidgrid.gui.swing.SwingPane

class LevelMap {

    public int xSize
    public int ySize

    public Tile[][] ground
    public ArrayList<Entity> objects // need to cut down this visibility
    private IntervalKDTree2d<Entity> spatialHash
    private HashSet<Entity> entityTemp;

    public LevelMap(int x, int y) {
        xSize = x
        ySize = y

        ground = new Tile[x][y]
        objects = new ArrayList<Entity>()
        spatialHash = new IntervalKDTree2d<>(Math.max(x, y), 4)
        entityTemp = new HashSet<>();
    }

    public void add(Entity e) {
        objects.add(e)
        spatialHash.put(e.x - 0.25, e.y - 0.25, e.x + 0.25, e.y + 0.25, e)
    }

    public void remove(Entity e) {
        objects.remove(e)
        spatialHash.remove(e)
    }


    void update(Entity e) {
        spatialHash.remove(e)
        spatialHash.put(e.x - 0.25, e.y - 0.25, e.x + 0.25, e.y + 0.25, e)
    }

    public HashSet<Entity> getEntitiesAtLocation(int x, int y) {
        entityTemp.clear()
        return spatialHash.getValues(x - 0.5, y - 0.5, x + 0.5, y + 0.5, entityTemp)
    }

    public HashSet<Entity> getEntitiesBetween(int lowX, int lowY, int highX, int highY) {
        entityTemp.clear()
        return spatialHash.getValues(lowX - 0.5, lowY - 0.5, highX + 0.5, highY + 0.5, entityTemp)
    }

    public void makeNoise(int x, int y, int noise) {

        int noiseSquare = noise * noise

        //TODO: find all entities in range, notify them of the noise
        getEntitiesBetween(x - noise, y - noise, x + noise, y + noise).each {
            if (it.ai && ((it.x - x) * (it.x - x)) + ((it.y - y) * (it.y - y)) < noiseSquare) {
                it.ai.hearNoise(x, y)
            }
        }

    }

    public boolean isBlocked(int x, int y) {

        if (x < 0 || x >= xSize || y < 0 || y >= ySize) {
            return true
        }

        if (ground[x][y] && ground[x][y].isBlocked) {
            return true
        }

        for (Entity entity : getEntitiesAtLocation(x, y)) {
            if (entity.blocks)
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

        if (player.ai)
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

                } else if (player.ai && lightX > 0 && lightX < player.ai.light.length
                        && lightY > 0 && lightY < player.ai.light[0].length
                        && player.ai.light[lightX][lightY] > 0f) {

                    double radius = Math.sqrt((originalX - player.x) * (originalX - player.x) + (originalY - player.y) * (originalY - player.y));
                    float bright = 1 - player.ai.light[lightX][lightY];

                    SColor cellLight = Game.isDay() ? SColorFactory.fromPallet("light", bright) : SColorFactory.fromPallet("dark", bright);


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

        objects.each { Entity entity ->

            int screenPositionX = entity.x - worldLowX
            int screenPositionY = entity.y - worldLowY

            if (screenPositionX >= 0 && screenPositionX < xRange && screenPositionY >= 0 && screenPositionY < yRange) {
                if (player?.ai?.light[screenPositionX][screenPositionY] > 0f) {
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

    }

    /**
     * Custom method to determine tint based on radius as well as general tint
     * factor.
     *
     * @param radius
     * @return
     */
    private static float getTint(double radius) {
        return (float) (0f + RenderConfig.lightTintPercentage * radius);//adjust tint based on distance
    }


}
