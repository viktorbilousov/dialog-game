package dialog.game.phrases.configurator

import dialog.game.maingame.GameData
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import dialog.game.phrases.collections.AnswerChooserCollection
import dialog.game.phrases.collections.PhraseChoosersCollections
import dialog.game.phrases.collections.PrinterCollection
import dialog.game.phrases.filters.FilterLabel
import dialog.game.phrases.filters.inline.change.RemoveLabelFilter
import dialog.game.phrases.filters.PhraseFilter
import dialog.game.phrases.filters.inline.text.*
import dialog.game.phrases.filters.phrase.JoinPhrasesFilter
import dialog.game.phrases.filters.phrase.AutoFilter
import dialog.system.models.Answer
import dialog.system.models.items.phrase.AFilteredPhrase
import dialog.system.models.items.phrase.FilteredPhrase
import game.Game
import phrases.filters.phrase.IfElsePreparingFilter

open class FilteredPhraseConfigurator(private val phrase: AFilteredPhrase) {

    companion object{
        private val logger = LoggerFactory.getLogger(FilteredPhraseConfigurator::class.java) as Logger
    }

    private var gameVariables: HashMap<String, Any?> = GameData.gameVariables;

    private var removeLabelFilter = RemoveLabelFilter();

    constructor(phrase: FilteredPhrase, settings: HashMap<String, Any?>) : this(phrase) {
        this.gameVariables = settings;
    }

    init {
        removeLabelFilter.addException(FilterLabel.DEBUG, FilterLabel.JOIN)
        addFilter(phrase, "debug",   DebugFilter(), AFilteredPhrase.Order.Last);
        addFilter(phrase, "rm", removeLabelFilter, AFilteredPhrase.Order.Last);
        addPhraseFilter(phrase, "join" , JoinPhrasesFilter(), AFilteredPhrase.Order.Last)
    }

    public fun auto(): FilteredPhraseConfigurator {
        phrase.answerChooser = AnswerChooserCollection.first()
        phrase.phrasePrinter = PrinterCollection.empty()
        return this
    }

    public fun count(): FilteredPhraseConfigurator {
        addFilter(phrase, "cnt", CountFilter());
        return this
    }

    public fun parametric(): FilteredPhraseConfigurator {
        parameterSet(gameVariables)
        parameterGet(gameVariables)
        parameterSetVariable(gameVariables)
        parameterGetVariable(gameVariables);
        return this
    }

    public fun parametricSet() : FilteredPhraseConfigurator {
        parameterSet(gameVariables)
        parameterSetVariable(gameVariables)
        return this
    }

    public fun parametricGet(): FilteredPhraseConfigurator {
        parameterGet(gameVariables)
        parameterGetVariable(gameVariables);
        return this
    }

    private fun parameterGetVariable(gameVariables: java.util.HashMap<String, Any?>) : FilteredPhraseConfigurator {
        addFilter(phrase, "getv",
            GetVariableFilter(gameVariables)
        )
        return this
    }


    private fun parameterGet(settings: HashMap<String, Any?>): FilteredPhraseConfigurator {
        addFilter(phrase, "get", GetBooleanFilter(settings));
        return this
    }

    private fun parameterSet(settings: HashMap<String, Any?>): FilteredPhraseConfigurator {
       removeLabelFilter.addException(FilterLabel.SET, FilterLabel.UNSET)

        phrase.phrasePrinter = PrinterCollection.hideLabels();
        val oldAfter = phrase.after;
        phrase.after = {
            logger.debug(">> process SET ${it.text}")
            oldAfter.invoke(it)
            ParamSetBoolean(settings).processSetParameter(it.text)
            logger.debug("<< process SET")
        }

        return this
    }

    private fun parameterSetVariable(settings: HashMap<String, Any?>): FilteredPhraseConfigurator {
        removeLabelFilter.addException(FilterLabel.SETV, FilterLabel.UNSETV)

        phrase.phrasePrinter = PrinterCollection.hideLabels();
        val oldAfter = phrase.after;
        phrase.after = {
            logger.debug(">> process SETV : ${it.text}")
            oldAfter.invoke(it)
            ParamSetValue(settings).process(it.text)
            logger.debug("<< process SETV")
        }

        return this
    }


    public fun build(): AFilteredPhrase {
        return this.phrase
    }

    public fun ifElseV2() : FilteredPhraseConfigurator {
        addFilter(phrase, "ifElseV2Preparing",
            IfElsePreparingFilter()
        )
        addFilter(phrase, "ifElseV2", IfElseFilterV2())
        return this;
    }

    public fun autoFilter(
        variableTexts: HashMap<String, () -> String> = GameData.variableTexts,
        variablePhrases: HashMap<String, () -> Array<String>> = GameData.variablePhrases,
        variableAnswers: HashMap<String, () -> Array<Answer>> = GameData.variableAnswers,
        gameVariables: HashMap<String, Any?> = GameData.gameVariables
    ) : FilteredPhraseConfigurator {
        addFilter(phrase, "autofilter", AutoFilter(
            variableTexts, variablePhrases, variableAnswers, gameVariables
        )
        )
        parametricSet()
        return this;
    }


    public fun applyPhrases(): FilteredPhraseConfigurator {
        addPhraseFilter(phrase, "applyAll", JoinPhrasesFilter())
        return this
    }

    public fun auto(answerId: String) {
        phrase.phrasePrinter = PrinterCollection.empty()
        phrase.answerChooser = AnswerChooserCollection.auto(answerId);
    }
    private fun addFilter(
        phrase: AFilteredPhrase,
        filterName: String,
        filter: PhraseFilter,
        order: AFilteredPhrase.Order = AFilteredPhrase.Order.First
    ) {
        phrase.addAnswerFilter("$filterName.answers", order, filter::filterAnswers)
        phrase.addPhrasesFilter("$filterName.phrases", order, filter::filterPhrases)
    }

    private fun addAnswerFilter(
        phrase: AFilteredPhrase,
        filterName: String,
        filter: PhraseFilter,
        order: AFilteredPhrase.Order = AFilteredPhrase.Order.First
    ) {
        phrase.addAnswerFilter("$filterName.answers", order, filter::filterAnswers)
    }

    private fun addPhraseFilter(
        phrase: AFilteredPhrase,
        filterName: String,
        filter: PhraseFilter,
        order: AFilteredPhrase.Order = AFilteredPhrase.Order.First
    ) {
        phrase.addPhrasesFilter("$filterName.phrases", order, filter::filterPhrases)
    }

    public fun randomPhrase() : FilteredPhraseConfigurator {
        phrase.phraseChooser = PhraseChoosersCollections.random()
        return this;
    }
}
