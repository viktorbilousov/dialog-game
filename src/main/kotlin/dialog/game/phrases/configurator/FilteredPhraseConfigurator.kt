package dialog.game.phrases.configurator

import dialog.game.game.GameData
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import dialog.game.phrases.collections.AnswerChooserCollection
import dialog.game.phrases.collections.PhraseChoosersCollections
import dialog.game.phrases.collections.PrinterCollection
import dialog.game.phrases.filters.FilterLabel
import dialog.game.phrases.filters.InlineTextPhraseFilter
import dialog.game.phrases.filters.inline.change.RemoveLabelFilter
import dialog.game.phrases.filters.PhraseFilter
import dialog.game.phrases.filters.inline.SetBooleanPhraseFilter
import dialog.game.phrases.filters.inline.SetVariablesPhraseFilter
import dialog.game.phrases.filters.inline.text.*
import dialog.game.phrases.filters.phrase.JoinPhrasesFilter
import dialog.game.phrases.filters.phrase.AutoFilter
import dialog.system.models.Answer
import dialog.system.models.items.phrase.AFilteredPhrase
import dialog.system.models.items.phrase.FilteredPhrase
import dialog.game.phrases.filters.phrase.IfElsePreparingFilter

open class FilteredPhraseConfigurator(
    private val phrase: AFilteredPhrase
) {

    companion object{
        private val logger = LoggerFactory.getLogger(FilteredPhraseConfigurator::class.java) as Logger
    }

    private var gameVariables: HashMap<String, Any?> = GameData.gameVariables;
    private var variableTexts: HashMap<String, () -> String> = GameData.variableTexts
    private var variablePhrases: HashMap<String, () -> Array<String>> = GameData.variablePhrases
    private var variableAnswers: HashMap<String, () -> Array<Answer>> = GameData.variableAnswers

    private var removeLabelFilter = RemoveLabelFilter();

    constructor(phrase: FilteredPhrase, settings: HashMap<String, Any?>) : this(phrase) {
        this.gameVariables = settings;
    }
    constructor(phrase: AFilteredPhrase,
                gameVariables: HashMap<String, Any?> = GameData.gameVariables,
                variableTexts: HashMap<String, () -> String> = GameData.variableTexts,
                variablePhrases: HashMap<String, () -> Array<String>> = GameData.variablePhrases,
                variableAnswers: HashMap<String, () -> Array<Answer>> = GameData.variableAnswers
    ) : this(phrase) {
        this.gameVariables = gameVariables
        this.variableAnswers = variableAnswers
        this.variablePhrases = variablePhrases
        this.variableTexts = variableTexts
    }

    init {
        removeLabelFilter.addException(FilterLabel.DEBUG, FilterLabel.JOIN)
        addFilter( "debug",   DebugFilter(), AFilteredPhrase.Order.Last);
        addFilter( "rm", removeLabelFilter, AFilteredPhrase.Order.Last);
        addPhraseFilter("join" , JoinPhrasesFilter(), AFilteredPhrase.Order.Last)
    }

    public fun auto(): FilteredPhraseConfigurator {
        phrase.answerChooser = AnswerChooserCollection.first()
        phrase.phrasePrinter = PrinterCollection.empty()
        return this
    }

    public fun count(): FilteredPhraseConfigurator {
        addFilter( "cnt", CountFilter());
        return this
    }

    public fun parametric(): FilteredPhraseConfigurator {
//        parameterSet(gameVariables)
//        parameterGet(gameVariables)
//        parameterSetVariable(gameVariables)
//        parameterGetVariable(gameVariables);
        parametricGet()
        parametricSet()
        return this
    }

    public fun parametricSet() : FilteredPhraseConfigurator {
       // parameterSet(gameVariables)
       // parameterSetVariable(gameVariables)
        addResultAnswerFilter("result.set", SetBooleanFilter(gameVariables))
        addResultAnswerFilter("result.setV", SetValueFilter(gameVariables))
        addResultAnswerFilter("result.setI", IntSimpleArithmeticsFilter(gameVariables))

        removeLabelFilter.addException(FilterLabel.SETV, FilterLabel.UNSETV, FilterLabel.SET, FilterLabel.UNSET, FilterLabel.SETI)
        phrase.phrasePrinter = PrinterCollection.hideLabels();

        return this
    }


    public fun parametricGet(): FilteredPhraseConfigurator {
        parameterGet(gameVariables)
        parameterGetVariable(gameVariables);
        return this
    }

    private fun parameterGetVariable(gameVariables: java.util.HashMap<String, Any?>) : FilteredPhraseConfigurator {
        addFilter( "getv",  GetVariableFilter(gameVariables) )
        return this
    }


    private fun parameterGet(settings: HashMap<String, Any?>): FilteredPhraseConfigurator {
        addFilter("get", GetBooleanFilter(settings));
        return this
    }

    private fun parameterSet(settings: HashMap<String, Any?>): FilteredPhraseConfigurator {
       removeLabelFilter.addException(FilterLabel.SET, FilterLabel.UNSET)

        phrase.phrasePrinter = PrinterCollection.hideLabels();
        val oldAfter = phrase.after;
        phrase.after = {
            logger.debug(">> process SET ${it.text}")
            oldAfter.invoke(it)
            SetBooleanFilter(settings).processSetParameter(it.text)
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
            SetValueFilter(settings).process(it.text)
            logger.debug("<< process SETV")
        }

        return this
    }


    public fun build(): AFilteredPhrase {
        return this.phrase
    }

    public fun ifElseV2() : FilteredPhraseConfigurator {
        addFilter( "ifElseV2Preparing", IfElsePreparingFilter() )
        addFilter("ifElseV2", IfElseFilterV2())
        return this;
    }

    public fun autoFilter() : FilteredPhraseConfigurator {
        addFilter("autofilter", AutoFilter(
            variableTexts, variablePhrases, variableAnswers, gameVariables
        )
        )
        parametricSet()
        return this;
    }

    private fun addResultAnswerFilter(id: String, filter: InlineTextPhraseFilter){
        phrase.addResultAnswerFilter(id){
            filter.filterText(it.text, 0)
            return@addResultAnswerFilter it
        }
    }

    public fun randomPhrase() : FilteredPhraseConfigurator {
        phrase.phraseChooser = PhraseChoosersCollections.random()
        return this;
    }

    public fun join(): FilteredPhraseConfigurator {
        addPhraseFilter( "join", JoinPhrasesFilter())
        return this
    }

    public fun auto(answerId: String) : FilteredPhraseConfigurator  {
        phrase.phrasePrinter = PrinterCollection.empty()
        phrase.answerChooser = AnswerChooserCollection.auto(answerId);
        return this
    }
    public fun addFilter(
        filterName: String,
        filter: PhraseFilter,
        order: AFilteredPhrase.Order = AFilteredPhrase.Order.First
    ) : FilteredPhraseConfigurator  {

        phrase.addAnswersFilter("$filterName.answers", order, filter::filterAnswers)
        phrase.addPhrasesFilter("$filterName.phrases", order, filter::filterPhrases)
        return this
    }
//
    public fun addAnswerFilter(
        filterName: String,
        filter: PhraseFilter,
        order: AFilteredPhrase.Order = AFilteredPhrase.Order.First
    ) : FilteredPhraseConfigurator  {
        phrase.addAnswersFilter("$filterName.answers", order, filter::filterAnswers)
        return this
    }

    public fun addPhraseFilter(
        filterName: String,
        filter: PhraseFilter,
        order: AFilteredPhrase.Order = AFilteredPhrase.Order.First
    ): FilteredPhraseConfigurator  {
        phrase.addPhrasesFilter("$filterName.phrases", order, filter::filterPhrases)
        return this
    }




}
