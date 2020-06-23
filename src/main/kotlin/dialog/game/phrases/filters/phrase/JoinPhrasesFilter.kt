package dialog.game.phrases.filters.phrase

import dialog.game.phrases.filters.labels.FilterLabel

import dialog.game.phrases.filters.PhraseFilter
import dialog.game.phrases.filters.labels.FilterLabelCollection
import dialog.system.models.answer.Answer
import tools.FiltersUtils

/**
 * [JOIN] as first label
 */
// todo: not only first label
class JoinPhrasesFilter : PhraseFilter() {

    private val FilterLabelCollection = FilterLabelCollection();
    override val filterLabelsList: Array<FilterLabel> = arrayOf(FilterLabelCollection.JOIN)

    init {
        this.filterOnlyPhrases = true;
    }

    override fun filterPhrasesLogic(phrases: Array<String>, count: Int): Array<String> {
        var res = phrases;
        if(isContainJoinLabel(phrases)) {
            res = arrayOf(phrases
                .filter { !FiltersUtils.isContainLabel(it, FilterLabelCollection.JOIN) }
                .joinToString(separator = "\n") { it })
        }
        return res;
    }

    override fun filterAnswersLogic(answer: Array<Answer>, count: Int): Array<Answer> {
        return answer
    }

    private fun isContainJoinLabel(phrases: Array<String>) : Boolean{
        phrases.forEach {
           if( FiltersUtils.isContainLabel(it, FilterLabelCollection.JOIN)) return true;
        }
        return false
    }
}