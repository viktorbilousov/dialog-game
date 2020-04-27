package phrases.filters.phrase

import models.Answer
import phrases.filters.FilterLabel
import phrases.filters.PhraseFilter
import tools.FiltersUtils

class ReplacePhraseFilter(private val variablePhrases: HashMap<String, () -> String>) : PhraseFilter {
    override fun filterPhrases(phrases: Array<String>, count: Int): Array<String> {
        return replacePhrase(variablePhrases)(phrases, count)
    }

    override fun filterAnswers(answer: Array<Answer>, count: Int): Array<Answer> {
        return answer
    }

    public fun replacePhrase(variablePhrases: HashMap<String, () -> String>) =
        fun(phrases: Array<String>, _: Int): Array<String> {
            return phrases.map {
                val labels = FiltersUtils.getFilterLabelsInsideText(it) ?: return@map it
                for (label in labels) {
                    if (FiltersUtils.isLabelParametric(label)
                        && FiltersUtils.getParameterName(label.toUpperCase()) == FilterLabel.PUT.name
                        && variablePhrases.containsKey(FiltersUtils.getParameterValue(label))
                    ) {
                        return@map it.replace("[$label]", variablePhrases[FiltersUtils.getParameterValue(label)]!!.invoke())
                    }
                }
                return@map it
            }.toTypedArray()
        }
}