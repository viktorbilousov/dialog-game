package phrases

import models.Answer
import models.items.phrase.FilteredPhrase
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import phrases.configurator.FilteredPhraseConfigurator

import tools.TestPhraseTools
import tools.TestPhraseWrapper

class GamePhase_InsertFilter_Test {
    @Test
    fun replaceAnswers(){

        val variableAnswers: HashMap<String, () -> Array<Answer>> = HashMap();
        val variablePhrases: HashMap<String, () -> Array<String>> = HashMap();

        variableAnswers["text1"] = { arrayOf(Answer("id1", "TEST1"), Answer( "id2", "TEST2"))}
        variablePhrases["phrases1"] = { arrayOf("Phrases1",  "Phrases2")}

        val phrase = TestPhraseTools.createTestPhrase<FilteredPhrase>(
            arrayOf("[INST=phrases1] phr1"),
            arrayOf("[INST=text1] ans21" )
        )
        FilteredPhraseConfigurator(phrase).autoFilter(variableAnswers = variableAnswers,
            variablePhrases = variablePhrases)
        val wrapper = TestPhraseWrapper(phrase);

        wrapper.run()

        val expectedPhrases = arrayOf(
            "Phrases1",  "Phrases2"
        )

        val expectedAnswer = arrayOf(
            Answer("test.id.0", "TEST1"),
            Answer("test.id.0", "TEST2")
        )
        Assertions.assertArrayEquals(expectedAnswer, wrapper.resultAnswers)
        Assertions.assertArrayEquals(expectedPhrases, wrapper.resultPhrases)
    }
}
