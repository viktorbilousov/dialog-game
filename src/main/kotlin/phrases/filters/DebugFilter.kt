package phrases.filters

import game.Game
import models.Answer
import tools.FiltersUtils

class DebugFilter {
    companion object{
        public val debugAnswerFilter =
            fun(answers: Array<Answer>, count: Int): Array<Answer> {
                return answers.filter {
                    val debug = Game.settings["debug"] as Boolean
                    if(debug) return@filter true
                    if(FiltersUtils.getFirstFilterLabel(it.text) == null) return@filter true
                    if(FiltersUtils.getFirstFilterLabel(it.text)!!.toUpperCase() == Labels.DEBUG.name.toUpperCase()) return@filter false
                    return@filter true
                }.toTypedArray()
            }

    }
}