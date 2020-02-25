package phrases.fabric

import game.Game
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tools.FiltersTools

class ParametersProcessing(public val parameters: HashMap<String, Any?> ) {

        private val logger = LoggerFactory.getLogger(this::class.java) as Logger

        public fun processSetParameter(str: String){
            val labels = FiltersTools.getFilterLabels(str) ?: return
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
