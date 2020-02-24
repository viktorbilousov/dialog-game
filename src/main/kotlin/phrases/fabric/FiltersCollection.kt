package phrases.fabric

import game.Game
import models.Answer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tools.FiltersTools
import tools.FiltersTools.Companion.getFilterLabels
import tools.FiltersTools.Companion.getLastFilterLabel
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
                if (getLastFilterLabel(it) != "debug") {
                    return@map removeLabels(it)
                }
                return@map it
            }.toTypedArray()        }

        public val removeLabelAnswersFilter= fun(answers: Array<Answer>, count: Int): Array<Answer> {
            return answers.map {
                if (getLastFilterLabel(it.text) != "debug") {
                    it.text = removeLabels(it.text)
                }
                return@map it
            }.toTypedArray()
        }
        public fun removeLabelPhrasesFilter(exceptions: Array<String>) = fun(phrases: Array<String>, count: Int): Array<String> {
            return phrases.map {
                if (getLastFilterLabel(it) == "debug" || getLastFilterLabel(it) == null) return@map it
                var res = removeLabels(it)
                getFilterLabels(it)!!.forEach { label -> if(exceptions.contains(label)) res += "[$label]$res" }
                return@map res
            }.toTypedArray()
        }
            public fun removeLabelAnswersFilter(exceptions: Array<String>) =
            fun(answers: Array<Answer>, _: Int):  Array<Answer> {
                return answers.map {
                    if (getLastFilterLabel(it.text) == "debug" || getLastFilterLabel(it.text) == null) return@map it

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
                notCountFilter(
                    getLastFilterLabel(
                        it.text
                    ), count
                )
            }.toTypedArray()
        }

        public val notCountPhrase = fun(answers: Array<String>, count: Int): Array<String>{
            return answers.filter {
                notCountFilter(
                    getLastFilterLabel(
                        it
                    ), count
                )
            }.toTypedArray()
        }


        public val countAnswer = fun(answers: Array<Answer>, count: Int): Array<Answer> {
            var maxCnt = 0;
            for (answer in answers) {
                val filterLabel =
                    getLastFilterLabel(answer.text);
                if (filterLabel != null) {
                    val number = filterLabel.toIntOrNull() ?: continue
                    if (number > maxCnt) maxCnt = number
                }
            }

            return answers
                .filter {
                    val filterLabel = getLastFilterLabel(it.text)
                        ?: return@filter true;
                    return@filter countFilter(
                        filterLabel,
                        maxCnt,
                        count
                    )
                }.toTypedArray()
        }

        public fun ifElseAnswersFilter(settings: HashMap<String, Any?>) = fun(answers: Array<Answer>, _: Int): Array<Answer> {
            val texts = answers.map { it.text }.toTypedArray()
            val index = ifElseStatement(texts, settings) ?: return answers;
            return arrayOf(answers[index])
        }

        public fun ifElsePhrasesFilter(settings: HashMap<String, Any?>) = fun(phrases: Array<String>, _: Int): Array<String> {
            val index = ifElseStatement(phrases, settings) ?: return phrases;
            return arrayOf(phrases[index])
        }

        private fun countFilter(filterLabel: String, maxCnt: Int, count: Int) : Boolean{
            val lastFilter = "*"
            if (maxCnt < count) {
                return filterLabel == lastFilter
            } else {
                val number = filterLabel.toIntOrNull()
                if (number == null || number == count) {
                    return true
                }
            }
            return false;
        }



        private fun notCountFilter (label: String?, count: Int): Boolean {
            return label == null
                    || !label.startsWith("!")
                    || label.removePrefix("!").toIntOrNull() == null
                    || label.removePrefix("!").toIntOrNull() != count

        }

        public val countPhrase = fun(phrases: Array<String>, count: Int): Array<String> {
            var maxCnt = 0;
            for (phrase in phrases) {
                val filterLabel = getLastFilterLabel(phrase);
                if (filterLabel != null) {
                    val number = filterLabel.toIntOrNull() ?: continue
                    if (number > maxCnt) maxCnt = number
                }
            }
            return  phrases
                .filter {
                    val filterLabel = getLastFilterLabel(it) ?: return@filter true;
                    return@filter countFilter(
                        filterLabel,
                        maxCnt,
                        count
                    )
                }
                .toTypedArray()
        }




        public val debugAnswerFilter =
            fun(answers: Array<Answer>, count: Int): Array<Answer> {
                return answers.filter {
                    val debug = Game.settings["debug"] as Boolean
                    if(debug) return@filter true
                    if(getLastFilterLabel(it.text) == null) return@filter true
                    if(getLastFilterLabel(it.text) == "[debug]") return@filter false
                    return@filter true
                }.toTypedArray()
            }



        private fun filterParameter(str: String, parametersProcessing: ParametersProcessing) : Boolean{
            val labels = getFilterLabels(str) ?: return true
            labels.forEach {
                val isGet = parametersProcessing.processGetParameter(it) ?: return@forEach
                if(!isGet) return false
            }
            return true
        }


        private fun ifElseStatement(lines: Array<String>, settings: HashMap<String, Any?>) : Int?{
            if(lines.size == 1) return null;
            for (index in lines.indices) {
                val res = processIfElse(lines[index], settings) ?: continue;
                if(res) return index;
            }
            return null;
        }

        private fun processIfElse(str: String, settings: HashMap<String, Any?>) : Boolean?{
            val labels = getFilterLabels(str) ?: return null;
            when(labels[0]){
                "IF", "ELSE IF" -> {
                    for (i in 1 until labels.size){
                        val res = ParametersProcessing(settings).processGetParameter(labels[i])
                        if(res == null){
                            logger.error("processIfElse > label ${labels[i]} in line $str return null")
                            continue;
                        }
                        if(!res) return false
                    }
                    return true
                }
                "ELSE" -> return true;
            }
            return null
        }


    }

}