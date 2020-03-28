package minigames.tea.phrase

import game.Game
import minigames.tea.TeaGame
import minigames.tea.tools.TeaGameUtils
import models.Answer
import models.items.phrase.FilteredPhrase
import models.items.phrase.PhrasePrinter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import phrases.configurator.FilteredPhraseConfigurator
import phrases.collections.PrinterCollection

class TeaGamePhrase (id: String, phrases: Array<String>, answers : Array<Answer>) : FilteredPhrase(id, phrases, answers){

    private val logger = LoggerFactory.getLogger(this::class.java) as Logger

    init {
        FilteredPhraseConfigurator(this).count().applyPhrases()

        setAfterFun {
            val answ = TeaGameUtils.answerToFlower(it) ?: return@setAfterFun
            logger.info("selected Flower is ${answ.name}")
            TeaGame.currentTea.addFlower(answ)
        }
        setBeforeFun {
            logger.info("reset = ${Game.boolGameVar("game_tea_restart")}")
            if(Game.boolGameVar("game_tea_restart")){
                resetCount()
                TeaGame.reset()
                Game.gameVariables["game_tea_restart"] = false;
            }

        }

        this.phrasePrinter = object : PhrasePrinter {
            override fun printTextDialog(text: String, answer: Array<Answer>) {
                PrinterCollection.defTextPrinter(text);
                println(TeaGameUtils.getLegend())
                PrinterCollection.defAnswersPrinter(answer)
            }
        }
    }
}