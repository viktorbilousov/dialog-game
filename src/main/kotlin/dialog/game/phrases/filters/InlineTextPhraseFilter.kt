package dialog.game.phrases.filters

import dialog.system.models.Answer

abstract class InlineTextPhraseFilter : InlinePhraseFilter() {

    abstract fun filterText(itemText: String, count: Int) : Boolean


    override fun filterPhrase(phrase: String, count: Int): Boolean {
        return filterText(phrase, count)
    }

    override fun filterAnswer(answer: Answer, count: Int): Boolean {
        return filterText(answer.text, count)
    }

    override fun filterPhrasesLogic(phrases: Array<String>, count: Int): Array<String> {
        if(filterOnlyAnswer) return phrases;
        return phrases.filter { filterText(it, count) }.toTypedArray()
    }

    override fun filterAnswersLogic(answer: Array<Answer>, count: Int): Array<Answer> {
        if(filterOnlyPhrases) return answer;
        return answer.filter { filterText(it.text, count) }.toTypedArray()
    }
}