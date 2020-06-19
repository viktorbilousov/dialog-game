package dialog.game.minigames.mail.phrase

import dialog.game.minigames.mail.filter.MailFilter
import dialog.game.phrases.configurator.FilteredPhraseConfigurator
import dialog.system.models.answer.Answer
import dialog.system.models.items.phrase.FilteredPhrase


class MailPhase(id: String, phrases: Array<String>, answers : Array<Answer>) : FilteredPhrase(id, phrases, answers){

    init {
        FilteredPhraseConfigurator(this).addPhraseFilter("mail" , MailFilter())
    }
}