package phrases.filters.Inlinetext

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import phrases.filters.InlineTextPhraseFilter
import phrases.filters.Labels
import tools.FiltersUtils

/**
 *
 * [IF]
 * [GET=bool1][NOT=bool3]
 * [ELSE IF]
 * [key1=v]
 * [ELSE]
 * [SETV=SET]
 * [FI]
 */
class IfElseFilterV2(private val settings: HashMap<String, Any?>) : InlineTextPhraseFilter {


    companion object {
        private val logger = LoggerFactory.getLogger(IfElseFilterV2::class.java) as Logger
    }

    private var startFound = false;
    private var valueReturned = false;

    override fun filterText(itemText: String, count: Int): Boolean {
        val firstLabel = FiltersUtils.getFirstFilterLabel(itemText) ?: return true

        when (Labels.parse(firstLabel)) {
            Labels.IF -> {
                if (startFound) logger.error("FI not found!")
                startFound = true;
                return false
            }
            Labels.FI -> {
                if (!startFound) logger.error("IF not found!")
                startFound = false;
                valueReturned = false;
                return false
            }
            Labels.ELSEIF, Labels.ELSE -> {
                if (startFound) logger.error("IF not found!")
                return false;
            }
            else -> {
                if (startFound) {
                    if (!valueReturned) {
                        valueReturned = true;
                        return true
                    } else {
                        return false;
                    }
                }
            }
        }
        return true
    }
}
