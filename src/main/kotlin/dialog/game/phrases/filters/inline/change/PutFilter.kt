package dialog.game.phrases.filters.inline.change

import dialog.game.phrases.filters.FilterLabel
import dialog.game.phrases.filters.InlineChangeTextPhraseFilter
import tools.FiltersUtils

/**
 * Answer / Phrases:
 * [PUT=key] this text will be ignored
 * Text :
 * this tag add text [PUT=key] this text will be continued
 */
class PutFilter(private val variableTexts: HashMap<String, () -> String>) :
    InlineChangeTextPhraseFilter {

    override fun changeText(itemText: String, count: Int): String {
        val labels = FiltersUtils.getFilterLabelsInsideText(itemText) ?: return itemText
        var res = itemText;
        for (label in labels) {
            if (FiltersUtils.isLabelParametric(label)
                && FiltersUtils.parseLabel(label) == FilterLabel.PUT
                && variableTexts.containsKey(FiltersUtils.getParameterValue(label))
            ) {
                res = res.replace("[$label]", variableTexts[FiltersUtils.getParameterValue(label)]!!.invoke())
            }
        }
        return res;
    }
}