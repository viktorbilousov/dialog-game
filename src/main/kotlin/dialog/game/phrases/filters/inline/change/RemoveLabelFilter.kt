package dialog.game.phrases.filters.inline.change

import dialog.game.phrases.filters.FilterLabel
import dialog.game.phrases.filters.InlineChangeTextPhraseFilter
import dialog.game.phrases.filters.PhraseFilter
import tools.FiltersUtils

/**
 * remove all labels
 */
class RemoveLabelFilter() : InlineChangeTextPhraseFilter() {

    override val filterLabelsList: Array<FilterLabel> = arrayOf()

    private var exceptions = HashSet<FilterLabel>()

    constructor(exceptions: Array<FilterLabel>) : this(){
        this.exceptions.addAll(exceptions)
    }

    public fun addException(vararg filter : FilterLabel){
        exceptions.addAll(filter)
    }


    public fun removeLabelFromExceptions(vararg filter : FilterLabel){
        filter.forEach { exceptions.remove(it) }
    }

    override fun changeText(itemText: String, count: Int): String {
        val labels = FiltersUtils.getFilterLabelsTexts(itemText) ?: return itemText
        val res = FiltersUtils.removeLabels(itemText)

        var ignoredLabels = "";
        labels.forEachIndexed { index, _label ->
            val label = FiltersUtils.parseLabel(_label) ?: return@forEachIndexed
            if (exceptions.contains(label)) {
                var cnt = 0;
                exceptions.forEach { if (it == label) cnt = it.intRange }
                for (i in 0..cnt) {
                    ignoredLabels += "[${labels[index + i]}]";
                }
            }
        }
        return ignoredLabels + res
    }

}