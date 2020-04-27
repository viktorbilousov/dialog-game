package phrases

import models.Answer
import models.items.phrase.FilteredPhrase
import org.junit.jupiter.api.Test
import phrases.collections.AnswerChooserCollection
import phrases.configurator.FilteredPhraseConfigurator
import tools.TestPhraseTools.Companion.createTestPhrase
import tools.TestPhraseTools

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

