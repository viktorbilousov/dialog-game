package phrases

import models.Answer
import models.items.phrase.FilteredPhrase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import phrases.collections.AnswerChooserCollection
import phrases.configurator.FilteredPhraseConfigurator
import tools.TestPhraseTools.Companion.createTestPhrase
import tools.TestPhraseTools

class ParametricPhrase_Test {
    @Test
    fun GET_phrase(){
        val testParams = hashMapOf<String, Any?>()
        testParams["test"] = true;

        val phrase =  createTestPhrase<GamePhrase>(
            arrayOf(
                "[GET=dsjfh] error" ,
                "[GET=test] ok"
            ),
            arrayOf(
                Answer("1", "answ")
            )
        )
        FilteredPhraseConfigurator(phrase, testParams).parametric()
        phrase.answerChooser = AnswerChooserCollection.first();
        val printer = TestPhraseTools.setTestPrinter(phrase)

        phrase.run()
        assert(printer.lastPhrase == "ok")
    }


    @Test
    fun NOT_phrase(){
        val testParams = hashMapOf<String, Any?>()
        testParams["test"] = true;


        val phrase = createTestPhrase<GamePhrase>(
            arrayOf(
                "[NOT=dsjfh] ok" ,
                "[NOT=test] error"
            ),
            arrayOf(
                Answer("1", "answr")
            )
        )
        FilteredPhraseConfigurator(phrase, testParams).parametric()
        phrase.answerChooser = AnswerChooserCollection.first();
        val printer = TestPhraseTools.setTestPrinter(phrase)

        phrase.run()
        assert(printer.lastPhrase == "ok")
    }

    @Test
    fun GET_answer_multiply(){
        val testParams = hashMapOf<String, Any?>()
        testParams["test1"] = true;
        testParams["test2"] = true;

        val phrase =  createTestPhrase<GamePhrase>(
            arrayOf(
                Answer("1", "[GET=test1][GET=test2][GET=test3] error") ,
                Answer("2", "[GET=test1][GET=test2] ok")
            )
        )
        FilteredPhraseConfigurator(phrase, testParams).autoFilter(gameVariables = testParams)
        phrase.answerChooser = AnswerChooserCollection.first();

        val res = phrase.run()
        assert(res.text == "ok")
    }

    @Test
    fun GET_answer(){
        val testParams = hashMapOf<String, Any?>()
        testParams["test"] = true;

        val phrase =  createTestPhrase<FilteredPhrase>(
            arrayOf(
                Answer("1", "[GET=dsjfh] error") ,
                Answer("2", "[GET=test] ok")
            )
        )
        FilteredPhraseConfigurator(phrase, testParams).autoFilter(gameVariables = testParams)
        phrase.answerChooser = AnswerChooserCollection.first();

        val res = phrase.run()
        assert(res.text == "ok")
    }

    @Test
    fun NOT_answer(){
        val testParams = hashMapOf<String, Any?>()
        testParams["test"] = true;

        val phrase =  createTestPhrase<GamePhrase>(
            arrayOf(
                Answer("1", "[NOT=dsjfh] ok") ,
                Answer("2", "[NOT=test] erroe")
            )
        )
        FilteredPhraseConfigurator(phrase).parametric()
        phrase.answerChooser = AnswerChooserCollection.first();

        val res = phrase.run()
        assert(res.text == "ok")
    }


    @Test
    fun SET_answer_(){
        val testParams = hashMapOf<String, Any?>()

        val phrase =  createTestPhrase<GamePhrase>(
            arrayOf(
                Answer("1", "[SET=test] ok")
            )
        )
        FilteredPhraseConfigurator(phrase, testParams).parametric()
        phrase.answerChooser = AnswerChooserCollection.first();

        phrase.run()
        assert(testParams["test"] != null)
    }

    @Test
    fun UNSET_answer(){
        val testParams = hashMapOf<String, Any?>()
        testParams["test"] = true;

        val phrase =  createTestPhrase<GamePhrase>(
            arrayOf(
                Answer("1", "[UNSET=test] ok")
            )
        )
        FilteredPhraseConfigurator(phrase, testParams).parametric()
        phrase.answerChooser = AnswerChooserCollection.first();

        phrase.run()
        assert(testParams["test"] == false)
    }


    @Test
    fun SETV_answer_() {
        val testParams = hashMapOf<String, Any?>()

        val phrase = createTestPhrase<GamePhrase>( arrayOf(
            "[SETV][value1=some_value] ok"
        )
        )
        FilteredPhraseConfigurator(phrase, testParams).parametric().auto()
        phrase.run()

        Assertions.assertEquals(testParams["value1"], "some_value")
      //  Assertions.assertEquals(testParams["value2"], "124")
      //  Assertions.assertEquals(testParams["value3"], "null")
       // Assertions.assertEquals(testParams["value4"], "-1234")

    }

    @Test
    fun UNSETV_answer_() {
        val testParams = hashMapOf<String, Any?>()
        testParams["value"] = "test"
        val phrase = createTestPhrase<GamePhrase>( arrayOf(
            "[UNSETV=value1] ok"
        )
        )
        FilteredPhraseConfigurator(phrase, testParams).parametric().auto()
        phrase.run()

        Assertions.assertEquals(testParams["value1"], null)
        //  Assertions.assertEquals(testParams["value2"], "124")
        //  Assertions.assertEquals(testParams["value3"], "null")
        // Assertions.assertEquals(testParams["value4"], "-1234")

    }

}