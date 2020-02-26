package phrases.fabric

import game.Game
import models.Answer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tools.FiltersTools.Companion.getFilterLabels
import tools.FiltersTools.Companion.getFirstFilterLabel
import tools.FiltersTools.Companion.removeLabels

class FiltersCollection {
    companion object {

        private val logger = LoggerFactory.getLogger(this::class.java) as Logger

        public fun parameterGetAnswersFilter(settings: HashMap<String, Any?>) =
            fun(answers: Array<Answer>, _: Int): Array<Answer> {
                return answers.filter {
                    filterParameter(it.text, ParametersProcessing(settings))
                }.toTypedArray()
            }

        public fun parameterGetPhasesFilter(settings: HashMap<String, Any?>) =
            fun(phrases: Array<String>, _: Int): Array<String> {
                return phrases.filter {
                    filterParameter(it,ParametersProcessing(settings))
                }.toTypedArray()
            }

        public val removeLabelPhrasesFilter = fun(phrases: Array<String>, count: Int): Array<String> {
            return phrases.map {
                if (getFirstFilterLabel(it) != "debug") {
                    return@map removeLabels(it)
                }
                return@map it
            }.toTypedArray()        }

        public val removeLabelAnswersFilter= fun(answers: Array<Answer>, count: Int): Array<Answer> {
            return answers.map {
                if (getFirstFilterLabel(it.text) != "debug") {
                    it.text = removeLabels(it.text)
                }
                return@map it
            }.toTypedArray()
        }
        public fun removeLabelPhrasesFilter(exceptions: Array<String>) = fun(phrases: Array<String>, count: Int): Array<String> {
            return phrases.map {
                if (getFirstFilterLabel(it) == "debug" || getFirstFilterLabel(it) == null) return@map it
                var res = removeLabels(it)
                getFilterLabels(it)!!.forEach { label -> if(exceptions.contains(label)) res += "[$label]$res" }
                return@map res
            }.toTypedArray()
        }
            public fun removeLabelAnswersFilter(exceptions: Array<String>) =
            fun(answers: Array<Answer>, _: Int):  Array<Answer> {
                return answers.map {
                    if (getFirstFilterLabel(it.text) == "debug" || getFirstFilterLabel(it.text) == null) return@map it

                    var res = removeLabels(it.text)

                    for (label in getFilterLabels(it.text)!!) {
                        exceptions.forEach {exception ->
                            if(label.startsWith(exception)) {
                                res = "[$label]$res"
                                return@forEach
                            }
                        }
                    }
                    it.text = res;
                    return@map it
                }.toTypedArray()
            }

        public val notCountAnswer = fun(answers: Array<Answer>, count: Int): Array<Answer>{
            return answers.filter {
                val labels = getFilterLabels(it.text) ?: return@filter true
                for (label in labels) {
                    if(!notCountFilter(label , count))  return@filter false
                }
                return@filter true
            }.toTypedArray()
        }

        public val notCountPhrase = fun(phrases: Array<String>, count: Int): Array<String>{
            return phrases.filter {
                val labels = getFilterLabels(it) ?: return@filter true
                for (label in labels) {
                    if(!notCountFilter(label , count))  return@filter false
                }
                return@filter true
            }.toTypedArray()
        }


        public val countAnswer = fun(answers: Array<Answer>, count: Int): Array<Answer> {
            var maxCnt = 0;
            for (answer in answers) {
                val filterLabel =
                    getFirstFilterLabel(answer.text);
                if (filterLabel != null) {
                    val number = filterLabel.toIntOrNull() ?: continue
                    if (number > maxCnt) maxCnt = number
                }
            }
            return answers
                .filter {
                    val filterLabels = getFilterLabels(it.text) ?: return@filter true;
                    for (label in filterLabels) {
                        if (!countFilter(label, maxCnt, count)) return@filter false
                    }
                    return@filter true
                }.toTypedArray()
        }

        public val countPhrase = fun(phrases: Array<String>, count: Int): Array<String> {
            var maxCnt = 0;
            for (phrase in phrases) {
                val filterLabel = getFirstFilterLabel(phrase);
                if (filterLabel != null) {
                    val number = filterLabel.toIntOrNull() ?: continue
                    if (number > maxCnt) maxCnt = number
                }
            }
            return  phrases
                .filter {
                    val filterLabels = getFilterLabels(it) ?: return@filter true;
                    for (label in filterLabels) {
                        if (!countFilter(label, maxCnt, count)) return@filter false
                    }
                    return@filter true
                }
                .toTypedArray()
        }


        public fun ifElseAnswersFilter(settings: HashMap<String, Any?>) = fun(answers: Array<Answer>, _: Int): Array<Answer> {

            var skeepToNext = false;

            val filtered = answers.filter{ !notContainIfLabels(it.text) }.filter { s ->
                if(getFirstFilterLabel(s.text) == "IF") skeepToNext = false;
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
                if(getFirstFilterLabel(s) == "IF") skeepToNext = false;
                if(skeepToNext) return@filter false;
                val res = processIfElse(s, settings)
                skeepToNext = res;
                return@filter res;
            }

            return phrases.filter { notContainIfLabels(it) || filtered.contains(it) }.toTypedArray()
        }


        private fun notContainIfLabels(line : String): Boolean{
            return getFirstFilterLabel(line) == null
                    || (
                        getFirstFilterLabel(line) != "IF"
                            && getFirstFilterLabel(line) != "ELSE"
                            && getFirstFilterLabel(line) != "ELSE IF"
                    )
        }

        private fun countFilter(filterLabel: String, maxCnt: Int, count: Int) : Boolean{
            val lastFilter = "*"
            if(filterLabel == lastFilter){
                return count > maxCnt
            }
            else {
                val number = filterLabel.toIntOrNull() ?: return true
                return number == count
            }
        }

        private fun notCountFilter (label: String?, count: Int): Boolean {
            return label == null
                    || !label.startsWith("!")
                    || label.removePrefix("!").toIntOrNull() == null
                    || label.removePrefix("!").toIntOrNull() != count

        }

        public val debugAnswerFilter =
            fun(answers: Array<Answer>, count: Int): Array<Answer> {
                return answers.filter {
                    val debug = Game.settings["debug"] as Boolean
                    if(debug) return@filter true
                    if(getFirstFilterLabel(it.text) == null) return@filter true
                    if(getFirstFilterLabel(it.text) == "[debug]") return@filter false
                    return@filter true
                }.toTypedArray()
            }



        private fun filterParameter(str: String, parametersProcessing: ParametersProcessing) : Boolean{
            val labels = getFilterLabels(str) ?: return true
            labels.forEach {
                val isTrue = parametersProcessing.processGetParameter(it) ?: return@forEach
                if(!isTrue) return false
            }
            return true
        }


        private fun processIfElse(str: String, settings: HashMap<String, Any?>) : Boolean{
            val labels = getFilterLabels(str) ?: return true
            when(labels[0]){
                "IF", "ELSE IF" -> {
                    for (i in 1 until labels.size){
                        val res = ParametersProcessing(settings).processGetParameter(labels[i])
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