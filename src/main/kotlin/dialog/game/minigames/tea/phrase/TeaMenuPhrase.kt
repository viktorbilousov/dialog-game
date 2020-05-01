package dialog.game.minigames.tea.phrase

import dialog.game.minigames.tea.TeaGame
import dialog.game.minigames.tea.tools.TeaGameUtils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import dialog.game.phrases.configurator.FilteredPhraseConfigurator
import dialog.system.models.Answer
import dialog.system.models.items.phrase.FilteredPhrase

class TeaMenuPhrase(id: String, phrases: Array<String>, answers : Array<Answer>) : FilteredPhrase(id, phrases, answers){
    companion object {
        private val logger = LoggerFactory.getLogger(TeaMenuPhrase::class.java) as Logger
    }
    init {
        FilteredPhraseConfigurator(this).autoFilter().applyPhrases()

        this.after =  { answer ->
            val answ = TeaGameUtils.answerToTea(answer)!!;
            logger.info("Set goal tea as ${answ.name}")
            TeaGame.goalTea = answ
        }
    }
}