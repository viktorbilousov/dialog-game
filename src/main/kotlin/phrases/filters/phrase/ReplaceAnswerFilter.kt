package phrases.filters.phrase

import models.Answer
import phrases.filters.FilterLabel
import phrases.filters.PhraseFilter
import tools.FiltersUtils

class ReplaceAnswerFilter(private val variablePhrases: HashMap<String, () -> Array<Answer>>) : PhraseFilter {

    override fun filterPhrases(phrases: Array<String>, count: Int): Array<String> {
        return phrases
    }

    override fun filterAnswers(answer: Array<Answer>, count: Int): Array<Answer> {
       return replaceAnswer(variablePhrases)(answer, count)
    }

    private fun replaceAnswer(variableAnswers : HashMap<String, () -> Array<Answer>>)=
        fun(answers: Array<Answer>, _: Int): Array<Answer> {
            val res = arrayListOf<Answer>();
            answers.forEach {
                val labels = FiltersUtils.getFilterLabelsTexts(it.text)
                if(labels == null){
                    res.add(it)
                    return@forEach
                }
                var isLabelFound = false;
                for(label in labels) {
                    if (
                        FiltersUtils.isLabelParametric(label)
                        && FiltersUtils.getParameterName(label.toUpperCase()) == FilterLabel.PUT.name.toUpperCase()
                        && variableAnswers.containsKey(FiltersUtils.getParameterValue(label))
                    ) {
                        isLabelFound = true;
                        res.addAll(
                            variableAnswers[FiltersUtils.getParameterValue(label)]!!
                                .invoke()
                                .map { newAnswer ->
                                    Answer(it.id, newAnswer.text)
                                }
                        )
                    }
                }
                if(!isLabelFound) res.add(it)
            }
            return res.toTypedArray()
        }

}