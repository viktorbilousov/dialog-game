package dialog.game.phrases.filters

import dialog.system.models.Answer

interface InlineTextPhraseFilter : InlinePhraseFilter {

    fun filterText(itemText: String, count: Int) : Boolean


    override fun filterPhrase(phrase: String, count: Int): Boolean {
        return filterText(phrase, count)
    }

    override fun filterAnswer(answer: Answer, count: Int): Boolean {
        return filterText(answer.text, count)
    }

    override fun filterPhrases(phrases: Array<String>, count: Int): Array<String> {
        return phrases.filter { filterText(it, count) }.toTypedArray()
    }

    override fun filterAnswers(answer: Array<Answer>, count: Int): Array<Answer> {
        return answer.filter { filterText(it.text, count) }.toTypedArray()
    }
}