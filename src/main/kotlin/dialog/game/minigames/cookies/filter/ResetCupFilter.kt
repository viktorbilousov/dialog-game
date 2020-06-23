package dialog.game.minigames.cookies.filter

import dialog.game.game.GameData
import dialog.game.minigames.cookies.models.Cups
import dialog.game.phrases.filters.labels.FilterLabel

import dialog.game.phrases.filters.InlineTextPhraseFilter
import dialog.game.phrases.filters.labels.FilterLabelCollection
import org.apache.log4j.Logger
import tools.FiltersUtils

//>[RESET_CUP] высыпать муку обратно (game.cookies.cups.start)
class ResetCupFilter(val gameVariables: HashMap<String, Any?> = GameData.gameVariables) : InlineTextPhraseFilter() {

    companion object{
        private val logger = Logger.getLogger(PourCupFilter::class.java) as Logger
    }

    private val FilterLabelCollection = FilterLabelCollection();
    override val filterLabelsList: Array<FilterLabel> = arrayOf(FilterLabelCollection.RESET_CUP)

    override fun filterText(itemText: String, count: Int): Boolean {
        if(FiltersUtils.isContainLabel(itemText, FilterLabelCollection.RESET_CUP)){
            Cups.values().forEach {
                gameVariables[it.gameVariableName]=0;
                logger.info("${it.gameVariableName}=0")
            }
        }
        return true;
    }
}