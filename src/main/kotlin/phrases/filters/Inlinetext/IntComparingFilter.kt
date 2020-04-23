package phrases.filters.Inlinetext

import models.Answer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import phrases.filters.InlineTextPhraseFilter
import phrases.filters.Labels
import tools.FiltersUtils
import java.lang.IllegalArgumentException

/**
 * [INT][key1>key2][INT][key>key2]
 * [INT][key1!=-10]
 * supported operators: >, <, >=, <=, ==, !=
 */
class IntComparingFilter(private val parameters: HashMap<String, Any?> ) :
    InlineTextPhraseFilter {

    companion object {
        private val operators: HashMap<String, (a: Int, b: Int) -> Boolean> = hashMapOf(
            Pair(">", { left, right -> left > right }),
            Pair("<", { left, right -> left < right }),
            Pair(">=", { left, right -> left >= right }),
            Pair("<=", { left, right -> left <= right }),
            Pair("==", { left, right -> left == right }),
            Pair("!=", { left, right -> left != right })
        )

        private val logger = LoggerFactory.getLogger(ParamGetBooleanFilter::class.java) as Logger
    }

    override fun filterText(itemText: String, count: Int): Boolean {
        val labels = FiltersUtils.getFilterLabels(itemText) ?: return true;
        labels.forEachIndexed(){ i, it ->
            val label = Labels.parse(it) ?: return@forEachIndexed
            if(label == Labels.INT) {
                val keysLabel = labels[i+1];
                if(!processInt(keysLabel)) return false
            }
        }
        return true
    }

    public fun processInt(valueLabel: String): Boolean {

        var array: Array<String>? = null
        var operator: (Int, Int) -> Boolean =
            { a, b -> throw IllegalArgumentException("Operator not recognised in label $valueLabel") }
        for (op in operators) {
            if (valueLabel.split(op.key).size == 2) {
                operator = op.value;
                array = valueLabel.split(op.key).toTypedArray()
                break
            }
        }
        if (array == null) {
            throw IllegalArgumentException("Operator not recognised in label $valueLabel")
        }

        val leftValue = getValue(array[0]) ?: throw IllegalArgumentException("LeftValue not recognised ${array[0]}")
        val rightValue = getValue(array[1]) ?: throw IllegalArgumentException("RightValue not recognised  ${array[1]}")

        return operator(leftValue, rightValue)
    }

    private fun getValue(value: String): Int? {
        if (parameters[value].toString().toIntOrNull() != null)
            return parameters[value].toString().toIntOrNull()

        if (value.toIntOrNull() != null) return value.toIntOrNull();

        return null
    }

}