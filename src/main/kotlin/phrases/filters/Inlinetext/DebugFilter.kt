package phrases.filters.Inlinetext

import game.Game
import models.Answer
import phrases.filters.InlineTextPhraseFilter
import phrases.filters.Labels
import tools.FiltersUtils

/*
[debug]
 */
class DebugFilter : InlineTextPhraseFilter {

    override fun filterText(itemText: String, count: Int): Boolean {
        val debug = Game.settings["debug"] as Boolean
        if(debug) return true
        if(FiltersUtils.getFirstFilterLabel(itemText) == null) return true
        if(FiltersUtils.getFirstFilterLabel(itemText)!!.toUpperCase() == Labels.DEBUG.name.toUpperCase()) return false
        return  true
    }


    companion object{
        public val debugAnswerFilter =
            fun(answers: Array<Answer>, count: Int): Array<Answer> {
                return answers.filter {
                   DebugFilter().filterText(it.text, count)
                }.toTypedArray()
            }

    }
}