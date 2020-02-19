package phrases

import models.Answer
import models.items.phrase.FilteredPhrase
import models.items.phrase.Phrase

class CountPhrase(id: String, phrases: Array<String>, answers : Array<Answer>) : FilteredPhrase(id, phrases, answers){
    init {
        this.addAnswerFilter("condition", ConditionsFabric.countAnswer)
    }
}