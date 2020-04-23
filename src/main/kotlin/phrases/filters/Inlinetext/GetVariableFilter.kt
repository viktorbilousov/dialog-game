package phrases.filters.Inlinetext

import phrases.filters.InlineTextPhraseFilter
import phrases.filters.Labels
import tools.FiltersUtils
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException


/**
 * [GETV][key=value][key1=value2]
 * [NOTV][key=value][key2=value2]
 */
class GetVariableFilter(private val parameters: HashMap<String, Any?> ) : InlineTextPhraseFilter{
    override fun filterText(itemText: String, count: Int): Boolean {
        val labels = FiltersUtils.getFilterLabels(itemText) ?: return true;
        labels.forEachIndexed(){ i, it ->
            val label = Labels.parse(it) ?: return@forEachIndexed
            if(label == Labels.GETV || label == Labels.NOTV) {
                val keysLabel = labels[i+1];
                if(!process(label, keysLabel)) return false
            }
        }
        return true
    }

    private fun process(label: Labels, valuesLabel: String) : Boolean{
        val key = FiltersUtils.getParameterName(valuesLabel)
        val value = FiltersUtils.getParameterValue(valuesLabel)
        return when (label) {
            Labels.NOTV -> parameters[key].toString() != value.toString()
            Labels.GETV -> parameters[key].toString() == value.toString()
            else -> throw IllegalArgumentException("$label is not recognised")
        }
    }


}