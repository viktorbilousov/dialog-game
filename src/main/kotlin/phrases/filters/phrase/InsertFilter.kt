package phrases.filters.phrase

import models.Answer
import phrases.filters.FilterLabel
import phrases.filters.PhraseFilter
import tools.FiltersUtils

/**
 * Answer / Phrases:
 * [PUT=key] this text will be ignored
 * Text :
 * this tag add text [PUT=key] this text will be continued
 */
class InsertFilter(private val variablePhrases: HashMap<String, () -> Array<String>>,
                   private val variableAnswers : HashMap<String, () -> Array<Answer>>)
                   : PhraseFilter {

    override fun filterPhrases(phrases: Array<String>, count: Int): Array<String> {
        return insertPhrase(phrases)
    }

    override fun filterAnswers(answer: Array<Answer>, count: Int): Array<Answer> {
        return insertAnswer(answer)
    }

    public fun insertPhrase(phrases: Array<String>): Array<String> {
        val res = arrayListOf<String>();

        phrases.forEach {
            val label = FiltersUtils.getFirstParametricLabelsText(it, FilterLabel.INST)
            if (label == null) {
                res.add(it)
                return@forEach
            }
            if (variablePhrases[FiltersUtils.getParameterValue(label)] != null) {
                res.addAll(
                    variablePhrases[FiltersUtils.getParameterValue(label)]!!.invoke()
                )
            } else {
                res.add(it)
            }
        }
        return res.toTypedArray()
    }


    private fun insertAnswer(answers: Array<Answer>) : Array<Answer> {
        val res = arrayListOf<Answer>();
        answers.forEach {
            val label = FiltersUtils.getFirstParametricLabelsText(it.text, FilterLabel.INST)
            if (label == null) {
                res.add(it)
                return@forEach
            }

            if ( variableAnswers[FiltersUtils.getParameterValue(label)] != null ) {
                res.addAll(
                    variableAnswers[FiltersUtils.getParameterValue(label)]!!
                        .invoke()
                        .map { newAnswer ->
                            Answer(it.id, newAnswer.text)
                        }
                )
            } else {
                res.add(it)
            }
        }
        return res.toTypedArray()
    }
}