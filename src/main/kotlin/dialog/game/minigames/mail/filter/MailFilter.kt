package dialog.game.minigames.mail.filter

import dialog.game.game.Loader
import dialog.game.minigames.mail.models.Mail
import dialog.game.phrases.filters.FilterLabel
import dialog.game.phrases.filters.InlineChangeTextPhraseFilter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tools.FiltersUtils

/**
 * [MAIL=SHOW_ALL]
 * [MAIL=HIDE_ALL]
 * [MAIL=SHOW_NUMBERS_AND_SENDER_NAME]
 * [MAIL=SHOW_NUMBERS]
 */

class MailFilter : InlineChangeTextPhraseFilter() {

    companion object {
        private val logger = LoggerFactory.getLogger(MailFilter::class.java) as Logger
    }

    override val filterLabelsList: Array<FilterLabel> = arrayOf(FilterLabel.MAIL)

    override fun changeText(itemText: String, count: Int): String {
        val labelRaw = FiltersUtils.getFirstFilterLabelText(itemText) ?: return itemText
        val label = FiltersUtils.parseLabel(labelRaw) ?: return itemText
        val value = FiltersUtils.getParameterValue(labelRaw) ?: return itemText

        if (label != FilterLabel.MAIL) return itemText;

        val mailType = Mail.MailType.parse(value);
        if (mailType == null) {
            logger.error("mail type not recoqnised: $value")
            return itemText;
        }

        return Mail.create(mailType)

    }
}