package phrases.filters

import models.Answer

interface InlinePhraseFilter : PhraseFilter{

    fun filterPhrase(phrase: String, count: Int) : Boolean
    fun filterAnswer(answer: Answer, count: Int) : Boolean


    override fun filterPhrases(phrases: Array<String>, count: Int): Array<String> {
        return phrases.filter { filterPhrase(it, count) }.toTypedArray()
    }

    override fun filterAnswers(answer: Array<Answer>, count: Int): Array<Answer> {
        return answer.filter { filterAnswer(it, count) }.toTypedArray()
    }
}