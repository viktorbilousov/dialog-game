package phrases.filters.phrase

import game.GameData
import models.Answer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import phrases.filters.FilterLabel
import phrases.filters.PhraseFilter
import phrases.filters.inline.change.PutFilter
import phrases.filters.inline.text.*
import tools.FiltersUtils

class AutoFilter(
    variableText: HashMap<String, () -> String> = GameData.variableTexts,
    variablePhrases: HashMap<String, () -> Array<String>> = GameData.variablePhrases,
    variableAnswers: HashMap<String, () -> Array<Answer>> = GameData.variableAnswers,
    gameVariables: HashMap<String, Any?> = GameData.gameVariables
) : PhraseFilter {

    companion object {
        private val logger = LoggerFactory.getLogger(AutoFilter::class.java) as Logger
    }

    private val phraseFiltersList = arrayListOf<FilterData>()

    init {
        addFilter(PutFilter(variableText), FilterLabel.PUT)
        addFilter(InsertFilter(variablePhrases, variableAnswers), true, FilterLabel.INST)
        addFilter(IfElsePreparingFilter(), true, FilterLabel.IF, FilterLabel.ELSE, FilterLabel.ELSEIF, FilterLabel.FI)
        addFilter(IntComparingFilter(gameVariables), FilterLabel.INT)
        addFilter(GetBooleanFilter(gameVariables), FilterLabel.GET, FilterLabel.NOT)
        addFilter(GetVariableFilter(gameVariables), FilterLabel.GETV, FilterLabel.NOTV)
        addFilter(CountFilter()) { CountFilter.isCountLabel(it) }
        addFilter(
            IfElseFilterV2(),
            FilterLabel.IF_SYS,
            FilterLabel.ELSE_SYS,
            FilterLabel.ELSEIF_SYS,
            FilterLabel.FI_SYS
        )
    }

    public fun addFilter(filter: PhraseFilter, vararg labels: FilterLabel) {
       addFilter(filter, false, *labels)
    }

    public fun addFilter(filter: PhraseFilter, isNeedRebuild: Boolean, vararg labels: FilterLabel) {
        val function = isContains@{ labelText: String ->
            labels.forEach {inputLabel ->
                val label = FiltersUtils.parseLabel(labelText) ?: return@forEach
                if (label == inputLabel) return@isContains true;
            }
            return@isContains false
        }
        addFilter(filter,isNeedRebuild, function)
    }

    public fun addFilter(filter: PhraseFilter, isNeedRebuild: Boolean, isContain: (label: String) -> Boolean) {
        phraseFiltersList.add(FilterData(filter, isNeedRebuild, isContain))
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

    override fun filterPhrases(phrases: Array<String>, count: Int): Array<String> {
        var filteredPhrases = phrases;
        val completedFilters = HashSet<FilterData>()
        var isExit = false;
        while (!isExit) {
            isExit = true;
            val list = getFilterOrderList(filteredPhrases);
            list.removeAll(completedFilters)
            list.forEach {
                logger.info("-----> call filter: ${it}")
                filteredPhrases = it.filter.filterPhrases(filteredPhrases, count);
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
            val labels = FiltersUtils.getFilterLabelsTexts(phrase) ?: return@phrasesForEach;
            labels.forEach labelsForEach@{ label ->
                val filter = findFilter(label) ?: return@labelsForEach
                filterList.add(filter)
            }
        }
        logger.info("filters list: ${filterList.map { it.toString() }.toTypedArray().contentToString()}.")
        return filterList.toMutableList();
    }

    override fun filterAnswers(answer: Array<Answer>, count: Int): Array<Answer> {
        var filteredAnswers = answer;
        val completedFilters = HashSet<FilterData>()
        var isExit = false;
        while (!isExit) {
            isExit = true;
            val list = getFilterOrderList(filteredAnswers.map { it.text }.toTypedArray());
            list.removeAll(completedFilters)
            list.forEach {
                logger.info("----->  call filter: ${it}")
                filteredAnswers = it.filter.filterAnswers(filteredAnswers, count);
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