package phrases.collections

import models.Answer
import phrases.filters.*
import phrases.filters.Inlinetext.DebugFilter
import phrases.filters.Inlinetext.ParamGetBooleanFilter
import phrases.filters.inlinechange.RemoveLabelFilter
import phrases.filters.phrase.CountFilter
import phrases.filters.phrase.IfElseFilter


class FiltersCollection {
    companion object {

        public val applyAllPhrasesToOne = fun (phrases: Array<String>, count: Int) : Array<String>{
            return arrayOf(phrases.joinToString(separator = "\n\n") { it })
        }

        public fun parameterGetAnswersFilter(settings: HashMap<String, Any?>) =
            fun(answers: Array<Answer>, count: Int): Array<Answer> {
                return ParamGetBooleanFilter.parameterGetAnswersFilter(settings)(answers, count)
            }

        public fun parameterGetPhasesFilter(settings: HashMap<String, Any?>) =
            fun(phrases: Array<String>, count: Int): Array<String> {
                return ParamGetBooleanFilter.parameterGetPhasesFilter(settings)(phrases, count)
            }

        public val removeLabelPhrasesFilter = fun(phrases: Array<String>, count: Int): Array<String> {
            return RemoveLabelFilter.removeLabelPhrasesFilter(phrases, count)
        }

        public val removeLabelAnswersFilter= fun(answers: Array<Answer>, count: Int): Array<Answer> {
            return RemoveLabelFilter.removeLabelAnswersFilter(answers, count)
        }


        public fun replacePhraseFilter(variablePhrases : HashMap<String, () -> String>)
                = fun(phrases: Array<String>, count: Int): Array<String>{
                return ReplaceFilter.replacePhrase(variablePhrases)(phrases, count)
            }

        public fun replaceAnswerFilter(variableAnswers: HashMap<String, () -> Array<Answer>>)
                = fun(answers: Array<Answer>, count: Int): Array<Answer>{
            return ReplaceFilter.replaceAnswer(variableAnswers)(answers, count)
        }


        public fun removeLabelPhrasesFilter(exceptions: Array<String>) = fun(phrases: Array<String>, count: Int): Array<String> {
            return RemoveLabelFilter.removeLabelPhrasesFilter(exceptions)(phrases, count)
        }
            public fun removeLabelAnswersFilter(exceptions: Array<String>) =
            fun(answers: Array<Answer>, count: Int):  Array<Answer> {
                return RemoveLabelFilter.removeLabelAnswersFilter(exceptions)(answers, count)
            }

        public val notCountAnswer = fun(answers: Array<Answer>, count: Int): Array<Answer>{
            return CountFilter().notCountAnswer(answers, count)
        }

        public val notCountPhrase = fun(phrases: Array<String>, count: Int): Array<String>{
            return CountFilter().notCountPhrase(phrases, count)
        }


        public val countAnswer = fun(answers: Array<Answer>, count: Int): Array<Answer> {
            return CountFilter().countAnswer(answers, count)
        }

        public val countPhrase = fun(phrases: Array<String>, count: Int): Array<String> {
            return CountFilter().countPhrase(phrases, count)

        }

        public fun ifElseAnswersFilter(settings: HashMap<String, Any?>) = fun(answers: Array<Answer>, count: Int): Array<Answer> {
            return IfElseFilter.ifElseAnswersFilter(settings)(answers, count)
        }

        public fun ifElsePhrasesFilter(settings: HashMap<String, Any?>) = fun(phrases: Array<String>, count: Int): Array<String> {
            return IfElseFilter.ifElsePhrasesFilter(settings)(phrases, count)
        }

        public val debugAnswerFilter =
            fun(answers: Array<Answer>, count: Int): Array<Answer> {
                return DebugFilter.debugAnswerFilter(answers, count)
            }

    }

}