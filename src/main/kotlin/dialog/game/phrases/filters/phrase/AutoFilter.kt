package dialog.game.phrases.filters.phrase

import dialog.game.phrases.filters.FilterLabel
import dialog.game.phrases.filters.PhraseFilter
import dialog.system.models.answer.Answer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tools.FiltersUtils

open class AutoFilter() : PhraseFilter() {

    override val filterLabelsList: Array<FilterLabel> = arrayOf()

    companion object {
        private val logger = LoggerFactory.getLogger(AutoFilter::class.java) as Logger
    }

    private val phraseFiltersList = arrayListOf<FilterData>()

//    init {
//
//        addFilter(PutFilter(variableText))
//        addFilter(InsertFilter(variablePhrases, variableAnswers), true)
//        addFilter(IfElsePreparingFilterV2(), true)
//        addFilter(RandomFilter())
//        addFilter(IntComparingFilter(gameVariables))
//        addFilter(GetBooleanFilter(gameVariables))
//        addFilter(GetVariableFilter(gameVariables))
//        addFilter(filterOnlyPhrases(SetBooleanFilter(gameVariables)))
//        addFilter(filterOnlyPhrases(SetValueFilter(gameVariables)))
//        addFilter(filterOnlyPhrases(IntSimpleArithmeticsFilter(gameVariables)))
//        addFilter(CountFilter()) { CountFilter.isCountLabel(it) }
//        addFilter( IfElseFilterV2())
//    }

    public fun addFilter(filter: PhraseFilter) {
       addFilter(filter, false)
    }

    public fun addFilter(filter: PhraseFilter, isNeedRebuild: Boolean = false) {
        val function = isContains@{ labelText: String ->
            filter.filterLabelsList.forEach { inputLabel ->
                val label = FiltersUtils.parseLabel(labelText) ?: return@forEach
                if (label == inputLabel) return@isContains true;
            }
            return@isContains false
        }
        addFilter(filter,isNeedRebuild, function)
    }

    public fun addFilter(filter: PhraseFilter, isNeedRebuild: Boolean, isContain: (label: String) -> Boolean) {
        phraseFiltersList.add(
            FilterData(
                filter,
                isNeedRebuild,
                isContain
            )
        )
    }
    public fun addFilter(filter: PhraseFilter, isContain: (label: String) -> Boolean) {
        addFilter(filter, false, isContain)
    }


    private fun findFilter(label: String): FilterData? {
        phraseFiltersList.forEach {
            if (it.trigger(label)) return it;
        }
        return null
    }

    override fun filterPhrasesLogic(phrases: Array<String>, count: Int): Array<String> {
        var filteredPhrases = phrases;
        val completedFilters = HashSet<FilterData>()
        var isExit = false;
        while (!isExit) {
            isExit = true;
            logger.info("input phrase: ${phrases.contentToString()}")
            val list = getFilterOrderList(filteredPhrases);
            list.removeAll(completedFilters)
            list.forEach {
                logger.info("-----> call: '${it}'")
                filteredPhrases = it.filter.filterPhrases(filteredPhrases, count);
                logger.info("result : ${filteredPhrases.map { if(it.length > 10)it.substring(IntRange(0,10)) else it }}")
                completedFilters.add(it);
                if (it.isNeedRebuild) {
                    isExit = false;
                    logger.info("can structure changing, rebuild filters list")
                    return@forEach
                }
            }
        }

        return filteredPhrases
    }

    private fun getFilterOrderList(phrases: Array<String>): MutableList<FilterData> {
        val filterList = linkedSetOf<FilterData>()
        phrases.forEach phrasesForEach@{ phrase ->
            val labels = FiltersUtils.getFilterLabelsInsideText(phrase) ?: return@phrasesForEach;
            labels.forEach labelsForEach@{ label ->
                val filter = findFilter(label) ?: return@labelsForEach
                filterList.add(filter)
            }
        }
        logger.info("filters list: ${filterList.map { it.toString() }.toTypedArray().contentToString()}.")
        return filterList.toMutableList();
    }

    override fun filterAnswersLogic(answer: Array<Answer>, count: Int): Array<Answer> {
        var filteredAnswers = answer;
        val completedFilters = HashSet<FilterData>()
        var isExit = false;
        while (!isExit) {
            isExit = true;
            logger.info("input answers: ${answer.contentToString()}")
            val list = getFilterOrderList(filteredAnswers.map { it.text }.toTypedArray());
            list.removeAll(completedFilters)
            list.forEach {
                logger.info("----->  call: '${it}'")
                filteredAnswers = it.filter.filterAnswers(filteredAnswers, count);
                logger.info("result : ${filteredAnswers.contentToString()}")
                completedFilters.add(it);
                if (it.isNeedRebuild) {
                    isExit = false;
                    logger.info("can structure changing, rebuild filters list")
                    return@forEach
                }
            }
        }
        return filteredAnswers
    }

    private data class FilterData(
        val filter: PhraseFilter,
        val isNeedRebuild: Boolean = false,
        val trigger: (label: String) -> Boolean
        )
    {
        override fun toString(): String {
            return filter::class.java.simpleName
        }
    }

}