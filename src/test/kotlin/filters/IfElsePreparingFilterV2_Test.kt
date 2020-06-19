package filters

import org.junit.jupiter.api.Test
import dialog.system.models.answer.Answer
import dialog.game.phrases.filters.phrase.IfElsePreparingFilterV2
import org.junit.jupiter.api.Assertions

class IfElsePreparingFilterV2_Test {
    @Test
    public fun ifElseTest() {

        val filter = IfElsePreparingFilterV2()

        val input = (
                arrayOf(
                    Answer("before", "before"),
                    Answer("IF", "[IF][condition] IF TEXT"),
                    Answer("ELSE", "[ELSE] ELSE TEXT"),
                    Answer("after", "after")
                )
                )
        val expected = arrayOf(
            "before",
            "[IF_SYS] this is system tag and must be removed",
            "[condition] IF TEXT",
            "[ELSE_SYS] this is system tag and must be removed",
            "ELSE TEXT",
            "[FI_SYS] this is system tag and must be removed",
            "after"
        )

        val result = filter.filterAnswers(input, 1).map { it.text }.toTypedArray();

        result.forEach { println(it) }
        result.forEachIndexed { i, it -> assert(it == expected[i]) }

    }

    @Test
    public fun ifElseIfElseTest() {
        val filter = IfElsePreparingFilterV2()

        val input = (
                arrayOf(
                    Answer("before", "before"),
                    Answer("IF", "[IF] [condition] IF TEXT"),
                    Answer("ELSE_IF", "[ELSE IF] [condition] ELSE IF TEXT1"),
                    Answer("ELSE_IF", "[ELSE IF] [condition] ELSE IF TEXT2"),
                    Answer("ELSE_IF", "[ELSE IF] [condition] ELSE IF TEXT3"),
                    Answer("ELSE", "[ELSE] ELSE TEXT"),
                    Answer("after", "after")
                )
                )
        val expected = arrayOf(
            "before",
            "[IF_SYS] this is system tag and must be removed",
            "[condition] IF TEXT",
            "[ELSEIF_SYS] this is system tag and must be removed",
            "[condition] ELSE IF TEXT1",
            "[ELSEIF_SYS] this is system tag and must be removed",
            "[condition] ELSE IF TEXT2",
            "[ELSEIF_SYS] this is system tag and must be removed",
            "[condition] ELSE IF TEXT3",
            "[ELSE_SYS] this is system tag and must be removed",
            "ELSE TEXT",
            "[FI_SYS] this is system tag and must be removed",
            "after"
        )

        val result = filter.filterAnswers(input, 1).map { it.text }.toTypedArray();

        result.forEach { println(it) }

        result.forEachIndexed { i, it -> assert(it == expected[i]) }

        // Assertions.assertEquals(result, expected)
    }

    @Test
    public fun ifElseIfElseIgnoredTest() {
        val filter = IfElsePreparingFilterV2()

        val input = (
                arrayOf(
                    Answer("id", "before"),
                    Answer("id", "[IF_SYS] this is system tag and must be removed"),
                    Answer("id", "IF TEXT"),
                    Answer("id", "[ELSEIF_SYS] this is system tag and must be removed"),
                    Answer("id", "ELSE IF TEXT1"),
                    Answer("id", "[ELSEIF_SYS] this is system tag and must be removed"),
                    Answer("id", "ELSE IF TEXT2"),
                    Answer("id", "[ELSEIF_SYS] this is system tag and must be removed"),
                    Answer("id", "ELSE IF TEXT3"),
                    Answer("id", "[ELSE_SYS] this is system tag and must be removed"),
                    Answer("id", "ELSE TEXT"),
                    Answer("id", "[FI_SYS] this is system tag and must be removed"),
                    Answer("id", "after")
                )
                )
        val expected = arrayOf(
            "before",
            "[IF_SYS] this is system tag and must be removed",
            "IF TEXT",
            "[ELSEIF_SYS] this is system tag and must be removed",
            "ELSE IF TEXT1",
            "[ELSEIF_SYS] this is system tag and must be removed",
            "ELSE IF TEXT2",
            "[ELSEIF_SYS] this is system tag and must be removed",
            "ELSE IF TEXT3",
            "[ELSE_SYS] this is system tag and must be removed",
            "ELSE TEXT",
            "[FI_SYS] this is system tag and must be removed",
            "after"
        )

        val result = filter.filterAnswers(input, 1).map { it.text };

        result.forEach { println(it) }

        assert(result.size == expected.size)

        expected.forEachIndexed { i, it -> assert(it == result[i]) }
    }


