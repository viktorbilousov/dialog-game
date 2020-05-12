package dialog.game.minigames.mail.models

import java.lang.IllegalArgumentException


class Mail {

    companion object {
        fun create(mailType: MailType): String {
            if (textsMap[mailType] == null) {
                throw IllegalArgumentException("Mail $mailType not exist!")
            }
            return createMailString(textsMap[mailType]!!)
        }

        private const val MAIL_WIDTH_SIZE = 50
        private const val MAIL_HEIGHT_SIZE = 10


        private val textsMap = hashMapOf<MailType, MailText>(
            Pair(MailType.SHOW_ALL,
                MailText(
                    arrayOf("Марина Ткач", "Agnesstraße 35", "80798 Звероленд"),
                    arrayOf("Крокор Крокусов", "ул. Карла 86а", "Дождиленд, 93051")
                )),
            Pair(MailType.SHOW_NUMBERS, MailText(
                arrayOf("SLkdaj2 @el2", "fsldfpso3% 35", "!@d;skd)@(, 80798"),
                arrayOf("flkjs SLkdfjsl", "fdslkj flskd 86a", "fsdkfsd, 93051")
                )),
            Pair(MailType.SHOW_NUMBERS_AND_SENDER_NAME, MailText(
                arrayOf("SLkdaj2 @el2", "fsldfpso3% 35", "!@d;skd)@(, 80798"),
                        arrayOf("Какой-то крокодил", "fdslkj flskd 86a", "fsdkfsd, 93051")

            )),
            Pair(MailType.HIDE_ALL, MailText(
                arrayOf("SLkdaj2 @el2", "fsldfpso3% @#", "sad@#! !@d;skd)@("),
                arrayOf("flkjs SLkdfjsl", "fdslkj flskd sd;", "fsdkfsd, dlkfjs")
            ))
        )

        private fun init(brief: Array<String?>){

            for (i in brief.indices) {
                brief[i] = "";
                for (j in 0 until MAIL_WIDTH_SIZE) {
                    if (j == 0) {
                        brief[i] = "|"
                    } else if (j == MAIL_WIDTH_SIZE - 1 || (j == 38 || j == 46) && i >= 1 && i <= 3) {
                        brief[i] += "|"
                    } else if (i == 0 || i == brief.size - 1 || (i == 1 || i == 3) && j > 38 && j < 46) {
                        brief[i] += "-"
                    } else {
                        brief[i] += " "
                    }
                }
            }
        }

        private fun createMailString(texts: MailText): String {
            val brief =  arrayOfNulls<String>(MAIL_HEIGHT_SIZE)
            val sender = texts.sender;
            val recipient = texts.reciever;
            init(brief)
            val indentationSpaces = 3
            for (i in sender.indices) {
                brief[i + 1] = brief[i + 1]!!
                    .replaceRange(indentationSpaces, indentationSpaces + sender[i].length, sender[i])
            }
            for (i in recipient.indices) {
                brief[8 - i] = brief[8 - i]!!
                    .replaceRange(MAIL_WIDTH_SIZE - indentationSpaces - recipient[recipient.size - i - 1].length, MAIL_WIDTH_SIZE - indentationSpaces , recipient[recipient.size - i - 1])
            }
            val word = "!!!!СРОЧНО!!!!"
            brief[7] =  brief[7]!!.replaceRange(indentationSpaces, indentationSpaces + word.length, word)

            return brief.joinToString("\n");
        }
    }

    enum class MailType {
        SHOW_ALL,
        HIDE_ALL,
        SHOW_NUMBERS_AND_SENDER_NAME,
        SHOW_NUMBERS;

        companion object {
            public fun parse(str: String): MailType? {
                MailType.values().forEach {
                    if (str == it.name) return it;
                }
                return null;
            }
        }
    }

   private data class MailText(public var reciever: Array<String>, public var sender: Array<String>);
}


