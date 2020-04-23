package phrases.filters.phrase

import models.Answer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import phrases.filters.Inlinetext.ParamGetBooleanFilter
import phrases.filters.Labels
import phrases.filters.PhraseFilter
import tools.FiltersUtils

/**
 *
 * [IF][GET=bool1][NOT=bool3]
 * [ELSEIF] [key1=v]
 * [ELSE]
 */
class IfElseFilter(private val settings: HashMap<String, Any?>) : PhraseFilter {
    
    override fun filterPhrases(phrases: Array<String>, count: Int): Array<String> {
        return ifElsePhrasesFilter(settings)(phrases,count)
    }

    override fun filterAnswers(answer: Array<Answer>, count: Int): Array<Answer> {
        return ifElseAnswersFilter(settings)(answer, count)
    }

    companion object{

        private val logger = LoggerFactory.getLogger(IfElseFilter::class.java) as Logger


        public fun ifElseAnswersFilter(settings: HashMap<String, Any?>) = fun(answers: Array<Answer>, _: Int): Array<Answer> {
            var skeepToNext = false;

            val filtered = answers.filter{ !notContainIfLabels(
                it.text
            )
            }.filter { s ->
                if(FiltersUtils.getFirstFilterLabel(s.text) == Labels.IF.label) skeepToNext = false;
                if(skeepToNext) return@filter false;
                val res =
                    processIfElse(s.text, settings)
                skeepToNext = res;
                return@filter res;
            }

            return answers.filter { notContainIfLabels( it.text ) || filtered.contains(it) }.toTypedArray()
        }

        public fun ifElsePhrasesFilter(settings: HashMap<String, Any?>) = fun(phrases: Array<String>, _: Int): Array<String> {

            var skeepToNext = false;

            val filtered = phrases.filter{ !notContainIfLabels(
                it
            )
            }.filter { s ->
                if(FiltersUtils.getFirstFilterLabel(s) == Labels.IF.label) skeepToNext = false;
                if(skeepToNext) return@filter false;
                val res = processIfElse(s, settings)
                skeepToNext = res;
                return@filter res;
            }

            return phrases.filter { notContainIfLabels(it) || filtered.contains(it) }
                .toTypedArray()
        }


        private fun notContainIfLabels(line : String): Boolean{

            return FiltersUtils.getFirstFilterLabel(line) == null
                    || (
                        Labels.parse(FiltersUtils.getFirstFilterLabel(line)!!) != Labels.IF
                        && Labels.parse(FiltersUtils.getFirstFilterLabel(line)!!) != Labels.ELSE
                        && Labels.parse(FiltersUtils.getFirstFilterLabel(line)!!)!= Labels.ELSEIF
                    )
        }

        private fun processIfElse(str: String, settings: HashMap<String, Any?>) : Boolean{
            val labels = FiltersUtils.getFilterLabels(str) ?: return true
            when(labels[0]){
                Labels.IF.label, Labels.ELSEIF.label -> {
                    for (i in 1 until labels.size){
                        val res = ParamGetBooleanFilter(settings).processGetParameter(labels[i])
                        if(res == null){
                            logger.warn("processIfElse > label '${labels[i]}' in line $str return null")
                            continue;
                        }
                        if(!res) return false
                    }
                    return true
                }
                Labels.ELSE.label -> return true;
            }
            return true
        }

    }
}