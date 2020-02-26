import models.MixedTea
import kotlin.math.pow
import kotlin.math.roundToInt

class Main {
    companion object{
        @JvmStatic
        fun main(args: Array<String>) {
            var positive = 0;
            var mitte = 0;
            var negative = 0;
            var negativeTaste = arrayOf(0,0,0,0,0);
             Bruteforce.find(5).forEach { entry ->
                 // if(Collections.getTeas().contains(it.key.toTea()))
                 if (entry.key.taste.toArray().filter { it > 0 }.size == 5) {
                     println(entry)
                     positive += entry.value;
                 }

                 if (entry.key.taste.toArray().filter { it > 0 }.size in 1..4) {
                     mitte += entry.value;
                 }

                 if (entry.key.taste.toArray().filter { it > 0 }.isEmpty()) {
                     negative += entry.value;
                 }

                 entry.key.taste.toArray().forEachIndexed{index: Int, i: Int -> if(i<0) negativeTaste[index]++; }

             }
        println("positiv = $positive ${(positive/(7.0).pow(5) * 100).roundToInt()}%")
        println("mitte = $mitte ${(mitte/(7.0).pow(5) * 100).roundToInt()}%")
        println("negativ = $negative ${(negative/(7.0).pow(5) * 100).roundToInt()}%")
        println("negativ array = ${negativeTaste.contentToString()}")
        }

    }
}