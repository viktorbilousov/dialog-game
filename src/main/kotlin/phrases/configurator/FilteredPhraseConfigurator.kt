package phrases.configurator

import game.GameData
import models.Answer
import models.items.phrase.AFilteredPhrase
import models.items.phrase.FilteredPhrase
import org.slf4j.Logger
//import org.slf4j.Logger
import org.slf4j.LoggerFactory
import phrases.collections.AnswerChooserCollection
import phrases.collections.PrinterCollection
import phrases.filters.FilterLabel
import phrases.filters.inline.text.DebugFilter
import phrases.filters.inline.text.GetBooleanFilter
import phrases.configurator.ParamSetBoolean
import phrases.filters.inline.change.RemoveLabelFilter
import phrases.filters.inline.text.IfElseFilterV2
import phrases.filters.phrase.*
import phrases.filters.PhraseFilter
import phrases.filters.inline.text.GetVariableFilter

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
        removeLabelFilter.addException(FilterLabel.DEBUG)
        addFilter(phrase, "debug", DebugFilter(), AFilteredPhrase.Order.Last);
        addAnswerFilter(phrase, "put", ReplaceAnswerFilter(GameData.variableAnswers), AFilteredPhrase.Order.Last);
        addPhraseFilter(phrase, "put", ReplacePhraseFilter(GameData.variablePhrases), AFilteredPhrase.Order.Last);
        addFilter(phrase, "rm", removeLabelFilter, AFilteredPhrase.Order.Last);
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

    public fun parametricSet() : FilteredPhraseConfigurator{
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
        addFilter(phrase, "getv", GetVariableFilter(gameVariables))
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
            logger.info(">> process SET ${it.text}")
            oldAfter.invoke(it)
            ParamSetBoolean(settings).processSetParameter(it.text)
            logger.info("<< process SET")
        }

        return this
    }

    private fun parameterSetVariable(settings: HashMap<String, Any?>): FilteredPhraseConfigurator {
        removeLabelFilter.addException(FilterLabel.SETV, FilterLabel.UNSETV)

        phrase.phrasePrinter = PrinterCollection.hideLabels();
        val oldAfter = phrase.after;
        phrase.after = {
            logger.info(">> process SETV : ${it.text}")
            oldAfter.invoke(it)
            ParamSetValue(settings).process(it.text)
            logger.info("<< process SETV")
        }

        return this
    }


    public fun build(): AFilteredPhrase {
        return this.phrase
    }

    public fun ifElseV2() : FilteredPhraseConfigurator{
        addFilter(phrase, "ifElseV2Preparing", IfElsePreparingFilter())
        addFilter(phrase, "ifElseV2", IfElseFilterV2())
        return this;
    }

    public fun autoFilter(
        variablePhrases: HashMap<String, () -> String> = GameData.variablePhrases,
        variableAnswers: HashMap<String, () -> Array<Answer>> = GameData.variableAnswers,
        gameVariables: HashMap<String, Any?> = GameData.gameVariables
    ) : FilteredPhraseConfigurator{
        addFilter(phrase, "autofilter", AutoFilter(variablePhrases, variableAnswers, gameVariables))
        parametricSet()
        return this;
    }


    public fun parametricIfElseStatement(): FilteredPhraseConfigurator {
        addFilter(phrase, "ifElse", IfElseFilter(gameVariables))
        parameterSet(gameVariables)
        return this
    }

    public fun applyPhrases(): FilteredPhraseConfigurator {
        addPhraseFilter(phrase, "applyAll", ApplyPhrasesFilter())
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
}
