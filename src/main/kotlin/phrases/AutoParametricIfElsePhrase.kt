package phrases

import models.Answer
import models.items.phrase.FilteredPhrase
import phrases.fabric.FilteredPhraseConfigurator

class AutoParametricIfElsePhrase (id: String, phrases: Array<String>, answers : Array<Answer>) : FilteredPhrase(id, phrases, answers){
    init {
        FilteredPhraseConfigurator(this).auto().parametricIfElseStatement()
    }
}