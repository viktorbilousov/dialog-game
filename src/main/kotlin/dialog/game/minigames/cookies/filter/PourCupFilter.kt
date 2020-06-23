package dialog.game.minigames.cookies.filter

import dialog.game.game.GameData
import dialog.game.minigames.cookies.models.Cups
import dialog.game.phrases.filters.labels.FilterLabel

import dialog.game.phrases.filters.InlineTextPhraseFilter
import dialog.game.phrases.filters.labels.FilterLabelCollection
import org.apache.log4j.Logger
import tools.FiltersUtils

/**
 * >[POUR_CUP][CUP1=CUP2]  пересыпать муку из 1 стакана во 2 (game.cookies.cups.start)
 * >[POUR_CUP][CUP2=CUP3]  пересыпать муку из 2 стакана во 3 (game.cookies.cups.start)
 */
class PourCupFilter(val gameVariables: HashMap<String, Any?> = GameData.gameVariables): InlineTextPhraseFilter(){

    companion object{
        private val logger = Logger.getLogger(PourCupFilter::class.java) as Logger
    }

    private val FilterLabelCollection = FilterLabelCollection();
    override val filterLabelsList: Array<FilterLabel> = arrayOf(FilterLabelCollection.POUR_CUP)

    override fun filterText(itemText: String, count: Int): Boolean {
        val labels = FiltersUtils.getFilterLabelsTexts(itemText) ?: return true
        labels.forEachIndexed{ i, label ->
            if(FilterLabelCollection.parse(label) == FilterLabelCollection.POUR_CUP){
                pour(labels[i+1])
            }
        }
        return true
    }

    private fun pour(label: String){

        val fromCupLabel = FiltersUtils.getParameterName(label) ?: throw IllegalArgumentException("left argument not recognised: $label")
        val toCupLabel = FiltersUtils.getParameterValue(label)  ?: throw IllegalArgumentException("right argument not recognised: $label")

        val fromCup = Cups.valueOf(fromCupLabel);
        val toCup = Cups.valueOf(toCupLabel);
        val fromCupCurrValue = gameVariables[fromCup.gameVariableName].toString().toIntOrNull() ?: 0
        val toCupCurrValue = gameVariables[toCup.gameVariableName].toString().toIntOrNull()  ?: 0

        var rest = 0;
        var newToValue = (toCupCurrValue+fromCupCurrValue)

        if(newToValue > toCup.volume){
            rest = newToValue - toCup.volume;
            newToValue = toCup.volume;
        }

        val newFromValue = rest;

        gameVariables[toCup.gameVariableName] = newToValue
        gameVariables[fromCup.gameVariableName] = newFromValue

        logger.info("${fromCup.name}($fromCupCurrValue) -> ${toCup.name}($toCupCurrValue)")
        logger.info("${toCup.gameVariableName} = ${gameVariables[toCup.gameVariableName]}")
        logger.info("${fromCup.gameVariableName} = ${gameVariables[fromCup.gameVariableName]}")
    }

}