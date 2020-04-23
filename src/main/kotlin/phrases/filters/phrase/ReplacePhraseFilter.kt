package phrases.filters.phrase

import models.Answer
import phrases.filters.PhraseFilter
import phrases.filters.ReplaceFilter

class ReplacePhraseFilter(public val variablePhrases: HashMap<String, () -> String>) : PhraseFilter {
    override fun filterPhrases(phrases: Array<String>, count: Int): Array<String> {
        return ReplaceFilter.replacePhrase(variablePhrases)(phrases, count)
    }

    override fun filterAnswers(answer: Array<Answer>, count: Int): Array<Answer> {
        return answer
    }
}