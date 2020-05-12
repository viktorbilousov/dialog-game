package dialog.game.phrases.filters.inline

import dialog.game.phrases.filters.inline.text.SetBooleanFilter
import dialog.game.phrases.filters.InlinePhraseFilter
import dialog.system.models.Answer

class SetBooleanPhraseFilter(private val parameters: HashMap<String, Any?>) : InlinePhraseFilter{
    override fun filterPhrase(phrase: String, count: Int): Boolean {
        SetBooleanFilter(parameters).processSetParameter(phrase) ?: return true
        return true
    }

    override fun filterAnswer(answer: Answer, count: Int): Boolean {
        return true
    }

}