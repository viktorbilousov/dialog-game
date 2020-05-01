package dialog.game.minigames.tea.service

import dialog.game.minigames.tea.models.Collection
import dialog.game.minigames.tea.models.Tea

class TeaQuality {

    companion object{
        private const val BAD_WEIGHT_WEIGHT_LIMIT = 3.0;
        private const val GOOD_WEIGHT_WEIGHT_LIMIT = 2.1
        private val comparedTea = Collection.getTeas()
        private val teaMetricCalc: (t1 : Tea, t2 : Tea) -> Double = TasteMetric.Companion::calcAverageWeigh


        public fun calcQuality(sourseTea: Tea, benchmarkTea: Tea) : Quality{
            val weight = teaMetricCalc(sourseTea, benchmarkTea)
            return getQuality(sourseTea, weight) ;
        }

        public fun nearestToCollection(tea: Tea) : Tea {

            return comparedTea
                .associate { Pair(it, teaMetricCalc(it, tea)) }
                .minBy { it.value }?.key!!
        }

       private fun getQuality(tea: Tea , weight: Double) : Quality{
           tea.taste.toArray().forEach { if(it < 0) return Quality.BAD }

           return when(weight){
                in BAD_WEIGHT_WEIGHT_LIMIT .. Int.MAX_VALUE.toDouble() -> Quality.BAD
                in GOOD_WEIGHT_WEIGHT_LIMIT..BAD_WEIGHT_WEIGHT_LIMIT -> Quality.MIDDLE
                else -> Quality.GOOD
            }
        }
    }

    enum class Quality{
        GOOD, BAD, MIDDLE
    }
}