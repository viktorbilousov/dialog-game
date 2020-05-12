package dialog.game.minigames.tea.phrase

import dialog.game.game.GameData
import dialog.game.minigames.tea.TeaGame
import dialog.game.minigames.tea.tools.TeaGameUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import dialog.game.phrases.configurator.FilteredPhraseConfigurator
import dialog.game.phrases.collections.PrinterCollection
import dialog.system.models.Answer
import dialog.system.models.items.phrase.FilteredPhrase
import dialog.system.models.items.phrase.PhrasePrinter

class TeaGamePhrase (id: String, phrases: Array<String>, answers : Array<Answer>) : FilteredPhrase(id, phrases, answers){

    private val logger = LoggerFactory.getLogger(TeaGamePhrase::class.java) as Logger

    init {
        FilteredPhraseConfigurator(this).autoFilter().applyPhrases()

        after = after@{
            val answ = TeaGameUtils.answerToFlower(it) ?: return@after
            logger.info("selected Flower is ${answ.name}")
            TeaGame.currentTea.addFlower(answ)
        }
        before =  {
            logger.info("reset = ${GameData.boolGameVar("game_tea_restart")}")
            if(GameData.boolGameVar("game_tea_restart")){
                resetCount()
                TeaGame.reset()
                GameData.gameVariables["game_tea_restart"] = false;
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