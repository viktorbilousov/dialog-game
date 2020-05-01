package dialog.game.phrases.filters

import dialog.system.models.Answer

interface InlineChangeTextPhraseFilter : InlineChangePhraseFilter {

    fun changeText(itemText: String, count: Int) : String

    override fun changePhrase(phrase: String, count: Int): String {
        return changeText(phrase, count)
    }

    override fun changeAnswer(answer: Answer, count: Int): Answer {
        answer.text = changeText(answer.text, count)
        return answer;
     }
}