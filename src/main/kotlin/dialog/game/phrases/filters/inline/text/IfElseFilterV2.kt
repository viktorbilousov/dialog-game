package dialog.game.phrases.filters.inline.text

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import dialog.game.phrases.filters.InlineTextPhraseFilter
import dialog.game.phrases.filters.FilterLabel
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
class IfElseFilterV2() : InlineTextPhraseFilter() {

    override val filterLabelsList: Array<FilterLabel> = arrayOf(FilterLabel.IF_SYS, FilterLabel.ELSEIF_SYS, FilterLabel.ELSE_SYS, FilterLabel.FI_SYS)

    companion object {
        private val logger = LoggerFactory.getLogger(IfElseFilterV2::class.java) as Logger
    }

    private var startFound = false;
    private var isReturnBlockFound = false;
    private var isReturnBlockEnded = false;

    override fun filterText(itemText: String, count: Int): Boolean {
        val firstLabel = FiltersUtils.getFirstFilterLabelText(itemText) ?: itemText

        when (FilterLabel.parse(firstLabel)) {
            FilterLabel.IF_SYS -> {
                if (startFound) logger.error("FI not found!")
                isReturnBlockEnded = false;
                isReturnBlockFound = false;
                startFound = true
                logger.info("FALSE: $itemText")
                return false
            }
            FilterLabel.FI_SYS -> {
                if (!startFound) logger.error("IF not found!")
                isReturnBlockEnded = true;
                isReturnBlockFound = false;
                startFound = false
                logger.info("FALSE: $itemText")
                return false
            }
            FilterLabel.ELSEIF_SYS, FilterLabel.ELSE_SYS -> {
                if (!startFound) logger.error("IF not found!")
                if(isReturnBlockFound) isReturnBlockEnded = true;
                logger.info("FALSE: $itemText")
                return false;
            }
            else -> {
                if(startFound){
                    if (!isReturnBlockEnded) {
                        isReturnBlockFound = true;
                        logger.info("TRUE: $itemText")
                        return true
                    }
                    else {
                        logger.info("FALSE: $itemText")
                        return false
                    }
                }
                else {
                    logger.info("TRUE: $itemText")
                    return true
                }
            }
        }
    }
}
