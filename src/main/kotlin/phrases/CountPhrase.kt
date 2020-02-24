package phrases

import models.Answer
import models.items.phrase.FilteredPhrase
import phrases.fabric.FiltersFabric

open class CountPhrase(id: String, phrases: Array<String>, answers : Array<Answer>) : FilteredPhrase(id, phrases, answers){
    init {
        this.addAnswerFilter("condition.answers", FiltersFabric.countAnswer)
        this.addAnswerFilter("condition.answers.not", FiltersFabric.notCountAnswer)
        this.addPhrasesFilter("condition.phrases", FiltersFabric.countPhrase)
        this.addPhrasesFilter("condition.phrases.not", FiltersFabric.notCountPhrase)
    }
}