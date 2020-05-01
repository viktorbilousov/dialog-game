package phrases

import dialog.system.models.items.phrase.FilteredPhrase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import dialog.game.phrases.configurator.FilteredPhraseConfigurator

import tools.TestPhraseTools
import tools.TestPhraseWrapper

class GamePhase_PutFilter_Test {
    @Test
    fun replaceAnswers(){

       //val variablePhrases: HashMap<String, () -> String> = HashMap();
        val variableTexts: HashMap<String, () -> String> = HashMap();

        variableTexts["answ_text1"] = { "answer_text1"}
        variableTexts["answ_text2"] = { "answer_text2"}
        variableTexts["phrase_text3"] = { "phrase_text3"}
        variableTexts["phrase_text4"] = { "phrase_text4"}

        val phrase = TestPhraseTools.createTestPhrase<FilteredPhrase>(
            arrayOf("[PUT=phrase_text3] phrase1", "[PUT=phrase_text4] phrase2" ),
            arrayOf("[PUT=answ_text1] asnwer1", "[PUT=answ_text2] asnwer2" )
        )
        FilteredPhraseConfigurator(phrase).autoFilter(variableTexts = variableTexts)
        val wrapper = TestPhraseWrapper(phrase);

        wrapper.run()


        val expectedAnswer = arrayOf(
            "answer_text1 asnwer1",
            "answer_text2 asnwer2"
        )
        val expectedPhrases = arrayOf(
            "phrase_text3 phrase1",
            "phrase_text4 phrase2"
        )
        Assertions.assertArrayEquals(expectedAnswer, wrapper.resultAnswers.map { it.text }.toTypedArray())
        Assertions.assertArrayEquals(expectedPhrases, wrapper.resultPhrases)
    }
}
