package dialog.game.minigames.cookies.phrase

import dialog.game.game.GameData
import dialog.game.minigames.cookies.filter.EmptyCupFilter
import dialog.game.minigames.cookies.filter.InitCupFilter
import dialog.game.minigames.cookies.filter.PourCupFilter
import dialog.game.minigames.cookies.filter.ResetCupFilter
import dialog.game.phrases.configurator.FilteredPhraseConfigurator
import dialog.game.phrases.filters.FilterLabel
import dialog.game.phrases.filters.PhraseFilter
import dialog.system.models.Answer
import dialog.system.models.items.phrase.EmptyPhrase
import dialog.system.models.items.phrase.FilteredPhrase

class CookiesPhrase(id: String, phrases: Array<String>, answers : Array<Answer>) : FilteredPhrase(id, phrases, answers){
    init {
        FilteredPhraseConfigurator(this).autoFilter()
            .addResultAnswerFilter("minigame.cup.emptycup", EmptyCupFilter(), FilterLabel.EMPTY_CUP)
            .addResultAnswerFilter("minigame.cup.initCup", InitCupFilter(), FilterLabel.INIT_CUP)
            .addResultAnswerFilter("minigame.cup.pourCup", PourCupFilter(), Pair(FilterLabel.POUR_CUP, 1))
            .addResultAnswerFilter("minigame.cup.resetCup", ResetCupFilter(), FilterLabel.RESET_CUP)

    }
}