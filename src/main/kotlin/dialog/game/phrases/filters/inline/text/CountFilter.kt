package dialog.game.phrases.filters.inline.text

import dialog.game.phrases.filters.FilterLabel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import dialog.game.phrases.filters.InlineTextPhraseFilter
import dialog.system.models.Answer
import tools.FiltersUtils

/**
[1]
[2]
[3]
[>4]
[<5]
[>=6]
[!6]
[<=6]
[*]
 */
class CountFilter : InlineTextPhraseFilter() {

    override val filterLabelsList: Array<FilterLabel> = arrayOf()

    companion object{
        private val operators: HashMap<String, (labelCnt: Int, currentCnt: Int) -> Boolean> = hashMapOf(
            Pair("!")  {currentCnt, labelCnt  -> labelCnt != currentCnt },
            Pair(">")  {currentCnt, labelCnt  ->  currentCnt > labelCnt },
            Pair("<")  {currentCnt, labelCnt  ->  currentCnt < labelCnt },
            Pair("<=") {currentCnt, labelCnt  ->  currentCnt <= labelCnt },
            Pair(">=") {currentCnt, labelCnt  ->  currentCnt >= labelCnt },
            Pair("*")  {currentCnt, maxCnt ->  currentCnt > maxCnt}
        )
        private val logger = LoggerFactory.getLogger(CountFilter::class.java) as Logger
        private val defaultOperator:(labelCnt: Int, currentCnt: Int) -> Boolean = {labelCnt, currentCnt ->  currentCnt == labelCnt }

        public fun isCountLabel(labelText: String): Boolean{
            val regexNum = "[0-9]+".toRegex()
            val regexOperator = "[^0-9]+".toRegex()

            val number = regexNum.find(labelText)?.value?.toIntOrNull()
            val operatorText = regexOperator.find(labelText)?.value?.trim()

            if(number == null) {
                return operatorText == "*"
            }
            if(operatorText == null) return true
            return operators[operatorText] != null
        }

    }

    private var currentMaxCnt = 0;


    override fun filterText(itemText: String, count: Int): Boolean {
        val labels = FiltersUtils.getFilterLabelsTexts(itemText) ?: return true
        labels.forEach {
            if(!isCountLabel(it)) return@forEach
            if(!countFilter(it, count)) return false }
        return true
    }

    override fun filterPhrasesLogic(phrases: Array<String>, count: Int): Array<String> {
        currentMaxCnt = getMaxCount(phrases)
        return super.filterPhrasesLogic(phrases, count)
    }

    override fun filterAnswersLogic(answer: Array<Answer>, count: Int): Array<Answer> {
        currentMaxCnt = getMaxCount(answer.map { it.text }.toTypedArray())
        return super.filterAnswersLogic(answer, count)
    }


        private fun countFilter(labelText: String, count: Int) : Boolean {

            val regexNum = "[0-9]+".toRegex()
            val regexOperator = "[^0-9]+".toRegex()

            val number = regexNum.find(labelText)?.value?.toIntOrNull()
            val operatorText = regexOperator.find(labelText)?.value?.trim()
            val operator =
                if (operatorText != null && operatorText.isNotEmpty()) {
                    if (operators[operatorText] == null) {
                        logger.error("unrecognised operator $operatorText")
                        defaultOperator
                    } else {
                        operators[operatorText]
                    }
                } else {
                    defaultOperator
                }

            if(operatorText == "*"){
                println("number: $number, currentMaxCnt: $currentMaxCnt")
                return operator!!.invoke(count, currentMaxCnt)
            }

            if(number != null) return operator!!.invoke(count, number)

            return true
        }

        private fun getMaxCount(itemTexts : Array<String>) : Int{
            var maxCnt = 0;
            for (phrase in itemTexts) {
                val filterLabel = FiltersUtils.getFirstFilterLabelText(phrase);
                if (filterLabel != null) {
                    val number = filterLabel.toIntOrNull() ?: continue
                    if (number > maxCnt) maxCnt = number
                }
            }
            return maxCnt;
        }
}