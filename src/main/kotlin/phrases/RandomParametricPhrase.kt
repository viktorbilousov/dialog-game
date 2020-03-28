package phrases

import models.Answer
import models.items.phrase.FilteredPhrase
import phrases.collections.PhraseChoosersCollections
import phrases.configurator.FilteredPhraseConfigurator

class RandomParametricPhrase (id: String, phrases: Array<String>, answers : Array<Answer>) : FilteredPhrase(id, phrases, answers){
    init {
        FilteredPhraseConfigurator(this).parametric()
        phraseChooser = PhraseChoosersCollections.random();
    }
}