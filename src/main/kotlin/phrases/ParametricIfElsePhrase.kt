package phrases

import models.Answer
import models.items.phrase.FilteredPhrase
import phrases.fabric.FilteredPhraseConfigurator

class ParametricIfElsePhrase (id: String, phrases: Array<String>, answers : Array<Answer>) : FilteredPhrase(id, phrases, answers){
    init {
        FilteredPhraseConfigurator(this).parametricIfElseStatement()
    }
}