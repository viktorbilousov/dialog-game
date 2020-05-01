package dialog.game.minigames.tea.phrase

import dialog.game.phrases.configurator.FilteredPhraseConfigurator
import dialog.system.models.Answer
import dialog.system.models.items.phrase.FilteredPhrase

class TeaGameResult (id: String, phrases: Array<String>, answers : Array<Answer>) : FilteredPhrase(id, phrases, answers){

    init{
        FilteredPhraseConfigurator(this).autoFilter().applyPhrases()
    }
}