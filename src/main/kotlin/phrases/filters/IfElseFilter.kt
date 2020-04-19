package phrases.filters

import models.Answer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import phrases.collections.FiltersCollection
import tools.FiltersUtils

class IfElseFilter {
    companion object{

        private val logger = LoggerFactory.getLogger(IfElseFilter::class.java) as Logger


        public fun ifElseAnswersFilter(settings: HashMap<String, Any?>) = fun(answers: Array<Answer>, _: Int): Array<Answer> {
            var skeepToNext = false;

            val filtered = answers.filter{ !notContainIfLabels(it.text) }.filter { s ->
                if(FiltersUtils.getFirstFilterLabel(s.text) == "IF") skeepToNext = false;
                if(skeepToNext) return@filter false;
                val res = processIfElse(s.text, settings)
                skeepToNext = res;
                return@filter res;
            }

            return answers.filter { notContainIfLabels(it.text) || filtered.contains(it) }.toTypedArray()
        }

        public fun ifElsePhrasesFilter(settings: HashMap<String, Any?>) = fun(phrases: Array<String>, _: Int): Array<String> {

            var skeepToNext = false;

            val filtered = phrases.filter{ !notContainIfLabels(it)  }.filter { s ->
                if(FiltersUtils.getFirstFilterLabel(s) == "IF") skeepToNext = false;
                if(skeepToNext) return@filter false;
                val res = processIfElse(s, settings)
                skeepToNext = res;
                return@filter res;
            }

            return phrases.filter { notContainIfLabels(it) || filtered.contains(it) }.toTypedArray()
        }


        private fun notContainIfLabels(line : String): Boolean{
            return FiltersUtils.getFirstFilterLabel(line) == null
                    || (
                    FiltersUtils.getFirstFilterLabel(line) != "IF"
                            && FiltersUtils.getFirstFilterLabel(line) != "ELSE"
                            && FiltersUtils.getFirstFilterLabel(line) != "ELSE IF"
                    )
        }

        private fun processIfElse(str: String, settings: HashMap<String, Any?>) : Boolean{
            val labels = FiltersUtils.getFilterLabels(str) ?: return true
            when(labels[0]){
                "IF", "ELSE IF" -> {
                    for (i in 1 until labels.size){
                        val res = ParameterFilter(settings).processGetParameter(labels[i])
                        if(res == null){
                            logger.warn("processIfElse > label '${labels[i]}' in line $str return null")
                            continue;
                        }
                        if(!res) return false
                    }
                    return true
                }
                "ELSE" -> return true;
            }
            return true
        }

    }
}