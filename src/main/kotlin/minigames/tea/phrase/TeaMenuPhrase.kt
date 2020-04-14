package minigames.tea.phrase

import minigames.tea.TeaGame
import minigames.tea.tools.TeaGameUtils
import models.Answer
import models.items.phrase.FilteredPhrase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import phrases.configurator.FilteredPhraseConfigurator

class TeaMenuPhrase(id: String, phrases: Array<String>, answers : Array<Answer>) : FilteredPhrase(id, phrases, answers){
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }
    init {
        FilteredPhraseConfigurator(this).parametric().applyPhrases()

        this.after =  { answer ->
            val answ = TeaGameUtils.answerToTea(answer)!!;
            logger.info("Set goal tea as ${answ.name}")
            TeaGame.goalTea = answ
        }
    }
}