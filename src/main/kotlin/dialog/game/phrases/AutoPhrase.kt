package dialog.game.phrases


import dialog.game.phrases.configurator.FilteredPhraseConfigurator
import dialog.system.models.answer.Answer
import dialog.system.models.items.phrase.FilteredPhrase

class AutoPhrase(id: String, phrases: Array<String>,  answers: Array<Answer>) : FilteredPhrase(id, phrases, answers){
    init {
        FilteredPhraseConfigurator(this).autoFilter().auto()
    }
}