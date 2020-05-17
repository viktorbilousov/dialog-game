package dialog.game.phrases.filters.inline.change

import dialog.game.phrases.filters.FilterLabel
import dialog.game.phrases.filters.InlineChangeTextPhraseFilter
import tools.FiltersUtils

/**
 * remove all labels
 */
class RemoveLabelFilter() : InlineChangeTextPhraseFilter() {


    private var exceptions = mutableMapOf<FilterLabel, Int>()

    constructor(exceptions: Array<FilterLabel>) : this(){
        this.exceptions = exceptions.associate { Pair(it, 0) }.toMutableMap()
    }

    public fun addException(vararg filter : FilterLabel){
        filter.forEach { exceptions[it] = 0 }
    }

    public fun addException(vararg filterPairs : Pair<FilterLabel, Int>){
        filterPairs.forEach { exceptions[it.first] = it.second }
    }

    public fun addException(filter : FilterLabel, cntSafeAfterlabels: Int){
         exceptions[filter] = cntSafeAfterlabels
    }

    public fun removeLabelFromExceptions(vararg filter : FilterLabel){
        filter.forEach { exceptions.remove(it) }
    }

    override fun changeText(itemText: String, count: Int): String {
       val labels =  FiltersUtils.getFilterLabelsTexts(itemText) ?: return itemText

        var res = FiltersUtils.removeLabels(itemText)

        var ignoredLabels="";
        labels.forEachIndexed { index, _label ->
            val label = FiltersUtils.parseLabel(_label) ?: return@forEachIndexed
            if (exceptions[label] != null) {
                for (i in 0 .. exceptions[label]!!){
                    ignoredLabels+="[${labels[index+i]}]";
                }
            }
        }

        return ignoredLabels + res
    }

}