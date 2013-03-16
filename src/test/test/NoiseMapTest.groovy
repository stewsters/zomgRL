import com.stewsters.zomgrl.map.NoiseMap
import squidpony.squidgrid.util.Direction

class NoiseMapTest extends GroovyTestCase {

    //This is generally not how you want to test things, this has a small chance to fail even if its working.
    void testNoise() {

        int xSize = 100
        int ySize = 100

        NoiseMap noiseMap = new NoiseMap(xSize, ySize)

        noiseMap.makeNoise(5 as int, 50 as int, 1000)

        20.times {
            noiseMap.spread()
            noiseMap.fade()
        }
        noiseMap.print()
        noiseMap.regenerateDirection()
        noiseMap.printDir()

        assert Direction.DOWN_RIGHT == noiseMap.getNoiseDir(1, 1)
    }

}