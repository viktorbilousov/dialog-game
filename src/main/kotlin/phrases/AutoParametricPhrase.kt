package phrases

import models.Answer
import models.items.phrase.FilteredPhrase
import phrases.fabric.FilteredPhraseConfigurator

class AutoParametricPhrase (id: String, phrases: Array<String>, answers : Array<Answer>) : FilteredPhrase(id, phrases, answers){
    init {
        FilteredPhraseConfigurator(this).auto().parametric()
    }
}