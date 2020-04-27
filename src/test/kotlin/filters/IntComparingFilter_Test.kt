package filters

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import phrases.filters.inline.text.IntComparingFilter

class IntComparingFilter_Test {


    @Test
    fun ignoringUnrecoqnised(){
        val testParams = hashMapOf<String, Any?>()
        testParams["VALUE0"] = 0
        testParams["VALUE1"] = 1

        val filter = IntComparingFilter(testParams);

        val inputAnswers = arrayOf(
            "[INT][VALUE0=0] value_0",
            "[INT][VALUE1==1] value_1",
            "[VALUE0=100] value_100",
            "[VALUE2==111] value_111"
        )

        val expectedAnswer = arrayOf(
            "[INT][VALUE0=0] value_0",
            "[INT][VALUE1==1] value_1",
            "[VALUE0=100] value_100",
            "[VALUE2==111] value_111")
        val res  = filter.filterPhrases(inputAnswers, 0);
        Assertions.assertArrayEquals(expectedAnswer, res)
    }


    @Test
    fun operationEquals(){

        val testParams = hashMapOf<String, Any?>()
        testParams["VALUE0"] = 0
        testParams["VALUE1"] = 1

        val filter = IntComparingFilter(testParams);

        val inputAnswers = arrayOf(
            "[INT][VALUE0=0] value_0",
            "[INT][VALUE1==1] value_1",
            "[INT][VALUE0=100] value_100",
            "[INT][VALUE2==111] value_111"
        )

        val expectedAnswer = arrayOf("[INT][VALUE0=0] value_0", "[INT][VALUE1==1] value_1")
        val res  = filter.filterPhrases(inputAnswers, 0);
        Assertions.assertArrayEquals(expectedAnswer, res)

    }

    @Test
    fun operationNotEquals(){

        val testParams = hashMapOf<String, Any?>()
        testParams["VALUE0"] = 0
        testParams["VALUE1"] = 1

        val filter = IntComparingFilter(testParams);

        val inputAnswers = arrayOf(
            "[INT][VALUE0!=0] value_0",
            "[INT][VALUE1!=1] value_1",
            "[INT][VALUE0!=100] value_100",
            "[INT][VALUE2!=111] value_111"
        )

        val expectedAnswer = arrayOf("[INT][VALUE0!=100] value_100")
        val res  = filter.filterPhrases(inputAnswers, 0);
        Assertions.assertArrayEquals(expectedAnswer, res)

    }

    @Test
    fun operationLess() {


        val testParams = hashMapOf<String, Any?>()
        testParams["VALUE0"] = 0
        testParams["VALUE1"] = 1

        val filter = IntComparingFilter(testParams);

        val inputAnswers = arrayOf(
            "[INT][VALUE0<0] value_0",
            "[INT][VALUE1<1] value_1",
            "[INT][VALUE0<100] value_100",
            "[INT][VALUE2<111] value_111"
        )

        val expectedAnswer = arrayOf("[INT][VALUE0<100] value_100")
        val res = filter.filterPhrases(inputAnswers, 0);
        Assertions.assertArrayEquals(expectedAnswer, res)

    }

    @Test
    fun operationLessEquals(){

        val testParams = hashMapOf<String, Any?>()
        testParams["VALUE0"] = 0
        testParams["VALUE1"] = 1

        val filter = IntComparingFilter(testParams);

        val inputAnswers = arrayOf(
            "[INT][VALUE0<=0] value_0",
            "[INT][VALUE1<=1] value_1",
            "[INT][VALUE0<=100] value_100",
            "[INT][VALUE2<=111] value_111"
        )

        val expectedAnswer = arrayOf("[INT][VALUE0<=0] value_0",
            "[INT][VALUE1<=1] value_1",
            "[INT][VALUE0<=100] value_100")
        val res = filter.filterPhrases(inputAnswers, 0);
        Assertions.assertArrayEquals(expectedAnswer, res)

    }

    @Test
    fun operationMore(){

        val testParams = hashMapOf<String, Any?>()
        testParams["VALUE0"] = 1110
        testParams["VALUE1"] = 1

        val filter = IntComparingFilter(testParams);

        val inputAnswers = arrayOf(
            "[INT][VALUE0>0] value_0",
            "[INT][VALUE1>1] value_1",
            "[INT][VALUE0>100] value_100",
            "[INT][VALUE2>111] value_111"
        )

        val expectedAnswer = arrayOf("[INT][VALUE0>0] value_0", "[INT][VALUE0>100] value_100")
        val res = filter.filterPhrases(inputAnswers, 0);
        Assertions.assertArrayEquals(expectedAnswer, res)
    }


    @Test
    fun operationMoreEquals(){
        val testParams = hashMapOf<String, Any?>()
        testParams["VALUE0"] = 1110
        testParams["VALUE1"] = 1

        val filter = IntComparingFilter(testParams);

        val inputAnswers = arrayOf(
            "[INT][VALUE0>=0] value_0",
            "[INT][VALUE1>=1] value_1",
            "[INT][VALUE0>=100] value_100",
            "[INT][VALUE2>=111] value_111"
        )

        val expectedAnswer = arrayOf( "[INT][VALUE0>=0] value_0",
            "[INT][VALUE1>=1] value_1",
            "[INT][VALUE0>=100] value_100")
        val res = filter.filterPhrases(inputAnswers, 0);
        Assertions.assertArrayEquals(expectedAnswer, res)
    }

    @Test
    fun multiParams(){
        val testParams = hashMapOf<String, Any?>()
        testParams["VALUE0"] = 1110
        testParams["VALUE1"] = 1

        val filter = IntComparingFilter(testParams);

        val inputAnswers = arrayOf(
            "[INT][VALUE0>=0][INT][VALUE1>=1] value_0",
            "[INT][VALUE0>=100][INT][VALUE2>=111] value_100"
        )

        val expectedAnswer = arrayOf( "[INT][VALUE0>=0][INT][VALUE1>=1] value_0")
        val res = filter.filterPhrases(inputAnswers, 0);
        Assertions.assertArrayEquals(expectedAnswer, res)
    }

}