package phrases.filters.phrase

import game.GameData
import models.Answer
import phrases.filters.InlinePhraseFilter
import phrases.filters.Inlinetext.GetVariableFilter
import phrases.filters.Inlinetext.IfElseFilterV2
import phrases.filters.Inlinetext.IntComparingFilter
import phrases.filters.Inlinetext.ParamGetBooleanFilter
import phrases.filters.Labels
import phrases.filters.PhraseFilter
import tools.FiltersUtils

class AutoFilter : PhraseFilter {
    //  private val inlineFilterList = arrayListOf<InlinePhraseFilter>()

    private val phraseFilterMap = mutableMapOf<PhraseFilter, (label: String) -> Boolean>()

    init {
        addFilter(IntComparingFilter(GameData.gameVariables), Labels.INT)
        addFilter(ParamGetBooleanFilter(GameData.gameVariables), Labels.GET, Labels.NOT)
        addFilter(IfElseFilterV2(GameData.gameVariables), Labels.IF, Labels.ELSE, Labels.ELSEIF, Labels.FI)
        addFilter(GetVariableFilter(GameData.gameVariables), Labels.GETV, Labels.NOTV)
        addFilter(IntComparingFilter(GameData.gameVariables), Labels.INT)
        addFilter(CountFilter()){label_ ->
            var label = label_;
            if (label.startsWith("!")) label = label.removePrefix("!")
            else if (label == "*")  return@addFilter true;
            return@addFilter label.toIntOrNull() != null
        }
    }

    public fun addFilter(filter: PhraseFilter, vararg labels: Labels) {
        val function = isContains@{ label: String ->
            labels.forEach {
                if (label.toUpperCase()
                        .contains(it.label.toUpperCase())
                ) return@isContains true;
            }
            return@isContains false
        }
        addFilter(filter, function)
    }

    public fun addFilter(filter: PhraseFilter, isContain: (label: String) -> Boolean) {
        phraseFilterMap[filter] = isContain
    }


    private fun findFilter(label: String): PhraseFilter? {
        phraseFilterMap.forEach {
            if (it.value(label)) return it.key;
        }
        return null
    }

    override fun filterPhrases(phrases: Array<String>, count: Int): Array<String> {
        var filteredPhrases = phrases;
        val completedFilters = HashSet<PhraseFilter>()
        var isExit = false;
        while (!isExit) {
            isExit = true;
            val list = getFilterOrderList(filteredPhrases);
            list.removeAll(completedFilters)
            list.forEach {
                filteredPhrases = it.filterPhrases(filteredPhrases, count);
                completedFilters.add(it);
                if (it !is InlinePhraseFilter) {
                    isExit = false;
                    return@forEach
                }
            }
        }

        return filteredPhrases
    }

    private fun getFilterOrderList(phrases: Array<String>): MutableList<PhraseFilter> {
        val filterList = linkedSetOf<PhraseFilter>()
        phrases.forEach phrasesForEach@{ phrase ->
            val labels = FiltersUtils.getFilterLabels(phrase) ?: return@phrasesForEach;
            labels.forEach labelsForEach@{ label ->
                val filter = findFilter(label) ?: return@labelsForEach
                filterList.add(filter)
            }
        }
        return filterList.toMutableList();
    }

    override fun filterAnswers(answer: Array<Answer>, count: Int): Array<Answer> {
        var filteredAnswers = answer;
        val completedFilters = HashSet<PhraseFilter>()
        var isExit = false;
        while (!isExit) {
            isExit = true;
            val list = getFilterOrderList(filteredAnswers.map { it.text }.toTypedArray());
            list.removeAll(completedFilters)
            list.forEach {
                filteredAnswers = it.filterAnswers(filteredAnswers, count);
                completedFilters.add(it);
                if (it !is InlinePhraseFilter) {
                    isExit = false;
                    return@forEach
                }
            }
        }

        return filteredAnswers
    }

    private fun isHaveConditionsLabel(): Boolean {
        return false
    }

    private fun isHaveMultiplyBrackets(): Boolean {
        return false
    }

}