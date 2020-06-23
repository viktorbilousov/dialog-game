package dialog.game.phrases.filters.inline.text

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import dialog.game.phrases.filters.labels.FilterLabel

import dialog.game.phrases.filters.InlineTextPhraseFilter
import dialog.game.phrases.filters.labels.FilterLabelCollection
import tools.FiltersUtils

/**
 * [SET=key]
 * [UNSET=key]
 * [NOT=key]
 * [GET=key]
 */
class SetBooleanFilter(private val parameters: HashMap<String, Any?> ) :
    InlineTextPhraseFilter() {

    private val FilterLabelCollection = FilterLabelCollection();
    override val filterLabelsList: Array<FilterLabel> = arrayOf(FilterLabelCollection.SET, FilterLabelCollection.UNSET)

    companion object {
        private val logger = LoggerFactory.getLogger(SetBooleanFilter::class.java) as Logger
    }

    override fun filterText(itemText: String, count: Int): Boolean {
        processSetParameter(itemText)
        return true;
    }

    public fun processSetParameter(str: String) {
        val labels = FiltersUtils.getFilterLabelsTexts(str) ?: return
        logger.info("process SET : $str")
        labels.forEach {
            processSetParameterLabel(it)
        }
    }

    public fun processSetParameterLabel(label: String?) {
        if (label == null) return;

        val value = getParameterValue(label) ?: return
        val name = FiltersUtils.getParameterName(label) ?: return
        val action = FilterLabelCollection.parse(name) ?: return;
        when (action) {
            FilterLabelCollection.SET -> {
                parameters[value] = true
                logger.info("[SET] $value = true ")
            }
            FilterLabelCollection.UNSET -> {
                parameters[value] = false
                logger.info("[UNSET] $value = false")
            }
            else -> return;
        }
    }

    private fun getParameterValue(label: String): String? {
        if (!label.contains('=')) return null;
        val value = label.split("=")
        return value[1].trim();
    }

}
