package phrases.configurator

import models.items.phrase.FilteredPhrase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import phrases.filters.FilterLabel
import phrases.filters.InlineTextPhraseFilter
import tools.FiltersUtils


/**
 * [SETV][key=value]
 * [SETV][key=null]
 * [UNSETV=key]
 */
class ParamSetValue(private val parameters: HashMap<String, Any?> ) : InlineTextPhraseFilter{

    companion object {
        private val logger = LoggerFactory.getLogger(ParamSetValue::class.java) as Logger
    }

    override fun filterText(itemText: String, count: Int): Boolean {
        process(itemText);
        return true;
    }

    public fun process(string: String) {
        val labels = FiltersUtils.getFilterLabelsTexts(string) ?: return
        labels.forEachIndexed{i, it ->
            val label = FiltersUtils.parseLabel(it) ?: return@forEachIndexed
            when(label){
                FilterLabel.SETV -> {
                    if(!setVariable(labels[i+1])){
                        logger.error("error set variable $it")
                    }
                }
                FilterLabel.UNSETV -> {
                    unsetVariable(it)
                    logger.error("error unset variable $it")
                }
                else -> return@forEachIndexed
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