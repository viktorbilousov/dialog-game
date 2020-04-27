package phrases.filters.phrase

import models.Answer
import phrases.filters.PhraseFilter

class ApplyPhrasesFilter : PhraseFilter{
    override fun filterPhrases(phrases: Array<String>, count: Int): Array<String> {
        return arrayOf(phrases.joinToString(separator = "\n\n") { it })
    }

    override fun filterAnswers(answer: Array<Answer>, count: Int): Array<Answer> {
        return answer
    }
}