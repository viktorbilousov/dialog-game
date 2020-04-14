package phrases.configurator

import game.Game
import game.GameData
import models.Answer
import models.items.phrase.AFilteredPhrase
import models.items.phrase.FilteredPhrase
import phrases.collections.AnswerChooserCollection
import phrases.collections.FiltersCollection
import phrases.collections.PrinterCollection
import phrases.filters.ParameterFilter

open class FilteredPhraseConfigurator(private val phrase: FilteredPhrase) {

    private var settings: HashMap<String, Any?> = Game.settings;
    private var gameVariables: HashMap<String, Any?> = Game.gameVariables;
    private var variablePhrasesBuffer: HashMap<String, () -> String> = GameData.variablePhrases;
    private var variableAnswersBuffer: HashMap<String, () -> Array<Answer>> = GameData.variableAnswers;

    constructor(phrase: FilteredPhrase, settings: HashMap<String, Any?>) : this(phrase){
        this.settings = settings;
    }

    init {

        phrase.addAnswerFilter("put.answers", AFilteredPhrase.Order.Last,
            FiltersCollection.replaceAnswerFilter(GameData.variableAnswers)
        )
        phrase.addPhrasesFilter("put.phrases", AFilteredPhrase.Order.Last,
            FiltersCollection.replacePhraseFilter(GameData.variablePhrases)
        )

        phrase.addAnswerFilter("debug", AFilteredPhrase.Order.Last,
            FiltersCollection.debugAnswerFilter
        )
        phrase.addPhrasesFilter("rm", AFilteredPhrase.Order.Last,
            FiltersCollection.removeLabelPhrasesFilter
        )
        phrase.addAnswerFilter("rm", AFilteredPhrase.Order.Last,
            FiltersCollection.removeLabelAnswersFilter
        )
    }
    
    public fun auto() : FilteredPhraseConfigurator {
        phrase.answerChooser = AnswerChooserCollection.first()
        phrase.phrasePrinter = PrinterCollection.empty()
        return this
    }

    public fun count() : FilteredPhraseConfigurator {
        phrase.addAnswerFilter("condition.answers",
            FiltersCollection.countAnswer
        )
        phrase.addAnswerFilter("condition.answers.not",
            FiltersCollection.notCountAnswer
        )
        phrase.addPhrasesFilter("condition.phrases",
            FiltersCollection.countPhrase
        )
        phrase.addPhrasesFilter("condition.phrases.not",
            FiltersCollection.notCountPhrase
        )
        return this
    }

    public fun parametric() : FilteredPhraseConfigurator {
        parameterSet(gameVariables)
        parameterGet(gameVariables)
        return this;
    }


    private fun parameterGet(settings: HashMap<String, Any?>): FilteredPhraseConfigurator {
        phrase.addAnswerFilter("condition.parameter.answer",
            FiltersCollection.parameterGetAnswersFilter(settings)
        )
        phrase.addPhrasesFilter("condition.parameter.phrases",
            FiltersCollection.parameterGetPhasesFilter(settings)
        )
        return this
    }

    private fun parameterSet(settings: HashMap<String, Any?>): FilteredPhraseConfigurator {
        phrase.removeAnswerFilter("rm");
        phrase.addAnswerFilter("rm.param", AFilteredPhrase.Order.Last,
            FiltersCollection.removeLabelAnswersFilter(
                arrayOf(
                    "SET",
                    "UNSET"
                )
            )
        )
        phrase.phrasePrinter = PrinterCollection.parametric();
        phrase.after = {
            ParameterFilter(settings).processSetParameter(it.text)
        }

        return this
    }


    public fun build() : FilteredPhrase{
        return this.phrase
    }


    public fun parametricIfElseStatement() : FilteredPhraseConfigurator {
        phrase.addPhrasesFilter("condition.parameter.ifElse.phrases",
            FiltersCollection.ifElsePhrasesFilter(settings)
        )
        phrase.addAnswerFilter("condition.parameter.ifElse.answer",
            FiltersCollection.ifElseAnswersFilter(settings)
        )
        parameterSet(settings)
        return this
    }

    public fun applyPhrases() : FilteredPhraseConfigurator {
        phrase.addPhrasesFilter("applyAll", AFilteredPhrase.Order.Last,
            FiltersCollection.applyAllPhrasesToOne
        )
        return this
    }

    public fun auto(answerId: String){
        phrase.phrasePrinter = PrinterCollection.empty()
        phrase.answerChooser = AnswerChooserCollection.auto(answerId);
    }

    /*public fun put() : FilteredPhraseConfigurator {
        phrase.addAnswerFilter("put.answers", FilteredPhrase.Order.Last,
            FiltersCollection.replaceAnswerFilter(GameData.variableAnswers)
        )
        phrase.addPhrasesFilter("put.phrases", FilteredPhrase.Order.Last,
            FiltersCollection.replacePhraseFilter(GameData.variablePhrases)
        )
        return this
    }*/
}
