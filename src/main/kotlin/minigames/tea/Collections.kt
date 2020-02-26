import models.Flower
import models.Tea

class Collections {
    companion object{
        fun getFlowers() : Array<Flower>{
           return arrayOf(
               Flower("земляника", 3 ,-2, 1, 5, 0 ),
               Flower("смородина", 2, 1, 1, 3, 0),
               Flower("мята", 2, 5 , 3, -3, 2 ),
               Flower("иван-чай", -3, 5, -1, 2, 5),
               Flower("Ель", 1, -2, 5, 5, 0),
               Flower("ромашка", 1, 1, 0, 5, -2),
               Flower("одуванчик", -3, 0 ,-2,6, 2)
            )
        }

        fun getTeas() : Array<Tea>{
            return arrayOf(
                Tea("вкусный", 10 ,4, 7, 2, 8 ),
                Tea("ароматный", 6,6,1,4,5),
                Tea("обычный", 5,7,5,4,10),
                Tea("ягодный", 8,3,8,10,6),
                Tea("освежающий", 7,10,6,3,5)
            )
        }
    }
}