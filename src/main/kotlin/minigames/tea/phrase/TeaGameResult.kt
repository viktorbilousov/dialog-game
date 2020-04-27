package minigames.tea.phrase

import game.Game
import minigames.tea.TeaGame
import models.Answer
import models.items.phrase.FilteredPhrase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import phrases.configurator.FilteredPhraseConfigurator

class TeaGameResult (id: String, phrases: Array<String>, answers : Array<Answer>) : FilteredPhrase(id, phrases, answers){

    init{
        FilteredPhraseConfigurator(this).autoFilter().applyPhrases()
    }
}