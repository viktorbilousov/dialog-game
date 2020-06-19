package dialog.game.phrases.configurator

import dialog.game.game.GameData
import dialog.game.phrases.filters.PhraseFilter
import dialog.game.phrases.filters.inline.change.PutFilter
import dialog.game.phrases.filters.inline.text.*
import dialog.game.phrases.filters.phrase.AutoFilter
import dialog.game.phrases.filters.phrase.IfElsePreparingFilterV2
import dialog.game.phrases.filters.phrase.InsertFilter
import dialog.system.models.answer.Answer

class AutoFilterFabric(
    private val variableText: HashMap<String, () -> String> = GameData.variableTexts,
    private val variablePhrases: HashMap<String, () -> Array<String>> = GameData.variablePhrases,
    private val variableAnswers: HashMap<String, () -> Array<Answer>> = GameData.variableAnswers,
    private val gameVariables: HashMap<String, Any?> = GameData.gameVariables
) {

    fun firstOrderFilter() : AutoFilter{
        val filter = AutoFilter();
//        filter.addFilter(PutFilter(variableText))
        filter.addFilter(InsertFilter(variablePhrases, variableAnswers), true)
        filter.addFilter(IfElsePreparingFilterV2(), true)
   //     filter.addFilter(RandomFilter())
        filter.addFilter(IntComparingFilter(gameVariables))
        filter.addFilter(GetBooleanFilter(gameVariables))
        filter.addFilter(GetVariableFilter(gameVariables))
        filter.addFilter(PhraseFilter.filterOnlyPhrases(SetBooleanFilter(gameVariables)))
        filter.addFilter(PhraseFilter.filterOnlyPhrases(SetValueFilter(gameVariables)))
        filter.addFilter(PhraseFilter.filterOnlyPhrases(IntSimpleArithmeticsFilter(gameVariables)))
        filter.addFilter(CountFilter()) { CountFilter.isCountLabel(it) }
        filter.addFilter(IfElseFilterV2())
        return filter;
    }

    fun lastOrderFilter() : AutoFilter{
        val filter = AutoFilter();
        filter.addFilter(PutFilter(variableText))
        filter.addFilter(RandomFilter())
        return filter;
    }

}