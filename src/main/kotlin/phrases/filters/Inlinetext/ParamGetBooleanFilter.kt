package phrases.filters.Inlinetext

import models.Answer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import phrases.filters.InlineTextPhraseFilter
import phrases.filters.Labels
import tools.FiltersUtils

/**
 * [SET=key]
 * [UNSET=key]
 * [NOT=key]
 * [GET=key]
 */
class ParamGetBooleanFilter(public val parameters: HashMap<String, Any?> ):
    InlineTextPhraseFilter {

    override fun filterText(itemText: String, count: Int): Boolean {
        return filterParameter(
            itemText,
            ParamGetBooleanFilter(parameters)
        )
    }

    companion object {
        private val logger = LoggerFactory.getLogger(ParamGetBooleanFilter::class.java) as Logger

        public fun parameterGetAnswersFilter(settings: HashMap<String, Any?>) =
            fun(answers: Array<Answer>, _: Int): Array<Answer> {
                return answers.filter {
                    filterParameter(
                        it.text,
                        ParamGetBooleanFilter(settings)
                    )
                }.toTypedArray()
            }

        public fun parameterGetPhasesFilter(settings: HashMap<String, Any?>) =
            fun(phrases: Array<String>, _: Int): Array<String> {
                return phrases.filter {
                    filterParameter(
                        it,
                        ParamGetBooleanFilter(settings)
                    )
                }.toTypedArray()
            }

        private fun filterParameter(str: String, parameterFilter: ParamGetBooleanFilter) : Boolean{
            val labels = FiltersUtils.getFilterLabels(str) ?: return true
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
            val action = Labels.parse(name) ?: return null;
            when(action){
                Labels.GET -> {
                    logger.info("GET $value ${parameters[value]}")
                    if(parameters[value] == null) return false
                    return parameters[value] as Boolean;
                }
                Labels.NOT -> {
                    logger.info("NOT $value !${parameters[value]}")
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
