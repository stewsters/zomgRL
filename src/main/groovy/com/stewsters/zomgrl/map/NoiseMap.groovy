package com.stewsters.zomgrl.map

import squidpony.squidgrid.util.Direction

public class NoiseMap {

    int shrinkageFactor = 10;
    Float[][] lowResMap
    Direction[][] direction

    int xSize
    int ySize

    public NoiseMap(int x, int y) {

        xSize = convertX(x) + 1
        ySize = convertY(y) + 1

        lowResMap = new float[xSize][ySize]
        direction = new Direction[xSize][ySize]
    }

    public void makeNoise(int x, int y, float noise) {
        lowResMap[convertX(x)][convertY(y)] += noise
    }

    public Direction getNoiseDir(int x, int y) {
        return direction[convertX(x)][convertY(y)]
    }

    /**
     * Fade the whole grid
     * @return
     */
    public void fade() {
        xSize.times { int x ->
            ySize.times { int y ->
                lowResMap[x][y] *= 0.99
            }
        }
    }

    /**
     * Spread the noise to adjacent squares
     * @return
     */
    public void spread() {

        Float[][] newLowResMap = new Float[xSize][ySize]

        xSize.times { x ->
            ySize.times { y ->

                float leftXVal = (x >= 1) ? lowResMap[x - 1][y] : lowResMap[x][y]
                float rightXVal = (x < xSize - 1) ? lowResMap[x + 1][y] : lowResMap[x][y]

                float leftYVal = (y >= 1) ? lowResMap[x][y - 1] : lowResMap[x][y]
                float rightYVal = (y < ySize - 1) ? lowResMap[x][y + 1] : lowResMap[x][y]

                newLowResMap[x][y] = (leftXVal + rightXVal + leftYVal + rightYVal + lowResMap[x][y]) / 5f
            }
        }

        lowResMap = newLowResMap
    }

    public void print() {
        xSize.times { x ->
            ySize.times { y ->
                print lowResMap[x][y]

            }
            println ''
        }
    }

    public void printDir() {
        xSize.times { x ->
            ySize.times { y ->
                print direction[x][y]
            }
            println ''
        }
    }

    public void regenerateDirection() {
        xSize.times { x ->
            ySize.times { y ->

                float leftXVal = (x >= 1) ? lowResMap[x - 1][y] : lowResMap[x][y]
                float rightXVal = (x < xSize - 1) ? lowResMap[x + 1][y] : lowResMap[x][y]

                float upYVal = (y >= 1) ? lowResMap[x][y - 1] : lowResMap[x][y]
                float downYVal = (y < ySize - 1) ? lowResMap[x][y + 1] : lowResMap[x][y]

                int xDiff = (rightXVal - leftXVal) as int
                int yDiff = (downYVal - upYVal) as int

                direction[x][y] = Direction.getDirection(xDiff, yDiff)
            }
        }
    }


    private int convertX(int x) {
        return (x / shrinkageFactor) as int
    }

    private int convertY(int y) {
        return (y / shrinkageFactor) as int
    }
}