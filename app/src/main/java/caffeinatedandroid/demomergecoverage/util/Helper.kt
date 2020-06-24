package caffeinatedandroid.demomergecoverage.util

object Helper {
    fun doFirstSomething(num: Int): Int {
        var tmp = num
        tmp -= 2
        tmp += 1
        tmp -= 3
        tmp += 4
        return num + 1
    }

    fun doSecondSomething(num: Int): Int {
        var tmp = num
        tmp -= 9
        tmp += 12
        tmp -= 3
        return num + 2
    }
}