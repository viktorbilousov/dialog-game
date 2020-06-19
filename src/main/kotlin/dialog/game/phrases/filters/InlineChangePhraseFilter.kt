package dialog.game.phrases.filters

import dialog.system.models.answer.Answer


abstract class  InlineChangePhraseFilter : InlinePhraseFilter() {

    abstract fun changePhrase(phrase: String, count: Int) : String
    abstract fun changeAnswer(answer: Answer, count: Int) : Answer

    override fun filterPhrase(phrase: String, count: Int): Boolean {
        changePhrase(phrase, count)
        return true
    }

    override fun filterAnswer(answer: Answer, count: Int): Boolean {
        changeAnswer(answer, count)
        return true
    }

    override fun filterPhrasesLogic(phrases: Array<String>, count: Int): Array<String> {
        if(filterOnlyAnswer) return phrases;
        return phrases.map { changePhrase(it, count) }.toTypedArray()
    }

    override fun filterAnswersLogic(answer: Array<Answer>, count: Int): Array<Answer> {
        if(filterOnlyPhrases) return answer;
        return answer.map { changeAnswer(it, count) }.toTypedArray()
    }
}