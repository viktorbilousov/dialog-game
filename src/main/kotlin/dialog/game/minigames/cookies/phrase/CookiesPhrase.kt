package dialog.game.minigames.cookies.phrase

import dialog.game.minigames.cookies.filter.EmptyCupFilter
import dialog.game.minigames.cookies.filter.InitCupFilter
import dialog.game.minigames.cookies.filter.PourCupFilter
import dialog.game.minigames.cookies.filter.ResetCupFilter
import dialog.game.phrases.configurator.FilteredPhraseConfigurator
import dialog.system.models.answer.Answer
import dialog.system.models.items.phrase.FilteredPhrase

class CookiesPhrase(id: String, phrases: Array<String>, answers : Array<Answer>) : FilteredPhrase(id, phrases, answers){
    init {
        FilteredPhraseConfigurator(this).autoFilter()
            .addResultAnswerFilter("minigame.cup.emptycup", EmptyCupFilter())
            .addResultAnswerFilter("minigame.cup.initCup", InitCupFilter())
            .addResultAnswerFilter("minigame.cup.pourCup", PourCupFilter())
            .addResultAnswerFilter("minigame.cup.resetCup", ResetCupFilter())

    }
}