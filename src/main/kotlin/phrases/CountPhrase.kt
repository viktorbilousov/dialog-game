package phrases

import models.Answer
import models.items.phrase.FilteredPhrase
import models.items.phrase.Phrase

class CountPhrase(id: String, phrases: Array<String>, answers : Array<Answer>) : FilteredPhrase(id, phrases, answers){
    init {
        this.addAnswerFilter("condition.answers", ConditionsFabric.countAnswer)
        this.addAnswerFilter("condition.answers.not", ConditionsFabric.notCountAnswer)
        this.addPhrasesFilter("condition.phrases", ConditionsFabric.countPhrase)
        this.addPhrasesFilter("condition.phrases.not", ConditionsFabric.notCountPhrase)
    }
}