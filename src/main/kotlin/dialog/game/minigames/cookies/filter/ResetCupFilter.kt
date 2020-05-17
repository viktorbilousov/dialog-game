package dialog.game.minigames.cookies.filter

import dialog.game.game.GameData
import dialog.game.minigames.cookies.models.Cups
import dialog.game.phrases.filters.FilterLabel
import dialog.game.phrases.filters.InlineTextPhraseFilter
import org.apache.log4j.Logger
import tools.FiltersUtils

//>[RESET_CUP] высыпать муку обратно (game.cookies.cups.start)
class ResetCupFilter(val gameVariables: HashMap<String, Any?> = GameData.gameVariables) : InlineTextPhraseFilter() {

    companion object{
        private val logger = Logger.getLogger(PourCupFilter::class.java) as Logger
    }

    override val filterLabelsList: Array<FilterLabel> = arrayOf(FilterLabel.RESET_CUP)

    override fun filterText(itemText: String, count: Int): Boolean {
        if(FiltersUtils.isContainLabel(itemText, FilterLabel.RESET_CUP)){
            Cups.values().forEach {
                gameVariables[it.gameVariableName]=0;
                logger.info("${it.gameVariableName}=0")
            }
        }
        return true;
    }
}