package dialog.game.minigames.cookies.filter

import dialog.game.game.GameData
import dialog.game.minigames.cookies.models.Cups
import dialog.game.phrases.filters.labels.FilterLabel

import dialog.game.phrases.filters.InlineTextPhraseFilter
import dialog.game.phrases.filters.labels.FilterLabelCollection
import org.apache.log4j.Logger
import tools.FiltersUtils
import java.lang.IllegalArgumentException

/**
 * >[EMPTY_CUP=CUP1]  высыпать муку из 1 стакана (game.cookies.cups.start)
 * >[EMPTY_CUP=CUP2]  высыпать муку из 2 стакана (game.cookies.cups.start)
 * >[EMPTY_CUP=CUP3]  высыпать муку из 3 стакана (game.cookies.cups.start)
  */
class EmptyCupFilter(val gameVariables: HashMap<String, Any?> = GameData.gameVariables) : InlineTextPhraseFilter(){

    companion object{
        private val logger = Logger.getLogger(EmptyCupFilter::class.java) as Logger
    }

    private val FilterLabelCollection = FilterLabelCollection();
    override val filterLabelsList: Array<FilterLabel> = arrayOf(FilterLabelCollection.EMPTY_CUP)

    override fun filterText(itemText: String, count: Int): Boolean {
        val labels = FiltersUtils.getFilterLabelsTexts(itemText) ?: return true
        labels.forEach {
            if(FiltersUtils.parseLabel(it) == FilterLabelCollection.EMPTY_CUP){
                reset(it)
            }
        }
        return true
    }

    private fun reset(label: String){
        val name = FiltersUtils.getParameterValue(label) ?: throw IllegalArgumentException("Cup name not found $label")
        val cups = Cups.valueOf(name);
        gameVariables[cups.gameVariableName] = 0;
        logger.info("${cups.gameVariableName} = 0")
    }
}