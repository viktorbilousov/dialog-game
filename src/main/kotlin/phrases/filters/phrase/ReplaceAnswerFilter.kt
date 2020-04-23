package phrases.filters.phrase

import models.Answer
import phrases.filters.PhraseFilter
import phrases.filters.ReplaceFilter

class ReplaceAnswerFilter(public val variablePhrases: HashMap<String, () -> Array<Answer>>) : PhraseFilter {
    override fun filterPhrases(phrases: Array<String>, count: Int): Array<String> {
        return phrases
    }

    override fun filterAnswers(answer: Array<Answer>, count: Int): Array<Answer> {
       return ReplaceFilter.replaceAnswer(variablePhrases)(answer, count)
    }

}