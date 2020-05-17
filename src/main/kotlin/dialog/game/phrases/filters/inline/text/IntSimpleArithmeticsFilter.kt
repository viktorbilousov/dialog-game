package dialog.game.phrases.filters.inline.text

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import dialog.game.phrases.filters.InlineTextPhraseFilter
import dialog.game.phrases.filters.FilterLabel
import tools.FiltersUtils
import java.lang.IllegalArgumentException

/**
 * [SETI][key+=1]
 * [SETI][key*=2]
 * [SETI][key/=2]
 * [SETI][key-=3]
 * [SETI][key++]
 * [SETI][key--]
 * [SETI][key%=5]
 * supported operators:++, --, -=, +=, /=, *=. %=
 */
class IntSimpleArithmeticsFilter(private val parameters: HashMap<String, Any?> ) :
    InlineTextPhraseFilter() {

    override val filterLabelsList: Array<FilterLabel> = arrayOf(FilterLabel.SETI)

    companion object {
        private val operators: HashMap<String, (a: Int, b: Int) -> Int> = hashMapOf(
            Pair("=", { left, right -> right }),
            Pair("+=", { left, right -> left + right }),
            Pair("*=", { left, right -> left * right }),
            Pair("-=", { left, right -> left - right }),
            Pair("/=", { left, right -> left / right }),
            Pair("%=", { left, right -> left % right }),
            Pair("--", { left, _ -> left  - 1    }),
            Pair("++", { left, _ -> left + 1     })
        )

        private val logger = LoggerFactory.getLogger(GetBooleanFilter::class.java) as Logger
    }

    override fun filterText(itemText: String, count: Int): Boolean {
        val labels = FiltersUtils.getFilterLabelsTexts(itemText) ?: return true;
        labels.forEachIndexed(){ i, it ->
            val label = FilterLabel.parse(it) ?: return@forEachIndexed
            if(label == FilterLabel.SETI) {
                val keysLabel = labels[i+1];
                processArithmOperation(keysLabel)
            }
        }
        return true
    }

    private fun processArithmOperation(valueLabel: String) {

        val operatorKey = findOperator(valueLabel)
            ?: throw IllegalArgumentException("Operator not recognised in label $valueLabel")
        val operator: (Int, Int) -> Int = operators[operatorKey]!!
        val array = valueLabel.split(operatorKey)

        val variable = array[0]
        val variablesValue = parameters[array[0]].toString().toIntOrNull()  ?: throw IllegalArgumentException("variable $variable is null")
        var rightValue = 0;
        if(operatorKey != "--" && operatorKey !="++") {
            rightValue = array[1].toInt()
        }

        val res =  operator(variablesValue, rightValue)

        logger.info("$valueLabel: $variable , Operator: $operatorKey : ${parameters[variable]}  $res")
        parameters[variable] = res;

    }


    private fun findOperator(line: String) : String?{
       return operators.keys.filter { line.contains(it)}.maxBy { it.length } ?: return null
    }


}