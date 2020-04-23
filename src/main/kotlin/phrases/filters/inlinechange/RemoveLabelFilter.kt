package phrases.filters.inlinechange

import models.Answer
import phrases.filters.InlineChangePhraseFilter
import tools.FiltersUtils


class RemoveLabelFilter() : InlineChangePhraseFilter {

    private var exceptions: Array<String> = emptyArray()

    constructor(exceptions: Array<String>) : this(){
        this.exceptions = exceptions;
    }

    override fun changePhrase(phrase: String, count: Int): String {
        if (FiltersUtils.getFirstFilterLabel(phrase) == "debug" || FiltersUtils.getFirstFilterLabel(phrase) == null) return phrase
        var res = FiltersUtils.removeLabels(phrase)
        FiltersUtils.getFilterLabels(phrase)!!.forEach { label -> if(exceptions.contains(label)) res += "[$label]$res" }
        return res
    }

    override fun changeAnswer(answer: Answer, count: Int): Answer {
        if (FiltersUtils.getFirstFilterLabel(answer.text) == "debug" || FiltersUtils.getFirstFilterLabel(answer.text) == null) return answer

        var res = FiltersUtils.removeLabels(answer.text)

        for (label in FiltersUtils.getFilterLabels(answer.text)!!) {
            exceptions.forEach {exception ->
                if(label.startsWith(exception)) {
                    res = "[$label]$res"
                    return@forEach
                }
            }
        }
        answer.text = res;
        return answer
    }

    companion object{
        public val removeLabelPhrasesFilter = fun(phrases: Array<String>, count: Int): Array<String> {
            return phrases.map {
                RemoveLabelFilter().changePhrase(it,count)
            }.toTypedArray()   }

        public val removeLabelAnswersFilter= fun(answers: Array<Answer>, count: Int): Array<Answer> {
            return answers.map {
                RemoveLabelFilter().changeAnswer(it, count)
            }.toTypedArray()
        }

        public fun removeLabelPhrasesFilter(exceptions: Array<String>) = fun(phrases: Array<String>, count: Int): Array<String> {
            return phrases.map {
                RemoveLabelFilter(exceptions).changePhrase(it,count)
            }.toTypedArray()
        }
        public fun removeLabelAnswersFilter(exceptions: Array<String>) =
            fun(answers: Array<Answer>, count: Int):  Array<Answer> {
                return answers.map {
                    RemoveLabelFilter(exceptions).changeAnswer(it, count)
                }.toTypedArray()
            }
    }

}