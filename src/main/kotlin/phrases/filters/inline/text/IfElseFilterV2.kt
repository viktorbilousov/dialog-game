package phrases.filters.inline.text

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import phrases.filters.InlineTextPhraseFilter
import phrases.filters.FilterLabel
import tools.FiltersUtils

/**
 *
 * [IF_SYS]
 * [GET=bool1][NOT=bool3]
 * [ELSEIF_SYS]
 * [key1=v]
 * [ELSE_SYS]
 * [SETV=SET]
 * [FI_SYS]
 */
class IfElseFilterV2() : InlineTextPhraseFilter {


    companion object {
        private val logger = LoggerFactory.getLogger(IfElseFilterV2::class.java) as Logger
    }

    private var startFound = false;
    private var valueReturned = false;

    override fun filterText(itemText: String, count: Int): Boolean {
        val firstLabel = FiltersUtils.getFirstFilterLabelText(itemText) ?: itemText

        when (FilterLabel.parse(firstLabel)) {
            FilterLabel.IF_SYS -> {
                if (startFound) logger.error("FI not found!")
                startFound = true;
                return false
            }
            FilterLabel.FI_SYS -> {
                if (!startFound) logger.error("IF not found!")
                startFound = false;
                valueReturned = false;
                return false
            }
            FilterLabel.ELSEIF_SYS, FilterLabel.ELSE_SYS -> {
                if (!startFound) logger.error("IF not found!")
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
