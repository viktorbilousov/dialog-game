package dialog.game.phrases.filters.inline.text

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import dialog.game.phrases.filters.InlineTextPhraseFilter
import dialog.game.phrases.filters.FilterLabel
import tools.FiltersUtils
import java.lang.IllegalArgumentException

/**
 * [INT][key1>key2][INT][key>key2]
 * [INT][key1!=-10]
 * supported operators: >, <, >=, <=, ==, !=
 */
class IntComparingFilter(private val parameters: HashMap<String, Any?> ) :
    InlineTextPhraseFilter() {

    override val filterLabelsList: Array<FilterLabel> = arrayOf(FilterLabel.INT)

    companion object {
        private val operators: HashMap<String, (a: Int, b: Int) -> Boolean> = hashMapOf(
            Pair(">", { left, right -> left > right }),
            Pair("<", { left, right -> left < right }),
            Pair(">=", { left, right -> left >= right }),
            Pair("<=", { left, right -> left <= right }),
            Pair("==", { left, right -> left == right }),
            Pair("!=", { left, right -> left != right }),
            Pair("=", { left, right -> left == right })
        )

        private val logger = LoggerFactory.getLogger(GetBooleanFilter::class.java) as Logger
    }

    override fun filterText(itemText: String, count: Int): Boolean {
        val labels = FiltersUtils.getFilterLabelsTexts(itemText) ?: return true;
        labels.forEachIndexed(){ i, it ->
            val label = FilterLabel.parse(it) ?: return@forEachIndexed
            if(label == FilterLabel.INT) {
                val keysLabel = labels[i+1];
                if(!processInt(keysLabel)) return false
            }
        }
        return true
    }

    public fun processInt(valueLabel: String): Boolean {

        val operatorKey = findOperator(valueLabel)
            ?: throw IllegalArgumentException("Operator not recognised in label $valueLabel")
        val operator: (Int, Int) -> Boolean = operators[operatorKey]!!
        val array = valueLabel.split(operatorKey)

        val leftValue = getValue(array[0])  ?: return false
        val rightValue = getValue(array[1]) ?: return false

        return operator(leftValue, rightValue)
    }

    private fun getValue(value: String): Int? {
        if (parameters[value].toString().toIntOrNull() != null)
            return parameters[value].toString().toIntOrNull()

        if (value.toIntOrNull() != null) return value.toIntOrNull();

        return null
    }

    private fun findOperator(line: String) : String?{
       return operators.keys.filter { line.contains(it)}.maxBy { it.length } ?: return null
    }


}