    @Test
    public fun ifElseIfElseWithoitBracks() {
        val filter = IfElsePreparingFilterV2()

        val input = (
                arrayOf(
                    Answer("id", "before"),
                    Answer("id", "[IF]"),
                    Answer("id", "IF TEXT"),
                    Answer("id", "[ELSE IF]"),
                    Answer("id", "ELSE IF TEXT1"),
                    Answer("id", "[ELSE IF]"),
                    Answer("id", "ELSE IF TEXT2"),
                    Answer("id", "[ELSE IF]"),
                    Answer("id", "ELSE IF TEXT3"),
                    Answer("id", "[ELSE]"),
                    Answer("id", "ELSE TEXT"),
                    Answer("id", "[FI]"),
                    Answer("id", "after")
                )
                )
        val expected = arrayOf(
            "before",
            "[IF_SYS] this is system tag and must be removed",
            "IF TEXT",
            "[ELSEIF_SYS] this is system tag and must be removed",
            "ELSE IF TEXT1",
            "[ELSEIF_SYS] this is system tag and must be removed",
            "ELSE IF TEXT2",
            "[ELSEIF_SYS] this is system tag and must be removed",
            "ELSE IF TEXT3",
            "[ELSE_SYS] this is system tag and must be removed",
            "ELSE TEXT",
            "[FI_SYS] this is system tag and must be removed",
            "after"
        )

        val result = filter.filterAnswers(input, 1).map { it.text };

        result.forEach { println(it) }

        assert(result.size == expected.size)

        expected.forEachIndexed { i, it -> assert(it == result[i]) }
    }

    @Test
    public fun ifElseIfElseWithConditionAndWithoutBracks() {
        val filter = IfElsePreparingFilterV2()

        val input = (
                arrayOf(
                    Answer("id", "before"),
                    Answer("id", "[IF][condition 1]"),
                    Answer("id", "[condition 2] IF TEXT"),
                    Answer("id", "[condition 3] IF TEXT"),
                    Answer("id", "[ELSE IF] [condition 4]"),
                    Answer("id", "ELSE IF TEXT1"),
                    Answer("id", "[ELSE IF]"),
                    Answer("id", "ELSE IF TEXT2"),
                    Answer("id", "[ELSE IF]"),
                    Answer("id", "ELSE IF TEXT3"),
                    Answer("id", "[ELSE] "),
                    Answer("id", "[condition 6] ELSE TEXT"),
                    Answer("id", "[condition 5] ELSE TEXT"),
                    Answer("id", "[FI]"),
                    Answer("id", "after")
                )
                )
        val expected = arrayOf(
            "before",
            "[IF_SYS] this is system tag and must be removed",
            "[condition 1][condition 2] IF TEXT",
            "[condition 1][condition 3] IF TEXT",
            "[ELSEIF_SYS] this is system tag and must be removed",
            "[condition 4]ELSE IF TEXT1",
            "[ELSEIF_SYS] this is system tag and must be removed",
            "ELSE IF TEXT2",
            "[ELSEIF_SYS] this is system tag and must be removed",
            "ELSE IF TEXT3",
            "[ELSE_SYS] this is system tag and must be removed",
            "[condition 6] ELSE TEXT",
            "[condition 5] ELSE TEXT",
            "[FI_SYS] this is system tag and must be removed",
            "after"
        )

        val result = filter.filterAnswers(input, 1).map { it.text };

        result.forEach { println(it) }

        Assertions.assertEquals(result.size, expected.size)

        expected.forEachIndexed { i, it -> Assertions.assertEquals(it, result[i]) }
    }

