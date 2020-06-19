package phrases

import dialog.game.phrases.GamePhrase
import dialog.system.models.answer.Answer
import dialog.system.models.items.phrase.FilteredPhrase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import dialog.game.phrases.collections.AnswerChooserCollection
import dialog.game.phrases.configurator.FilteredPhraseConfigurator
import tools.TestPhraseTools.Companion.createTestPhrase
import tools.TestPhraseTools

class ParametricPhrase_Test {
    @Test
    fun GET_phrase(){
        val testParams = hashMapOf<String, Any?>()
        testParams["test"] = true;

        val phrase =  createTestPhrase<FilteredPhrase>(
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
        FilteredPhraseConfigurator(phrase, testParams).autoFilter()
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
        FilteredPhraseConfigurator(phrase, testParams).autoFilter()
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
    fun SET_answer(){
        val testParams = hashMapOf<String, Any?>()

        val phrase =  createTestPhrase<FilteredPhrase>(
            arrayOf(
                Answer("1", "[SET=test] ok")
            )
        )
        FilteredPhraseConfigurator(phrase, testParams).parametric()
        phrase.answerChooser = AnswerChooserCollection.first();

        phrase.run()
        Assertions.assertNotNull(testParams["test"])
    }

    @Test
    fun UNSET_answer(){
        val testParams = hashMapOf<String, Any?>()
        testParams["test"] = true;

        val phrase =  createTestPhrase<FilteredPhrase>(
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

        val phrase = createTestPhrase<FilteredPhrase>( arrayOf(
            "[SETV][value1=some_value] ok"
        )
        )
        FilteredPhraseConfigurator(phrase, testParams).parametric().auto()
        phrase.run()

        Assertions.assertEquals( "some_value", testParams["value1"])
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