package phrases

import models.Answer
import org.junit.jupiter.api.Test
import phrases.collections.AnswerChooserCollection
import phrases.configurator.FilteredPhraseConfigurator
import tools.PhrasesTestUtils.Companion.createTestPhrase
import tools.TestPhraseTools

class ParametricPhrase_Test {
    @Test
    fun test_ParametricPhrase_phrase_get(){
        val testParams = hashMapOf<String, Any?>()
        testParams["test"] = true;

        val phrase =  createTestPhrase<ParametricPhrase>(
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
    fun test_ParametricPhrase_phrase_not(){
        val testParams = hashMapOf<String, Any?>()
        testParams["test"] = true;


        val phrase = createTestPhrase<ParametricPhrase>(
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
    fun test_ParametricPhrase_answer_multiply_get(){
        val testParams = hashMapOf<String, Any?>()
        testParams["test1"] = true;
        testParams["test2"] = true;

        val phrase =  createTestPhrase<ParametricPhrase>(
            arrayOf(
                Answer("1", "[GET=test1][GET=test2][GET=test3] error") ,
                Answer("2", "[GET=test1][GET=test2] ok")
            )
        )
        FilteredPhraseConfigurator(phrase, testParams).parametric()
        phrase.answerChooser = AnswerChooserCollection.first();

        val res = phrase.run()
        assert(res.text == "ok")
    }

    @Test
    fun test_ParametricPhrase_answer_get(){
        val testParams = hashMapOf<String, Any?>()
        testParams["test"] = true;

        val phrase =  createTestPhrase<ParametricPhrase>(
            arrayOf(
                Answer("1", "[GET=dsjfh] error") ,
                Answer("2", "[GET=test] ok")
            )
        )
        FilteredPhraseConfigurator(phrase, testParams).parametric()
        phrase.answerChooser = AnswerChooserCollection.first();

        val res = phrase.run()
        assert(res.text == "ok")
    }

    @Test
    fun test_ParametricPhrase_answer_not(){
        val testParams = hashMapOf<String, Any?>()
        testParams["test"] = true;

        val phrase =  createTestPhrase<ParametricPhrase>(
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
    fun test_ParametricPhrase_answer_set(){
        val testParams = hashMapOf<String, Any?>()

        val phrase =  createTestPhrase<ParametricPhrase>(
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
    fun test_ParametricPhrase_answer_unset(){
        val testParams = hashMapOf<String, Any?>()
        testParams["test"] = true;

        val phrase =  createTestPhrase<ParametricPhrase>(
            arrayOf(
                Answer("1", "[UNSET=test] ok")
            )
        )
        FilteredPhraseConfigurator(phrase, testParams).parametric()
        phrase.answerChooser = AnswerChooserCollection.first();

        phrase.run()
        assert(testParams["test"] == false)
    }
}