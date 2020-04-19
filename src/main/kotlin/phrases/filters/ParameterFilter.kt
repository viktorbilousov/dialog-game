package phrases.filters

import models.Answer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tools.FiltersUtils

class ParameterFilter(public val parameters: HashMap<String, Any?> ) {

    companion object {
        private val logger = LoggerFactory.getLogger(ParameterFilter::class.java) as Logger

        public fun parameterGetAnswersFilter(settings: HashMap<String, Any?>) =
            fun(answers: Array<Answer>, _: Int): Array<Answer> {
                return answers.filter {
                    filterParameter(it.text, ParameterFilter(settings))
                }.toTypedArray()
            }

        public fun parameterGetPhasesFilter(settings: HashMap<String, Any?>) =
            fun(phrases: Array<String>, _: Int): Array<String> {
                return phrases.filter {
                    filterParameter(it, ParameterFilter(settings))
                }.toTypedArray()
            }

        private fun filterParameter(str: String, parameterFilter: ParameterFilter) : Boolean{
            val labels = FiltersUtils.getFilterLabels(str) ?: return true
            labels.forEach {
                val isTrue = parameterFilter.processGetParameter(it) ?: return@forEach
                if(!isTrue) return false
            }
            return true
        }
    }


        public fun processSetParameter(str: String){
            val labels = FiltersUtils.getFilterLabels(str) ?: return
            labels.forEach {
                processSetParameterLabel(it)
            }
        }

       public fun processSetParameterLabel(label: String?){
            if(label == null) return;

            val value = getParameterValue(label) ?: return;
            when(getParameterAction(label)){
                ParameterAction.SET -> {
                    parameters[value] = true
                    logger.info("SET $value = true ")
                }
                ParameterAction.UNSET -> {
                    parameters[value] = false
                    logger.info("SET $value = false")
                }
                else -> return;
            }
        }

        public fun processGetParameter(label: String?) : Boolean?{
            if(label == null) return null;
            val value = getParameterValue(label) ?: return null;
            val action = getParameterAction(label) ?: return null;
            when(action){
                ParameterAction.GET -> {
                    logger.info("GET $value ${parameters[value]}")
                    if(parameters[value] == null) return false
                    return parameters[value] as Boolean;
                }
                ParameterAction.NOT -> {
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

        private fun getParameterAction(label: String) : ParameterAction?{
            var action : ParameterAction? = null;
            for (value in ParameterAction.values()) {
                if(label.startsWith(value.name)){
                    action = value;
                    break;
                }
            }
            return action;
        }

        private enum class ParameterAction{
            SET, UNSET, GET, NOT
        }



}
