package phrases

import dialog.system.models.Answer
import dialog.system.models.items.phrase.FilteredPhrase
import org.junit.jupiter.api.Test
import dialog.game.phrases.collections.AnswerChooserCollection
import dialog.game.phrases.configurator.FilteredPhraseConfigurator
import tools.TestPhraseTools.Companion.createTestPhrase
import tools.TestPhraseTools
import tools.TestPhraseWrapper

class GamePhrase_IfElse_Test {
    @Test
    fun test_GamePhrase_phrase(){
        val testParams = hashMapOf<String, Any?>()
        testParams["if"] = true;
        testParams["elseif"] = false;
        testParams["else"] = false;

        val phrase =  createTestPhrase<FilteredPhrase>(
            arrayOf(
                "[IF] [GET=if] if_answ" ,
                "[ELSE IF] [GET=elseif] elseif_answ",
                "[ELSE] [GET=else] else_answ"
            ),
            arrayOf(
                Answer("1", "answ")
            )
        )
        FilteredPhraseConfigurator(phrase, testParams).autoFilter(gameVariables = testParams)
        phrase.answerChooser = AnswerChooserCollection.first();
        val printer = TestPhraseTools.setTestPrinter(phrase)

        phrase.run()
        assert(printer.lastPhrase == "if_answ")

        testParams["if"] = false;
        testParams["elseif"] = true;

        phrase.run()
        assert(printer.lastPhrase == "elseif_answ")

        testParams["elseif"] = false;
        testParams["else"] = true;

        phrase.run()
        assert(printer.lastPhrase == "else_answ")
    }


    @Test
    fun test_GamePhrase_answer_multiply(){
        val testParams = hashMapOf<String, Any?>()
        testParams["test1"] = true;
        testParams["test2"] = false;
        testParams["test3"] = true;

        val phrase =  createTestPhrase<FilteredPhrase>(
            arrayOf(
                Answer("1", "[IF][GET=test2] if_answ") ,
                Answer("2", "[ELSE IF][GET=test1][GET=test2] elseif1_false"),
                Answer("3", "[ELSE IF][GET=test3][GET=test1] elseif2_ok"),
                Answer("4", "[ELSE] else_answ")
            )
        )
        FilteredPhraseConfigurator(phrase, testParams).autoFilter(gameVariables = testParams)
        phrase.answerChooser = AnswerChooserCollection.first();

        var res = phrase.run()
        assert(res.text == "elseif2_ok")
    }


    @Test
    fun test_GamePhrase_answer_multiplyAnswer(){
        val testParams = hashMapOf<String, Any?>()
        testParams["test1"] = false;
        testParams["test2"] = true;
        testParams["test3"] = false;

        val phrase =  createTestPhrase<FilteredPhrase>(
            arrayOf(
                Answer("1", "[IF][GET=test1] if_answ") ,
                Answer("2", "[ELSE IF][GET=test2] elseif1_ok3"),
                Answer("2", "elseif1_ok1"),
                Answer("2", "elseif1_ok2"),
                Answer("3", "[ELSE IF][GET=test3][GET=test1] elseif2_false"),
                Answer("4", "[ELSE] else_answ")
            )
        )
        FilteredPhraseConfigurator(phrase, testParams).autoFilter(gameVariables = testParams)
        phrase.answerChooser = AnswerChooserCollection.first();

       val wp =  TestPhraseWrapper(phrase);

        val expectedAnswersText = arrayOf(
            "elseif1_ok3",
            "elseif1_ok1",
            "elseif1_ok2"
        )

        wp.run()
        val result = wp.resultAnswers.map { it.text }

        expectedAnswersText.forEachIndexed{i , it -> assert(it == result[i])}

    }

    @Test
    fun test_GamePhrase_answer(){
        val testParams = hashMapOf<String, Any?>()
        testParams["if"] = true;
        testParams["elseif"] = false;
        testParams["else"] = false;

        val phrase =  createTestPhrase<FilteredPhrase>(
            arrayOf(
                Answer("1", "[IF][GET=if] if_answ") ,
                Answer("2", "[ELSE IF][GET=elseif] elseif_answ"),
                Answer("3", "[ELSE][GET=else] else_answ")
            )
        )
        FilteredPhraseConfigurator(phrase, testParams).autoFilter(gameVariables = testParams)
        phrase.answerChooser = AnswerChooserCollection.first();

        var res = phrase.run()
        assert(res.text == "if_answ")

        testParams["if"] = false;
        testParams["elseif"] = true;

        res = phrase.run()
        assert(res.text== "elseif_answ")

        testParams["elseif"] = false;
        testParams["else"] = true;

        res = phrase.run()
        assert(res.text == "else_answ")
    }
}

