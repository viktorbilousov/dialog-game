package dialog.game.phrases.filters.phrase

import dialog.game.phrases.filters.FilterLabel
import dialog.game.phrases.filters.PhraseFilter
import dialog.system.models.Answer
import tools.FiltersUtils

/**
 * [JOIN] as first label
 */
class JoinPhrasesFilter : PhraseFilter {
    override fun filterPhrases(phrases: Array<String>, count: Int): Array<String> {
        if(isContainJoinLabel(phrases)) return arrayOf(phrases
            .filter {  FiltersUtils.getFirstFilterLabel(it) != FilterLabel.JOIN }
            .joinToString(separator = "\n") { it })
        return phrases;
    }

    override fun filterAnswers(answer: Array<Answer>, count: Int): Array<Answer> {
        return answer
    }

    private fun isContainJoinLabel(phrases: Array<String>) : Boolean{
        val joinLabel = FilterLabel.JOIN
        phrases.forEach {
            FiltersUtils.getFirstFilterLabel(it)?.let{ firstLabel ->  if( firstLabel== joinLabel) return true }
        }
        return false
    }
}