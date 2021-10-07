package caffeinatedandroid.demomergecoverage

import caffeinatedandroid.demomergecoverage.util.Helper.doSomethingInUnitTest
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val result = 2 + 2
        assertEquals(4, result)
    }

    @Test
    fun firstHelperFunctionTest() {
        assertEquals(doSomethingInUnitTest(5), 6)
    }
}