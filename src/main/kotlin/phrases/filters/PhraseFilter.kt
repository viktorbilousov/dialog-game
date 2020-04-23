package phrases.filters

import models.Answer

interface PhraseFilter {
    fun filterPhrases(phrases: Array<String>, count: Int): Array<String>
    fun filterAnswers(answer: Array<Answer>, count: Int): Array<Answer>
}
