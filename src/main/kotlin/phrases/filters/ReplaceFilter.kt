package phrases.filters

import models.Answer
import tools.FiltersUtils

class ReplaceFilter {


    companion object {
       /* public fun replaceLabelToTextAnswer(label: String, text: String) =
            fun(answers: Array<Answer>, _: Int): Array<Answer> {
                return answers.map {
                    val labels = FiltersUtils.getFilterLabels(it.text) ?: return@map it
                    if (labels.contains(label)) {
                        val newAnswer = it;
                        newAnswer.text = newAnswer.text.replace("[$label]", text)
                        return@map newAnswer
                    } else return@map it
                }.toTypedArray()
            }


        public fun replaceLabelToTextPhrase(label: String, text: String) =
            fun(phrases: Array<String>, _: Int): Array<String> {
                return phrases.map {
                    val labels = FiltersUtils.getFilterLabels(it) ?: return@map it
                    if (labels.contains(label)) {
                        return@map it.replace("[$label]", text)
                    } else return@map it
                }.toTypedArray()
            }

        public fun replaceLabelToTextPhrase(label: String, textgen: () -> String) =
            fun(phrases: Array<String>, _: Int): Array<String> {
                return phrases.map {
                    val labels = FiltersUtils.getFilterLabels(it) ?: return@map it
                    if (labels.contains(label)) {
                        return@map it.replace("[$label]", textgen.invoke())
                    } else return@map it
                }.toTypedArray()
            }

        public fun addAnswersInsteadLabel(label: String, inputAnswers: Array<Answer>) =
            fun(answers: Array<Answer>, _: Int): Array<Answer> {
                val res = arrayListOf<Answer>();
                answers.forEach {
                    val firstLabel = FiltersUtils.getFilterLabels(it.text)
                    if (firstLabel == null || !firstLabel.contains(label)) {
                        res.add(it)
                    } else {
                        res.addAll(inputAnswers.map { inAnswer ->
                            return@map Answer(it.id, inAnswer.text)
                        }
                        )
                    }
                }
                return res.toTypedArray()
            }*/


        public fun replacePhrase(variablePhrases: HashMap<String, () -> String>) =
            fun(phrases: Array<String>, _: Int): Array<String> {
                return phrases.map {
                    val labels = FiltersUtils.getFilterLabelsInsideText(it) ?: return@map it
                    for (label in labels) {
                        if (FiltersUtils.isLabelParametric(label)
                            && FiltersUtils.getParameterName(label.toUpperCase()) == Labels.PUT.name
                            && variablePhrases.containsKey(FiltersUtils.getParameterValue(label))
                        ) {
                            return@map it.replace("[$label]", variablePhrases[FiltersUtils.getParameterValue(label)]!!.invoke())
                        }
                    }
                    return@map it
                }.toTypedArray()
            }

        public fun replaceAnswer(variableAnswers : HashMap<String, () -> Array<Answer>>)=
            fun(answers: Array<Answer>, _: Int): Array<Answer> {
                val res = arrayListOf<Answer>();
                answers.forEach {
                    val labels = FiltersUtils.getFilterLabels(it.text)
                    if(labels == null){
                        res.add(it)
                        return@forEach
                    }
                    var isLabelFound = false;
                    for(label in labels) {
                        if (
                            FiltersUtils.isLabelParametric(label)
                            && FiltersUtils.getParameterName(label.toUpperCase()) == Labels.PUT.name.toUpperCase()
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
}