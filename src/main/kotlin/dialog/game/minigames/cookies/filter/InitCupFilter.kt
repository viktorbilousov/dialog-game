package dialog.game.minigames.cookies.filter

import dialog.game.game.GameData
import dialog.game.minigames.cookies.models.Cups
import dialog.game.phrases.filters.FilterLabel
import dialog.game.phrases.filters.InlineTextPhraseFilter
import org.apache.log4j.Logger
import tools.FiltersUtils
import java.lang.IllegalArgumentException


/**
 * >[INIT_CUP=CUP1]  насыпать муку в 1 стакан (game.cookies.cups.start)
 * >[INIT_CUP=CUP2]  насыпать муку в 2 стакан (game.cookies.cups.start)
 * >[INIT_CUP=CUP3]  насыпать муку в 3 стакан (game.cookies.cups.start)
 */
class InitCupFilter(val gameVariables: HashMap<String, Any?> = GameData.gameVariables) : InlineTextPhraseFilter(){

    companion object{
        private val logger = Logger.getLogger(InitCupFilter::class.java) as Logger
    }

    override val filterLabelsList: Array<FilterLabel> = arrayOf(FilterLabel.INIT_CUP)

    override fun filterText(itemText: String, count: Int): Boolean {
        val labels = FiltersUtils.getFilterLabelsTexts(itemText) ?: return true
        labels.forEach {
            if(FiltersUtils.parseLabel(it) == FilterLabel.INIT_CUP){
                initCup(it)
                return true
            }
        }
        return true
    }

    private fun initCup(label: String){
        val name = FiltersUtils.getParameterValue(label) ?: throw IllegalArgumentException("Cup name not found $label")
        val cup = Cups.valueOf(name);
        gameVariables[cup.gameVariableName] = cup.volume;
        logger.info("SET ${Cups.CUP1.gameVariableName}=${gameVariables[cup.gameVariableName]}")
    }
}