package dialog.game.phrases.filters.inline

import dialog.game.phrases.filters.inline.text.SetValueFilter
import dialog.game.phrases.filters.InlinePhraseFilter
import dialog.system.models.Answer

class SetVariablesPhraseFilter(private val parameters: HashMap<String, Any?>) : InlinePhraseFilter{
    override fun filterAnswer(answer: Answer, count: Int): Boolean {
        return true
    }

    override fun filterPhrase(phrase: String, count: Int): Boolean {
        SetValueFilter(parameters).process(phrase);
        return true
    }
}