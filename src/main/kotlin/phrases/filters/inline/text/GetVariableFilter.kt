package phrases.filters.inline.text

import phrases.filters.InlineTextPhraseFilter
import phrases.filters.FilterLabel
import tools.FiltersUtils
import java.lang.IllegalArgumentException


/**
 * [GETV][key=value][GETV][key1=value2]
 * [NOTV][key=value][NOTV][key2=value2]
 */
class GetVariableFilter(private val parameters: HashMap<String, Any?> ) : InlineTextPhraseFilter{
    override fun filterText(itemText: String, count: Int): Boolean {
        val labels = FiltersUtils.getFilterLabelsTexts(itemText) ?: return true;
        labels.forEachIndexed(){ i, it ->
            val label = FilterLabel.parse(it) ?: return@forEachIndexed
            if(label == FilterLabel.GETV || label == FilterLabel.NOTV) {
                val keysLabel = labels[i+1];
                if(!process(label, keysLabel)) return false
            }
        }
        return true
    }

    private fun process(label: FilterLabel, valuesLabel: String) : Boolean{
        val key = FiltersUtils.getParameterName(valuesLabel)
        val value = FiltersUtils.getParameterValue(valuesLabel)
        return when (label) {
            FilterLabel.NOTV -> parameters[key].toString() != value.toString()
            FilterLabel.GETV -> parameters[key].toString() == value.toString()
            else -> throw IllegalArgumentException("$label is not recognised")
        }
    }


}