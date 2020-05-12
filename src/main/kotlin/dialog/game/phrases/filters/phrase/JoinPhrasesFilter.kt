package dialog.game.phrases.filters.phrase

import dialog.game.phrases.filters.FilterLabel
import dialog.game.phrases.filters.PhraseFilter
import dialog.system.models.Answer
import tools.FiltersUtils

/**
 * [JOIN] as first label
 */
// todo: not only first label
class JoinPhrasesFilter : PhraseFilter {
    override fun filterPhrases(phrases: Array<String>, count: Int): Array<String> {
        if(isContainJoinLabel(phrases)) return arrayOf(phrases
            .filter {  FiltersUtils.isContainLabel(it, FilterLabel.JOIN) }
            .joinToString(separator = "\n") { it })
        return phrases;
    }

    override fun filterAnswers(answer: Array<Answer>, count: Int): Array<Answer> {
        return answer
    }

    private fun isContainJoinLabel(phrases: Array<String>) : Boolean{
        phrases.forEach {
           if( FiltersUtils.isContainLabel(it, FilterLabel.JOIN)) return true;
        }
        return false
    }
}