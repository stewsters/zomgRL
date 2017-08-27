package com.stewsters.test

import com.stewsters.util.math.MatUtils

class MathUtilsTest extends GroovyTestCase {

    //This is generally not how you want to test things, this has a small chance to fail even if its working.
    void testRandomNumbers() {
        def numbers = []

        100.times {
            numbers.add MatUtils.getIntInRange(1, 3)
        }

        assert 3 == numbers.max()
        assert 1 == numbers.min()

        assert MatUtils.getIntInRange(1, 1) == 1

        println "Finished without asserting"
    }

}