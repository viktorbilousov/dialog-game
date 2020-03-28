package minigames.tea.phrase

import game.Game
import minigames.tea.TeaGame
import models.Answer
import models.items.phrase.FilteredPhrase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import phrases.configurator.FilteredPhraseConfigurator

class TeaGameResult (id: String, phrases: Array<String>, answers : Array<Answer>) : FilteredPhrase(id, phrases, answers){

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }
    init{
        FilteredPhraseConfigurator(this).parametric().applyPhrases()
    }
}