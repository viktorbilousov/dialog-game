package dialog.game.phrases.filters.phrase

import dialog.game.phrases.filters.FilterLabel
import dialog.game.phrases.filters.PhraseFilter
import dialog.system.models.Answer
import tools.FiltersUtils

/**
 * [JOIN] as first label
 */
// todo: not only first label
class JoinPhrasesFilter : PhraseFilter() {

    init {
        this.filterOnlyPhrases = true;
    }

    override fun filterPhrasesLogic(phrases: Array<String>, count: Int): Array<String> {
        var res = phrases;
        if(isContainJoinLabel(phrases)) {
            res = arrayOf(phrases
                .filter { !FiltersUtils.isContainLabel(it, FilterLabel.JOIN) }
                .joinToString(separator = "\n") { it })
        }
        return res;
    }

    override fun filterAnswersLogic(answer: Array<Answer>, count: Int): Array<Answer> {
        return answer
    }

    private fun isContainJoinLabel(phrases: Array<String>) : Boolean{
        phrases.forEach {
           if( FiltersUtils.isContainLabel(it, FilterLabel.JOIN)) return true;
        }
        return false
    }
}