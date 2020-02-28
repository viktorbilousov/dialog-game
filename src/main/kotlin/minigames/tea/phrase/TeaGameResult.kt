package minigames.tea.phrase

import game.Game
import minigames.tea.TeaGame
import models.Answer
import models.items.phrase.FilteredPhrase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import phrases.fabric.FiltersCollection

class TeaGameResult (id: String, phrases: Array<String>, answers : Array<Answer>) : FilteredPhrase(id, phrases, answers){
    private val qualityLabel = "QUALITY"
    private val qualityAnswer = "ANSWER"
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }
    init{
        addPhrasesFilter("qualityText", FiltersCollection.replaceLabelToTextPhrase(qualityLabel, TeaGame.quality().toString() ) )
        addPhrasesFilter("answerText", FiltersCollection.replaceLabelToTextPhrase(qualityAnswer, TeaGame.answer() ) )
        addPhrasesFilter("applyPhrase", FiltersCollection.applyAllPhrasesToOne)
        this.setBeforeFun {
            val reset = Game.settings["game.tea.restart"] ?: false;
            logger.info("reset = $reset")
            if(reset == true){
                TeaGame.reset();
            }
        }
    }
}