package phrases.filters

import models.Answer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tools.FiltersUtils

/**
 * [SET=key]
 * [UNSET=key]
 * [NOT=key]
 * [GET=key]
 */
class ParamSetBooleanFilter(public val parameters: HashMap<String, Any?> ) {

    companion object {
        private val logger = LoggerFactory.getLogger(ParamSetBooleanFilter::class.java) as Logger
    }

    public fun processSetParameter(str: String) {
        val labels = FiltersUtils.getFilterLabels(str) ?: return
        labels.forEach {
            processSetParameterLabel(it)
        }
    }

    public fun processSetParameterLabel(label: String?) {
        if (label == null) return;

        val value = getParameterValue(label) ?: return
        val name = FiltersUtils.getParameterName(label) ?: return
        val action = Labels.parse(name) ?: return;
        when (action) {
            Labels.SET -> {
                parameters[value] = true
                logger.info("SET $value = true ")
            }
            Labels.UNSET -> {
                parameters[value] = false
                logger.info("SET $value = false")
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
