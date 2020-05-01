package filters

import org.junit.jupiter.api.Test
import dialog.game.phrases.filters.inline.text.GetBooleanFilter
import dialog.game.phrases.filters.inline.text.IfElseFilterV2
import org.junit.jupiter.api.Assertions

class IfElseFilterV2_Test {
    @Test
    fun parametricIfElsePhrase_phrase(){
        val testParams = hashMapOf<String, Any?>()


        val filter = createFilter(
            IfElseFilterV2(),
            GetBooleanFilter(testParams)
        )

        val input =
            arrayOf(
                "[IF_SYS]",
                "[GET=if] if",
                "[ELSEIF_SYS]",
                "[GET=elseif1] elseif1",
                "[ELSEIF_SYS]",
                "[GET=elseif2] elseif2",
                "[ELSE_SYS]",
                "[GET=else] else",
                "[FI_SYS]"
            )

        val expectedIf = "[GET=if] if"
        val expectedElseIf1 = "[GET=elseif1] elseif1"
        val expectedElseIf2 = "[GET=elseif2] elseif2"
        val expectedElse = "[GET=else] else"


        testParams["if"] = true;
        testParams["elseif1"] = false;
        testParams["elseif2"] = false;
        testParams["else"] = false;

        val answerif = filter(input);

        testParams["if"] = false;
        testParams["elseif1"] = true;
        testParams["elseif2"] = false;
        testParams["else"] = false;

        val answerElseIf1 = filter(input);

        testParams["if"] = false;
        testParams["elseif1"] = false;
        testParams["elseif2"] = true;
        testParams["else"] = false;

        val answerElseIf2 = filter(input);

        testParams["if"] = false;
        testParams["elseif1"] = false;
        testParams["elseif2"] = false;
        testParams["else"] = true;

        val answerElse = filter(input);

        assert(answerif.size == 1)
        assert(answerElse.size == 1)
        assert(answerElseIf1.size == 1)
        assert(answerElseIf2.size == 1)

        assert(answerif[0] == expectedIf)
        assert(answerElse[0] == expectedElse)
        assert(answerElseIf1[0] == expectedElseIf1)
        assert(answerElseIf2[0] == expectedElseIf2)

    }

    private fun createFilter(ifElseFilter: IfElseFilterV2, getBooleanFilter: GetBooleanFilter) : ((Array<String>) -> Array<String>) {
         return {
            ifElseFilter.filterPhrases(getBooleanFilter.filterPhrases(it, 0), 0)
        }
    }

    @Test
    fun parametricIfElsePhrase_multiplyPhrase() {

        val testParams = hashMapOf<String, Any?>()


        val filter = createFilter(
            IfElseFilterV2(),
            GetBooleanFilter(testParams)
        )

        val input =
            arrayOf(
                "[IF_SYS]",
                "[GET=if] if",
                "[ELSEIF_SYS]",
                "[GET=elseif1] elseif text 1",
                "[GET=elseif1] elseif text 2",
                "[GET=elseif1] elseif text 3",
                "[ELSEIF_SYS]",
                "[GET=elseif2] elseif2",
                "[ELSE_SYS]",
                "[GET=else] else",
                "[FI_SYS]"
            )

        val expectedElseIf1 = arrayOf(
            "[GET=elseif1] elseif text 1",
            "[GET=elseif1] elseif text 2",
            "[GET=elseif1] elseif text 3"
        )



        testParams["if"] = false;
        testParams["elseif1"] = true;
        testParams["elseif2"] = false;
        testParams["else"] = false;

        val answerElseif = filter(input);


        Assertions.assertArrayEquals(answerElseif, expectedElseIf1)

    }

}
