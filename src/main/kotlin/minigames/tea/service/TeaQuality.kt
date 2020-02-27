package minigames.tea.service

import minigames.tea.models.Collection
import minigames.tea.models.Tea
import minigames.tea.service.TasteMetric.Companion.calcAverageWeight
import kotlin.reflect.full.memberFunctions

class TeaQuality {

    companion object{
        private const val BAD_WEIGHT_WEIGHT_LIMIT = 4.0;
        private const val GOOD_WEIGHT_WEIGHT_LIMIT = 1.9
        private val comparedTea = Collection.getTeas()
        private val teaMetricCalc: (t1 : Tea, t2 : Tea) -> Double = TasteMetric.Companion::calcAverageWeigh

        public fun nearestToCollection(tea: Tea) : TeaQualityRecord{

            val comparedTeaEntry = comparedTea
                .associate { Pair(it, teaMetricCalc(it, tea)) }
                .minBy { it.value }!!

            return TeaQualityRecord(tea, comparedTeaEntry.key, comparedTeaEntry.value, getQuality(comparedTeaEntry.value)) ;
        }

       public fun getQuality(weight: Double) : Quality{
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