package phrases.filters

import models.Answer
import tools.FiltersUtils

class RemoveLabelFilter {
    companion object{
        public val removeLabelPhrasesFilter = fun(phrases: Array<String>, count: Int): Array<String> {
            return phrases.map {
                if (FiltersUtils.getFirstFilterLabel(it) != "debug") {
                    return@map FiltersUtils.removeLabels(it)
                }
                return@map it
            }.toTypedArray()        }

        public val removeLabelAnswersFilter= fun(answers: Array<Answer>, count: Int): Array<Answer> {
            return answers.map {
                if (FiltersUtils.getFirstFilterLabel(it.text) != "debug") {
                    it.text = FiltersUtils.removeLabels(it.text)
                }
                return@map it
            }.toTypedArray()
        }

        public fun removeLabelPhrasesFilter(exceptions: Array<String>) = fun(phrases: Array<String>, count: Int): Array<String> {
            return phrases.map {
                if (FiltersUtils.getFirstFilterLabel(it) == "debug" || FiltersUtils.getFirstFilterLabel(it) == null) return@map it
                var res = FiltersUtils.removeLabels(it)
                FiltersUtils.getFilterLabels(it)!!.forEach { label -> if(exceptions.contains(label)) res += "[$label]$res" }
                return@map res
            }.toTypedArray()
        }
        public fun removeLabelAnswersFilter(exceptions: Array<String>) =
            fun(answers: Array<Answer>, _: Int):  Array<Answer> {
                return answers.map {
                    if (FiltersUtils.getFirstFilterLabel(it.text) == "debug" || FiltersUtils.getFirstFilterLabel(it.text) == null) return@map it

                    var res = FiltersUtils.removeLabels(it.text)

                    for (label in FiltersUtils.getFilterLabels(it.text)!!) {
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
    }

}