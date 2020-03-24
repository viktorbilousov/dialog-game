package minigames.tea.models

import minigames.tea.models.Flower
import minigames.tea.models.Tea

class Collection {
    companion object{
        public fun getFlowers() : Array<Flower>{
           return arrayOf(
               Flower("земляника", 3 ,-1, 1, 1, 1 ),
               Flower("смородина", 3, -1, 1, 1, 1),
               Flower("мята", 2, 3 , 3, -1, 2 ),
               Flower("иван-чай", -2, 1, -1, 2, 3),
               Flower("eль", 1, -2, 3, -1, -1),
               Flower("ромашка", 1, 1, 0, 2, -2),
               Flower("одуванчик", -1, 0 ,-1,2, 2)
            )
        }

        public fun getTeas() : Array<Tea>{
            return arrayOf(
                Tea("вкусный", 8 ,4, 7, 2, 7 ),
                Tea("ароматный", 6,6,1,4,5),
                Tea("обычный", 5,2,5,4,8),
                Tea("ягодный", 8,3,6,8,6),
                Tea("освежающий", 7,8,6,3,5)
            )
        }
    }
}