package phrases.filters.inline.change

import phrases.filters.FilterLabel
import phrases.filters.InlineChangeTextPhraseFilter
import tools.FiltersUtils


class RemoveLabelFilter() : InlineChangeTextPhraseFilter {

    private var exceptions = mutableSetOf<FilterLabel>()

    constructor(exceptions: Array<FilterLabel>) : this(){
        this.exceptions = exceptions.toMutableSet()
    }

    public fun addException(vararg filter : FilterLabel){
        exceptions.addAll(filter)
    }

    public fun removeLabelFromExceptions(vararg filter : FilterLabel){
        exceptions.removeAll(filter)
    }

    override fun changeText(itemText: String, count: Int): String {
        FiltersUtils.getFirstFilterLabelText(itemText) ?: return itemText

        var res = FiltersUtils.removeLabels(itemText)
        var addNext = false; // todo: rewrite this
        FiltersUtils.getFilterLabelsTexts(itemText)!!.filter {
                labelText ->  FiltersUtils.parseLabel(labelText).let {
            if(addNext) {
                addNext = false;
                return@filter true
            }
            if(it == FilterLabel.SETV && exceptions.contains(it)) addNext = true;
            exceptions.contains(it)
        }
        }
            .reversed().forEach(){
            res = "[$it]$res";
        }

        return res
    }

}