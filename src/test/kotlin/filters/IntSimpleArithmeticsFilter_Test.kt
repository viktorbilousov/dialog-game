package filters

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import dialog.game.phrases.filters.inline.text.IntSimpleArithmeticsFilter
import java.lang.Exception

class IntSimpleArithmeticsFilter_Test {


    @Test
    fun plus(){
        val testParams = hashMapOf<String, Any?>()
        testParams["VALUE1"] = 0
        testParams["VALUE2"] = 20
        testParams["VALUE3"] = 30

        val filter = IntSimpleArithmeticsFilter(testParams);

        val inputPhrases = arrayOf(
            "[SETI][VALUE1+=0] value_0",
            "[SETI][VALUE2+=20] value_1",
            "[SETI][VALUE3+=-100] value_2"
        )

        filter.filterPhrases(inputPhrases, 0)

        Assertions.assertEquals(0 ,testParams["VALUE1"] )
        Assertions.assertEquals(40 ,testParams["VALUE2"] )
        Assertions.assertEquals(-70 ,testParams["VALUE3"] )
    }

    @Test
    fun minus(){
        val testParams = hashMapOf<String, Any?>()
        testParams["VALUE1"] = 0
        testParams["VALUE2"] = 100
        testParams["VALUE3"] = -100

        val filter = IntSimpleArithmeticsFilter(testParams);

        val inputPhrases = arrayOf(
            "[SETI][VALUE1-=0] value_0",
            "[SETI][VALUE2-=20] value_1",
            "[SETI][VALUE3-=-100] value_2"
        )

        filter.filterPhrases(inputPhrases, 0)

        Assertions.assertEquals(0 ,testParams["VALUE1"] )
        Assertions.assertEquals(80 ,testParams["VALUE2"] )
        Assertions.assertEquals(0,testParams["VALUE3"] )
    }

    @Test
    fun multiply(){
        val testParams = hashMapOf<String, Any?>()
        testParams["VALUE1"] = 1
        testParams["VALUE2"] = 2
        testParams["VALUE3"] = -100

        val filter = IntSimpleArithmeticsFilter(testParams);

        val inputPhrases = arrayOf(
            "[SETI][VALUE1*=0] value_0",
            "[SETI][VALUE2*=20] value_1",
            "[SETI][VALUE3*=-100] value_2"
        )

        filter.filterPhrases(inputPhrases, 0)

        Assertions.assertEquals(0 ,testParams["VALUE1"] )
        Assertions.assertEquals(40 ,testParams["VALUE2"] )
        Assertions.assertEquals(10000 ,testParams["VALUE3"] )
    }

    @Test
    fun division(){
        val testParams = hashMapOf<String, Any?>()
        testParams["VALUE1"] = 100
        testParams["VALUE2"] = 200
        testParams["VALUE3"] = -100

        val filter = IntSimpleArithmeticsFilter(testParams);

        val inputPhrases = arrayOf(
            "[SETI][VALUE1/=1] value_0",
            "[SETI][VALUE2/=20] value_1",
            "[SETI][VALUE3/=-100] value_2"
        )

        filter.filterPhrases(inputPhrases, 0)

        Assertions.assertEquals(100 ,testParams["VALUE1"] )
        Assertions.assertEquals(10 ,testParams["VALUE2"] )
        Assertions.assertEquals(1 ,testParams["VALUE3"] )
    }

    @Test
    fun divisionByNull(){
        val testParams = hashMapOf<String, Any?>()
        testParams["VALUE1"] = 100

        val filter = IntSimpleArithmeticsFilter(testParams);

        val inputPhrases = arrayOf(
            "[SETI][VALUE1/=0] value_0"
        )

        Assertions.assertThrows(Exception::class.java){ filter.filterPhrases(inputPhrases, 0)}

    }

    @Test
    fun increment(){
        val testParams = hashMapOf<String, Any?>()
        testParams["VALUE1"] = 1
        testParams["VALUE2"] = 0
        testParams["VALUE3"] = -100

        val filter = IntSimpleArithmeticsFilter(testParams);

        val inputPhrases = arrayOf(
            "[SETI][VALUE1++] value_0",
            "[SETI][VALUE2++] value_1",
            "[SETI][VALUE3++] value_2"
        )

        filter.filterPhrases(inputPhrases, 0)

        Assertions.assertEquals(2 ,testParams["VALUE1"] )
        Assertions.assertEquals(1 ,testParams["VALUE2"] )
        Assertions.assertEquals(-99 ,testParams["VALUE3"] )
    }

    @Test
    fun decrement(){
        val testParams = hashMapOf<String, Any?>()
        testParams["VALUE1"] = 1
        testParams["VALUE2"] = 0
        testParams["VALUE3"] = -100

        val filter = IntSimpleArithmeticsFilter(testParams);

        val inputPhrases = arrayOf(
            "[SETI][VALUE1--] value_0",
            "[SETI][VALUE2--] value_1",
            "[SETI][VALUE3--] value_2"
        )

        filter.filterPhrases(inputPhrases, 0)

        Assertions.assertEquals(0 ,testParams["VALUE1"] )
        Assertions.assertEquals(-1 ,testParams["VALUE2"] )
        Assertions.assertEquals(-101 ,testParams["VALUE3"] )
    }


}