    @Test
    public fun ifElseIfElseMultiply() {
        val filter = IfElsePreparingFilterV2()

        val input = (
                arrayOf(
                    Answer("id", "before"),
                    Answer("id", "[IF] [condition1] IF TEXT"),
                    Answer("id", "[ELSE IF] [condition2][condition23][condition22]"),
                    Answer("id", "ELSE IF TEXT1"),
                    Answer("id", "ELSE IF TEXT12"),
                    Answer("id", "ELSE IF TEXT13"),
                    Answer("id", "ELSE IF TEXT14"),
                    Answer("id", "[ELSE IF]"),
                    Answer("id", "[condition3][condition4] ELSE IF TEXT21"),
                    Answer("id", "ELSE IF TEXT22"),
                    Answer("id", "[condition31][condition41]ELSE IF TEXT23"),
                    Answer("id", "[ELSE IF] [condition5] "),
                    Answer("id", "ELSE IF TEXT3"),
                    Answer("id", "ELSE IF TEXT4"),
                    Answer("id", "[ELSE]"),
                    Answer("id", "ELSE TEXT4"),
                    Answer("id", "ELSE TEXT3"),
                    Answer("id", "[FI]"),
                    Answer("id", "after")
                )
                )
        val expected = arrayOf(
            "before",
            "[IF_SYS] this is system tag and must be removed",
            "[condition1] IF TEXT",
            "[ELSEIF_SYS] this is system tag and must be removed",
            "[condition2][condition23][condition22]ELSE IF TEXT1",
            "[condition2][condition23][condition22]ELSE IF TEXT12",
            "[condition2][condition23][condition22]ELSE IF TEXT13",
            "[condition2][condition23][condition22]ELSE IF TEXT14",
            "[ELSEIF_SYS] this is system tag and must be removed",
            "[condition3][condition4] ELSE IF TEXT21",
            "ELSE IF TEXT22",
            "[condition31][condition41]ELSE IF TEXT23",
            "[ELSEIF_SYS] this is system tag and must be removed",
            "[condition5]ELSE IF TEXT3",
            "[condition5]ELSE IF TEXT4",
            "[ELSE_SYS] this is system tag and must be removed",
            "ELSE TEXT4",
            "ELSE TEXT3",
            "[FI_SYS] this is system tag and must be removed",
            "after"
        )

        val result = filter.filterAnswers(input, 1).map { it.text };

        result.forEach { println(it) }

        Assertions.assertEquals(result.size, expected.size)

        expected.forEachIndexed { i, it -> Assertions.assertEquals(it, result[i]) }
    }

    @Test
    fun addFI_ELSE_separately_from_text() {

        val filter = IfElsePreparingFilterV2()

        val input = (
                arrayOf(
                    Answer("before", "before"),
                    Answer("IF", "[IF][condition] IF TEXT"),
                    Answer("ELSE", "[ELSE]"),
                    Answer("ELSEText", "ELSE TEXT"),
                    Answer("after", "after")
                )
                )

        val expected = arrayOf(
            "before",
            "[IF_SYS] this is system tag and must be removed",
            "[condition] IF TEXT",
            "[ELSE_SYS] this is system tag and must be removed",
            "ELSE TEXT",
            "[FI_SYS] this is system tag and must be removed",
            "after"
        )

        val result = filter.filterAnswers(input, 1).map { it.text }.toTypedArray();

        expected.forEachIndexed { i, it -> Assertions.assertEquals(it, result[i]) }
    }

}