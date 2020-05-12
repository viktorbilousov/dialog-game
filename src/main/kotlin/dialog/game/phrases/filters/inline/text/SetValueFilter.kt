package dialog.game.phrases.filters.inline.text

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import dialog.game.phrases.filters.FilterLabel
import dialog.game.phrases.filters.InlineTextPhraseFilter
import tools.FiltersUtils


/**
 * [SETV][key=value]
 * [SETV][key=null]
 * [UNSETV=key]
 */
class SetValueFilter(private val parameters: HashMap<String, Any?> ) :
    InlineTextPhraseFilter {

    companion object {
        private val logger = LoggerFactory.getLogger(SetValueFilter::class.java) as Logger
    }

    override fun filterText(itemText: String, count: Int): Boolean {
        process(itemText);
        return true;
    }

    public fun process(string: String) {
        val labels = FiltersUtils.getFilterLabelsTexts(string) ?: return
        logger.info("Process Set Value $string")
        labels.forEachIndexed{i, it ->
            val label = FiltersUtils.parseLabel(it) ?: return@forEachIndexed
            when(label){
                FilterLabel.SETV -> {
                    if(!setVariable(labels[i+1])){
                        logger.error("error set variable $it")
                    }
                }
                FilterLabel.UNSETV -> {
                    if(!unsetVariable(it))  logger.error("error unset variable $it")
                }
                else -> {
                    return@forEachIndexed
                }
            }
        }
    }

    private fun setVariable(valuesLabel: String) : Boolean {
        val key = FiltersUtils.getParameterName(valuesLabel) ?: return false
        val value = FiltersUtils.getParameterValue(valuesLabel) ?: return false
        parameters[key] = value
        logger.info("[SETV] $key=$value")
        return true
    }

    private fun unsetVariable(valuesLabel: String) : Boolean {
        val value = FiltersUtils.getParameterValue(valuesLabel) ?: return false
        parameters[value] = null
        logger.info("[UNSET] Set $value=null")
        return true
    }


}