package dialog.game.phrases.filters.inline.text

import game.Game
import dialog.game.phrases.filters.InlineTextPhraseFilter
import dialog.game.phrases.filters.FilterLabel
import tools.FiltersUtils

/*
[dialog.game.debug]
 */
class DebugFilter : InlineTextPhraseFilter() {



    override fun filterText(itemText: String, count: Int): Boolean {
        val debug = Game.settings[""] as Boolean
        if(debug) return true
        if(FiltersUtils.getFirstFilterLabelText(itemText) == null) return true
        if(FiltersUtils.getFirstFilterLabelText(itemText)!!.toUpperCase() == FilterLabel.DEBUG.name.toUpperCase()) return false
        return  true
    }

}