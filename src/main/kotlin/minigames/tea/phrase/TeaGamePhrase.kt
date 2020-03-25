package minigames.tea.phrase

import game.Game
import minigames.tea.TeaGame
import minigames.tea.tools.TeaGameUtils
import models.Answer
import models.items.phrase.FilteredPhrase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import phrases.fabric.FilteredPhraseConfigurator

class TeaGamePhrase (id: String, phrases: Array<String>, answers : Array<Answer>) : FilteredPhrase(id, phrases, answers){

    private val logger = LoggerFactory.getLogger(this::class.java) as Logger

    init {
        FilteredPhraseConfigurator(this).applyPhrases().count()
        TeaGamePhraseConfigurator(this).flowerAnswer(false).gameTablePhrase().legend()
        setAfterFun {
            val answ = TeaGameUtils.answerToFlower(it) ?: return@setAfterFun
            logger.info("selected Flower is ${answ.name}")
            TeaGame.currentTea.addFlower(answ)
        }
        setBeforeFun {
            if(Game.boolGameVar("game.tea.restart")){
                resetCount()
                TeaGame.reset()
            }
        }
    }
}