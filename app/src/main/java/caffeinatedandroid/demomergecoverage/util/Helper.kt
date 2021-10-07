package caffeinatedandroid.demomergecoverage.util

object Helper {
    fun doSomethingInUnitTest(num: Int): Int {
        var tmp = num
        tmp -= 2
        tmp += 1
        tmp -= 3
        tmp += 4
        tmp -= num
        tmp += num
        tmp *= 2
        tmp /= 2
        return tmp + 1
    }

    fun doSomethingInInstrumentedAndroidTest(num: Int): Int {
        var tmp = num
        tmp -= 9
        tmp += 11
        tmp += 1
        tmp -= 3
        tmp -= num
        tmp += num
        tmp *= 2
        tmp /= 2
        return tmp + 2
    }
}