package dialog.game.phrases.filters.inline.change

import dialog.game.game.GameData
import dialog.game.phrases.filters.FilterLabel
import dialog.game.phrases.filters.InlineChangeTextPhraseFilter
import tools.FiltersUtils

/**
 * Answer / Phrases:
 * [PUT=key] this text will be ignored
 * Text :
 * this tag add text [PUT=key] this text will be continued
 */
class PutFilter(
    private val variableTexts: HashMap<String, () -> String> = GameData.variableTexts,
    private val gameVariables: HashMap<String, Any?> = GameData.gameVariables

) :
    InlineChangeTextPhraseFilter() {

    override val filterLabelsList: Array<FilterLabel> = arrayOf(FilterLabel.PUT)

    override fun changeText(itemText: String, count: Int): String {
        val labels = FiltersUtils.getFilterLabelsInsideText(itemText) ?: return itemText
        var res = itemText;
        for (label in labels) {
            if (FiltersUtils.isLabelParametric(label)
                && FiltersUtils.parseLabel(label) == FilterLabel.PUT) {

                val key = FiltersUtils.getParameterValue(label)
                
                if(gameVariables.containsKey(key)){
                    res = res.replace("[$label]", gameVariables[key]!!.toString())
                }
                else if (variableTexts.containsKey(key)) {
                    res = res.replace("[$label]", variableTexts[key]!!.invoke())
                }
            }
        }
        return res;
    }
}