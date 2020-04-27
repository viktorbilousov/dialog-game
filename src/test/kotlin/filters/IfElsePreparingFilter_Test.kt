package filters

import models.Answer
import org.junit.jupiter.api.Test
import phrases.filters.phrase.IfElsePreparingFilter

class IfElsePreparingFilter_Test {
    @Test
    public fun ifElseTest(){

        val filter = IfElsePreparingFilter()

        val input = (
            arrayOf(
                Answer("before", "before"),
                Answer("IF", "[IF] IF TEXT"),
                Answer("ELSE", "[ELSE] ELSE TEXT"),
                Answer("after", "after")
            )
        )
        val expected =  arrayOf(
                "before",
                "[IF_SYS] this is system tag and must be removed",
                "IF TEXT",
                "[ELSE_SYS] this is system tag and must be removed",
                "ELSE TEXT",
                "[FI_SYS] this is system tag and must be removed",
                "after"
            )

        val result = filter.filterAnswers(input, 1).map { it.text };

        result.forEach{println(it)}

        assert(result.size == expected.size)

        expected.forEachIndexed{i , it -> assert(it == result[i])}

    }

    @Test
    public fun ifElseIfElseTest(){
        val filter = IfElsePreparingFilter()

        val input = (
                arrayOf(
                    Answer("before", "before"),
                    Answer("IF", "[IF] IF TEXT"),
                    Answer("ELSE_IF", "[ELSE IF] ELSE IF TEXT1"),
                    Answer("ELSE_IF", "[ELSE IF] ELSE IF TEXT2"),
                    Answer("ELSE_IF", "[ELSE IF] ELSE IF TEXT3"),
                    Answer("ELSE", "[ELSE] ELSE TEXT"),
                    Answer("after", "after")
                )
                )
        val expected =  arrayOf(
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

        result.forEach{println(it)}

        assert(result.size == expected.size)

        expected.forEachIndexed{i , it -> assert(it == result[i])}
    }

    @Test
    public fun ifElseIfElseIgnoredTest(){
        val filter = IfElsePreparingFilter()

        val input = (
                arrayOf(
                    Answer( "id","before"),
                    Answer( "id","[IF_SYS] this is system tag and must be removed"),
                    Answer( "id","IF TEXT"),
                    Answer( "id","[ELSEIF_SYS] this is system tag and must be removed"),
                    Answer( "id","ELSE IF TEXT1"),
                    Answer( "id","[ELSEIF_SYS] this is system tag and must be removed"),
                    Answer( "id","ELSE IF TEXT2"),
                    Answer( "id","[ELSEIF_SYS] this is system tag and must be removed"),
                    Answer( "id","ELSE IF TEXT3"),
                    Answer( "id","[ELSE_SYS] this is system tag and must be removed"),
                    Answer( "id","ELSE TEXT"),
                    Answer( "id","[FI_SYS] this is system tag and must be removed"),
                    Answer( "id","after")
                )
                )
        val expected =  arrayOf(
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

        result.forEach{println(it)}

        assert(result.size == expected.size)

        expected.forEachIndexed{i , it -> assert(it == result[i])}
    }


    @Test
    public fun ifElseIfElseWithoitBracks(){
        val filter = IfElsePreparingFilter()

        val input = (
                arrayOf(
                    Answer( "id","before"),
                    Answer( "id","[IF]"),
                    Answer( "id","IF TEXT"),
                    Answer( "id","[ELSE IF]"),
                    Answer( "id","ELSE IF TEXT1"),
                    Answer( "id","[ELSE IF]"),
                    Answer( "id","ELSE IF TEXT2"),
                    Answer( "id","[ELSE IF]"),
                    Answer( "id","ELSE IF TEXT3"),
                    Answer( "id","[ELSE]"),
                    Answer( "id","ELSE TEXT"),
                    Answer( "id","[FI]"),
                    Answer( "id","after")
                )
                )
        val expected =  arrayOf(
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

        result.forEach{println(it)}

        assert(result.size == expected.size)

        expected.forEachIndexed{i , it -> assert(it == result[i])}
    }
}