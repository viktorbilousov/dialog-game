package dialog.game.phrases.filters

import dialog.system.models.Answer

abstract class InlinePhraseFilter : PhraseFilter() {

    abstract fun filterPhrase(phrase: String, count: Int) : Boolean
    abstract fun filterAnswer(answer: Answer, count: Int) : Boolean


    override fun filterPhrasesLogic(phrases: Array<String>, count: Int): Array<String> {
        if(filterOnlyAnswer) return phrases;
        return phrases.filter { filterPhrase(it, count) }.toTypedArray()
    }

    override fun filterAnswersLogic(answer: Array<Answer>, count: Int): Array<Answer> {
        if(filterOnlyPhrases) return answer;
        return answer.filter { filterAnswer(it, count) }.toTypedArray()
    }
}