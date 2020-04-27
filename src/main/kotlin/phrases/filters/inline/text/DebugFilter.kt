package phrases.filters.inline.text

import game.Game
import phrases.filters.InlineTextPhraseFilter
import phrases.filters.FilterLabel
import tools.FiltersUtils

/*
[debug]
 */
class DebugFilter : InlineTextPhraseFilter {

    override fun filterText(itemText: String, count: Int): Boolean {
        val debug = Game.settings["debug"] as Boolean
        if(debug) return true
        if(FiltersUtils.getFirstFilterLabelText(itemText) == null) return true
        if(FiltersUtils.getFirstFilterLabelText(itemText)!!.toUpperCase() == FilterLabel.DEBUG.name.toUpperCase()) return false
        return  true
    }

}