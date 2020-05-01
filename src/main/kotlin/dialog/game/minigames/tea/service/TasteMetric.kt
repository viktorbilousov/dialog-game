package dialog.game.minigames.tea.service

import dialog.game.minigames.tea.models.Taste
import dialog.game.minigames.tea.models.Tea
import kotlin.math.abs

class TasteMetric {
    companion object {
        fun calcWeight(t1: Taste, t2: Taste): Int {
            val arr1 = t1.toArray()
            val arr2 = t2.toArray()
            val diff = IntArray(arr1.size) { 0 }.toTypedArray()
            for (i in arr1.indices) {
                diff[i] = abs(arr1[i] - arr2[i])
            }
            return diff.sum()
        }

        fun calcWeight(t1: Tea, t2: Tea): Int {
            return calcWeight(t1.taste, t2.taste)
        }

        fun calcAverageWeight(t1: Taste, t2: Taste) : Double{
            return calcWeight(t1, t2).toDouble()/t1.toArray().size
        }

        fun calcAverageWeigh(t1: Tea, t2: Tea) : Double{
            return calcAverageWeight(t1.taste, t2.taste)
        }

    }
}