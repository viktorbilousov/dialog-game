package dialog.game.phrases.filters

import dialog.system.models.Answer

interface PhraseFilter {
    fun filterPhrases(phrases: Array<String>, count: Int): Array<String>
    fun filterAnswers(answer: Array<Answer>, count: Int): Array<Answer>
}
