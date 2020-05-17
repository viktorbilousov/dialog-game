package dialog.game.phrases.filters

import dialog.system.models.Answer

abstract class PhraseFilter {
   final public fun filterPhrases(phrases: Array<String>, count: Int): Array<String> {
        if(filterOnlyAnswer) return phrases;
        return filterPhrasesLogic(phrases, count)
            
    }
    final public fun filterAnswers(answers: Array<Answer>, count: Int): Array<Answer>{
        if(filterOnlyPhrases) return answers;
        return filterAnswersLogic(answers, count)
    }

    protected abstract fun filterPhrasesLogic(phrases: Array<String>, count: Int): Array<String>
    protected abstract fun filterAnswersLogic(answers: Array<Answer>, count: Int): Array<Answer>
    
   protected var filterOnlyPhrases : Boolean = false
   protected var filterOnlyAnswer : Boolean = false


    abstract public val filterLabelsList : Array<FilterLabel>


    companion object{
       public fun filterOnlyPhrases (phraseFilter: PhraseFilter) : PhraseFilter{
            phraseFilter.filterOnlyPhrases = true;
            return phraseFilter
        }
       public fun filterOnlyAnswers (phraseFilter: PhraseFilter) : PhraseFilter{
            phraseFilter.filterOnlyAnswer = true;
            return phraseFilter
        }
       public fun filterAll (phraseFilter: PhraseFilter) : PhraseFilter{
            phraseFilter.filterOnlyAnswer = false;
            phraseFilter.filterOnlyAnswer = false;
            return phraseFilter
        }
    }
}
