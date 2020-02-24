package phrases

import models.Answer
import models.items.phrase.FilteredPhrase
import phrases.fabric.FilteredPhraseConfigurator
import phrases.fabric.PhraseChoosersCollections

class RandomParametricPhrase (id: String, phrases: Array<String>, answers : Array<Answer>) : FilteredPhrase(id, phrases, answers){
    init {
        FilteredPhraseConfigurator(this).parametric()
        phraseChooser = PhraseChoosersCollections.random();
    }
}