package dialog.game.phrases.filters.inline.text

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import dialog.game.phrases.filters.InlineTextPhraseFilter
import dialog.game.phrases.filters.labels.FilterLabel
import dialog.game.phrases.filters.labels.FilterLabelCollection

import tools.FiltersUtils

/**
 * [NOT=key]
 * [GET=key]
 */
class GetBooleanFilter(private val parameters: HashMap<String, Any?> ) :
    InlineTextPhraseFilter() {

    private val FilterLabelCollection = FilterLabelCollection();
    override val filterLabelsList: Array<FilterLabel> = arrayOf(FilterLabelCollection.GET, FilterLabelCollection.NOT)

    override fun filterText(itemText: String, count: Int): Boolean {
        return if(filterParameter(
                itemText,
                GetBooleanFilter(parameters)
            )){
            logger.debug("TRUE: $itemText")
            true
        }else{
            logger.debug("FALSE: $itemText")
            false
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(GetBooleanFilter::class.java) as Logger

        private fun filterParameter(str: String, parameterFilter: GetBooleanFilter) : Boolean{
            val labels = FiltersUtils.getFilterLabelsTexts(str) ?: return true
            labels.forEach {
                val isTrue = parameterFilter.processGetParameter(it) ?: return@forEach
                if(!isTrue) return false
            }
            return true
        }
    }
        public fun processGetParameter(label: String?) : Boolean?{
            if(label == null) return null;
            val value = getParameterValue(label) ?: return null;
            val name = FiltersUtils.getParameterName(label) ?: return null;
            val action = FilterLabelCollection.parse(name) ?: return null;
            when(action){
                FilterLabelCollection.GET -> {
                    logger.info("GET $value ${parameters[value]}")
                    if(parameters[value] == null) return false
                    return parameters[value] as Boolean;
                }
                FilterLabelCollection.NOT -> {
                    logger.info("NOT $value ${parameters[value]}")
                    if(parameters[value] == null) return true
                    return !(parameters[value] as Boolean);
                }
            }
            return null;
        }

        private fun getParameterValue(label: String) : String?{
            if (!label.contains('=')) return null;
            val value = label.split("=")
            return value[1].trim();
        }

}
