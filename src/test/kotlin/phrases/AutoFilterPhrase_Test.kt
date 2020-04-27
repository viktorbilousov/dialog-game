package phrases


import models.Answer
import models.items.phrase.APhrase
import models.items.phrase.DebugFilteredPhrase
import models.items.phrase.FilteredPhrase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import phrases.collections.AnswerChooserCollection
import phrases.configurator.FilteredPhraseConfigurator
import tools.TestPhraseTools
import tools.TestPhraseWrapper

class AutoFilterPhrase_Test {
    @Test
    fun simplePhraseTest(){

        val inputAnswers = arrayOf("answers1", "answers2", "answers3")
        val inputPhrases = arrayOf("phrase1", "phrase2", "phrase3")

        val phrase = TestPhraseWrapper(
            TestPhraseTools.createTestPhrase<AutoFilterPhrase>(
                inputPhrases,
                inputAnswers
            )
        )

        phrase.phrase.run()
        Assertions.assertArrayEquals(inputAnswers, phrase.resultAnswers.map { it.text }.toTypedArray())
        Assertions.assertArrayEquals(inputPhrases, phrase.resultPhrases)
    }

    @Test
    fun countFilter(){
        val inputAnswers = arrayOf("[1] answers1", "[!1] answers2", "[2] answers3")
        val inputPhrases = arrayOf("[>1] phrase1", "[>=1] phrase2", "[3] phrase3")

        val phrase = TestPhraseWrapper(
            TestPhraseTools.createTestPhrase<AutoFilterPhrase>(
                inputPhrases,
                inputAnswers
            )
        )

        val expectedAnswers =   arrayOf("answers1")
        val expectedPhrases =   arrayOf("phrase2")

        phrase.phrase.run()
        Assertions.assertArrayEquals(expectedAnswers, phrase.resultAnswers.map { it.text }.toTypedArray())
        Assertions.assertArrayEquals(expectedPhrases, phrase.resultPhrases)
    }

    @Test
    fun getBooleanFilter(){

        val testParams = hashMapOf<String, Any?>()

        testParams["false"] = false;
        testParams["true"] = true;

        val inputAnswers = arrayOf(
            "[IF] [GET=false] if",
            "[ELSE IF] [GET=false] elseif1",
            "[ELSE IF] [GET=true] elseif2",
            "[ELSE] else"
        )

        val inputPhrases = arrayOf(
            "[IF] [GET=false] if",
            "[ELSE IF] [GET=false] elseif1",
            "[ELSE IF] [GET=false] elseif2",
            "[ELSE] else"
        )

        val phrase = TestPhraseWrapper(
            TestPhraseTools.createTestPhrase<FilteredPhrase>(
                inputPhrases,
                inputAnswers
            )
        )

        FilteredPhraseConfigurator(phrase.phrase).autoFilter(gameVariables = testParams)


        val expectedAnswers =   arrayOf("elseif2")
        val expectedPhrases =   arrayOf("else")

        phrase.phrase.run()

        Assertions.assertArrayEquals(expectedAnswers, phrase.resultAnswers.map { it.text }.toTypedArray())
        Assertions.assertArrayEquals(expectedPhrases, phrase.resultPhrases)
    }

    @Test
    fun getIntFilter(){

        val testParams = hashMapOf<String, Any?>()

        testParams["VALUE"] = 1;

        val inputAnswers = arrayOf(
            "[IF] [INT][VALUE=0] if",
            "[ELSE IF] [INT][VALUE=0] elseif1",
            "[ELSE IF] [INT][VALUE=1] elseif2",
            "[ELSE] else"
        )

        val inputPhrases = arrayOf(
            "[IF] [INT][VALUE=0] if",
            "[ELSE IF] [INT][VALUE=0] elseif1",
            "[ELSE IF] [INT][VALUE=0] elseif2",
            "[ELSE] else"
        )

        val phrase = TestPhraseWrapper(
            TestPhraseTools.createTestPhrase(
                inputPhrases,
                inputAnswers
            )
        )

        FilteredPhraseConfigurator(phrase.phrase).autoFilter(gameVariables = testParams)


        val expectedAnswers =   arrayOf("elseif2")
        val expectedPhrases =   arrayOf("else")

        phrase.phrase.run()

        Assertions.assertArrayEquals(expectedAnswers, phrase.resultAnswers.map { it.text }.toTypedArray())
        Assertions.assertArrayEquals(expectedPhrases, phrase.resultPhrases)
    }


    @Test
    fun GET_answer_multiply(){
        val testParams = hashMapOf<String, Any?>()
        testParams["test1"] = true;
        testParams["test2"] = true;

        val phrase = TestPhraseTools.createTestPhrase<AutoFilterPhrase>(
            arrayOf(
                Answer("1", "[GET=test1][GET=test2][GET=test3] error"),
                Answer("2", "[GET=test1][GET=test2] ok")
            )
        )

        FilteredPhraseConfigurator(phrase, testParams).autoFilter(gameVariables = testParams);
        phrase.answerChooser = AnswerChooserCollection.first();

        val res = phrase.run()
        assert(res.text == "ok")
    }
}