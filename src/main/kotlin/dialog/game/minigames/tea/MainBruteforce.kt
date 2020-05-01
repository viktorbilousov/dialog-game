import dialog.game.minigames.tea.service.TeaQuality
import dialog.game.minigames.tea.tables.TeaTable
import kotlin.math.pow
import kotlin.math.roundToInt

class MainBruteforce {
    companion object{
        @JvmStatic
        fun main(args: Array<String>) {
            var positive = 0;
            var mitte = 0;
            var negative = 0;
            var negativeTaste = arrayOf(0, 0, 0, 0, 0);

            var goodCnt = 0
            var middleCnt = 0
            var badCnt = 0
            val teaTable = TeaTable()
            Bruteforce.find(5).forEach { entry ->
                // if(Collections.getTeas().contains(it.key.toTea()))
                if (entry.key.taste.toArray().filter { it > 0 }.size == 5) {
                    val quality = TeaQuality.calcQuality(entry.key, TeaQuality.nearestToCollection(entry.key));
                   /* println(quality.sourceTea)
                    println(quality.nearestTea)
                    println(quality.weight)
                    println(quality.quality)*/
                    println()
                    when (quality) {
                        TeaQuality.Quality.GOOD -> goodCnt++
                        TeaQuality.Quality.BAD -> badCnt++
                        else -> middleCnt++;
                    }
                    positive += entry.value;
                }

                if (entry.key.taste.toArray().filter { it > 0 }.size in 1..4) {
                    mitte += entry.value;
                }

                if (entry.key.taste.toArray().filter { it > 0 }.isEmpty()) {
                    negative += entry.value;
                }

                entry.key.taste.toArray().forEachIndexed { index: Int, i: Int -> if (i < 0) negativeTaste[index]++; }

            }



            println("positiv = $positive ${(positive / (7.0).pow(5) * 100).roundToInt()}%")
            println("mitte = $mitte ${(mitte / (7.0).pow(5) * 100).roundToInt()}%")
            println("negativ = $negative ${(negative / (7.0).pow(5) * 100).roundToInt()}%")
            println("negativ array = ${negativeTaste.contentToString()}")
            println("GOOD = $goodCnt")
            println("MIDDLE = $middleCnt")
            println("BAD = $badCnt")
        }

    }
}