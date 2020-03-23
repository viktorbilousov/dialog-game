package phrases.fabric

import game.Game
import models.items.phrase.FilteredPhrase

open class FilteredPhraseConfigurator(private val phrase: FilteredPhrase) {

    public var settings: HashMap<String, Any?> = Game.settings;

    constructor(phrase: FilteredPhrase, settings: HashMap<String, Any?>) : this(phrase){
        this.settings = settings;
    }

    init {
        phrase.addAnswerFilter("debug", FilteredPhrase.Order.Last, FiltersCollection.debugAnswerFilter)
        phrase.addPhrasesFilter("rm", FilteredPhrase.Order.Last, FiltersCollection.removeLabelPhrasesFilter)
        phrase.addAnswerFilter("rm", FilteredPhrase.Order.Last, FiltersCollection.removeLabelAnswersFilter)
    }
    
    public fun auto() : FilteredPhraseConfigurator{
        phrase.answerChooser = AnswerChooserFabric.auto()
        phrase.phrasePrinter = PrinterFabric.empty()
        return this
    }

    public fun count() : FilteredPhraseConfigurator{
        phrase.addAnswerFilter("condition.answers", FiltersCollection.countAnswer)
        phrase.addAnswerFilter("condition.answers.not", FiltersCollection.notCountAnswer)
        phrase.addPhrasesFilter("condition.phrases", FiltersCollection.countPhrase)
        phrase.addPhrasesFilter("condition.phrases.not", FiltersCollection.notCountPhrase)
        return this
    }

    public fun parametric() : FilteredPhraseConfigurator{
        parameterSet(settings)
        parameterGet(settings)
        return this;
    }


    private fun parameterGet(settings: HashMap<String, Any?>): FilteredPhraseConfigurator{
        phrase.addAnswerFilter("condition.parameter.answer", FiltersCollection.parameterGetAnswersFilter(settings))
        phrase.addPhrasesFilter("condition.parameter.phrases", FiltersCollection.parameterGetPhasesFilter(settings))
        return this
    }

    private fun parameterSet(settings: HashMap<String, Any?>): FilteredPhraseConfigurator{
        phrase.removeAnswerFilter("rm");
        phrase.addAnswerFilter("rm.param", FilteredPhrase.Order.Last, FiltersCollection.removeLabelAnswersFilter(arrayOf("SET", "UNSET")) )
        phrase.phrasePrinter = PrinterFabric.parametric();
        phrase.setAfterFun {
            ParametersProcessing(settings).processSetParameter(it.text)
        }
        return this
    }


    public fun build() : FilteredPhrase{
        return this.phrase
    }


    public fun parametricIfElseStatement() : FilteredPhraseConfigurator{
        phrase.addPhrasesFilter("condition.parameter.ifElse.phrases", FiltersCollection.ifElsePhrasesFilter(settings))
        phrase.addAnswerFilter("condition.parameter.ifElse.answer", FiltersCollection.ifElseAnswersFilter(settings))
        parameterSet(settings)
        return this
    }

    public fun applyPhrases() : FilteredPhraseConfigurator{
        phrase.addPhrasesFilter("applyAll", FilteredPhrase.Order.Last,  FiltersCollection.applyAllPhrasesToOne)
        return this
    }
}
