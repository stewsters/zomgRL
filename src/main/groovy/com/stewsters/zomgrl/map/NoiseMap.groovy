package com.stewsters.zomgrl.map

import squidpony.squidgrid.util.Direction

class NoiseMap {

    int shrinkageFactor = 10;
    int[][] lowResMap
    Direction[][] direction

    NoiseMap(int x, int y){
        lowResMap = new int[convertX(x)+1][convertY(y)+1]
        direction = new Direction[convertX(x)+1][convertY(y)+1]
    }

    public void makeNoise(int x, int y, int noise){
        lowResMap[convertX(x)][convertY(y)] += noise
    }

    public Direction getNoiseDir(int x, int y){
        return direction[convertX(x)][convertY(y)]
    }

    /**
     * Fade the whole grid
     * @return
     */
    public void fade(){
        (0..lowResMap.length){x->
            (0..lowResMap[0].length){y->
                lowResMap[x][y] = Math.max(0, lowResMap[x][y]-1)
            }
        }
    }

    /**
     * Spread the noise to adjacent squares
     * @return
     */
    public void spread(){
        (0..lowResMap.length){x->
            (0..lowResMap[0].length){y->
                lowResMap[x][y] = Math.max(0, lowResMap[x][y]-1)
            }
        }
    }

    public void regenerateDirection(){
        (0..lowResMap.length){x->
            (0..lowResMap[0].length){y->

                int xDiff = lowResMap[x+1] - lowResMap[x-1]
                int yDiff = lowResMap[y+1] - lowResMap[y-1]

                direction[x][y] = Direction.getDirection(xDiff,yDiff)
            }
        }
    }


    private int convertX(int x)
    {
        return  x/shrinkageFactor
    }

    private int convertY(int y)
    {
        return  y/shrinkageFactor
    }
}
