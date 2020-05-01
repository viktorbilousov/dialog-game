package dialog.game.phrases.filters

import dialog.system.models.Answer


interface InlineChangePhraseFilter : InlinePhraseFilter {

    fun changePhrase(phrase: String, count: Int) : String
    fun changeAnswer(answer: Answer, count: Int) : Answer

    override fun filterPhrase(phrase: String, count: Int): Boolean {
        changePhrase(phrase, count)
        return true
    }

    override fun filterAnswer(answer: Answer, count: Int): Boolean {
        changeAnswer(answer, count)
        return true
    }

    override fun filterPhrases(phrases: Array<String>, count: Int): Array<String> {
        return phrases.map { changePhrase(it, count) }.toTypedArray()
    }

    override fun filterAnswers(answer: Array<Answer>, count: Int): Array<Answer> {
        return answer.map { changeAnswer(it, count) }.toTypedArray()
    }
}