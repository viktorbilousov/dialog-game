package filters

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import dialog.game.phrases.filters.inline.text.GetVariableFilter


/**
 * [GETV][key=value][GETV][key1=value2]
 * [NOTV][key=value][NOTV][key2=value2]
 */

class GetVariablesFilter_Test {
    @Test
    fun singleLabel() {

        val testParams = hashMapOf<String, Any?>()
        testParams["VALUE"] = 1

        val inputAnswers = arrayOf(
            "[GETV][VALUE=0] get false",
            "[GETV][VALUE=1]  get true",
            "[NOTV][VALUE=1] not true",
            "[NOTV][VALUE=0] not false"
        )

        val filter = GetVariableFilter(testParams)

        val expectedAnswer = arrayOf(
            "[GETV][VALUE=1]  get true",
            "[NOTV][VALUE=0] not false"
        )

        val res = filter.filterPhrases(inputAnswers,0)
        Assertions.assertArrayEquals(expectedAnswer, res)

    }


    @Test
    fun multiplyLabels() {

        val testParams = hashMapOf<String, Any?>()
        testParams["VALUE1"] = 1
        testParams["VALUE2"] = 2

        val inputAnswers = arrayOf(
            "[GETV][VALUE1=1][GETV][VALUE2=0] get false",
            "[GETV][VALUE1=1][GETV][VALUE2=2]  get true",
            "[NOTV][VALUE1=1][NOTV][VALUE2=1] not true",
            "[NOTV][VALUE1=2][NOTV][VALUE2=1] not false"
        )

        val filter = GetVariableFilter(testParams)

        val expectedAnswer = arrayOf(
            "[GETV][VALUE1=1][GETV][VALUE2=2]  get true",
            "[NOTV][VALUE1=2][NOTV][VALUE2=1] not false"
        )

        val res = filter.filterPhrases(inputAnswers,0)
        Assertions.assertArrayEquals(expectedAnswer, res)

    }
}