import minigames.tea.models.Collection
import minigames.tea.models.Flower
import minigames.tea.models.MixedTea

class Bruteforce {
    companion object{
        fun find(pinches: Int) : HashMap<MixedTea, Int>{
            val flowers = Collection.getFlowers()
            val teas = Collection.getTeas()
            val teaCounter = HashMap<MixedTea, Int>();
            var cnt = 0;
            val mutations = IntArray(pinches){0}.toTypedArray()
           do {
               cnt++;
               // println("$cnt ${mutations.contentToString()}")
               val newTea = createTea(mutations, flowers)
               if (teaCounter.containsKey(newTea)) teaCounter[newTea] = teaCounter[newTea]!! + 1
               else teaCounter[newTea] = 1
           }while (plusOne(mutations, flowers.lastIndex))
            println(cnt)
            return teaCounter;
        }

        private fun plusOne(mutation: Array<Int>, MaxIndex: Int) : Boolean{
           return plusOneRec(mutation,MaxIndex, 0)
        }

        private fun plusOneRec(mutation: Array<Int>, maxNum: Int, index: Int) : Boolean{
            if(mutation[index] == maxNum){
                mutation[index] = 0;
                if(index == mutation.lastIndex) return false
                return plusOneRec(mutation, maxNum, index+1)
            }
            else {
                mutation[index]++
            }
            return true
        }


        private fun createTea(mutation: Array<Int>, flowers: Array<Flower>) : MixedTea {
            val mixedTea = MixedTea();
            mutation.forEach { mixedTea.addFlower(flowers[it]) }
            return mixedTea;
        }
    }
}