package dialog.game.phrases.collections


import dialog.game.phrases.filters.inline.change.RemoveLabelFilter
import dialog.system.models.answer.Answer
import dialog.system.models.items.phrase.PhrasePrinter
import dialog.system.tools.AnswersTool
import dialog.system.tools.SimplePhrasePrinter
import java.lang.StringBuilder

class PrinterCollection {
    companion object {

        public val defPrinter = printerWithLine()

        public fun defTextPrinter(text: String) { println(text + "\n\n\n")}

        public fun defAnswersPrinter(answer: Array<Answer>) {
            val builder = StringBuilder("")
            for(i in answer.indices) {
                builder.append("[${i + 1}] ${answer[i].text}\n")
            }
            println(builder.toString())
        }

        public fun empty() = object : PhrasePrinter {
            override fun printTextDialog(text: String, answer: Array<Answer>) {
            }
        }

        public fun hideLabels() = object : PhrasePrinter {
            override fun printTextDialog(text: String, answer: Array<Answer>) {
                val copy = AnswersTool.copyArrayOrAnswers(answer);
                val answersFiltered = RemoveLabelFilter()
                    .filterAnswers(copy, 0);
                defPrinter.printTextDialog(text, answersFiltered);
            }
        }

        public fun printerWithLine() = object : PhrasePrinter{
            override fun printTextDialog(text: String, answer: Array<Answer>) {
                println("-----------------------------------------------------------\n")
                SimplePhrasePrinter().printTextDialog(text, answer)
                println("\n-------------------------------------------------------")
            }
        }

    }
}
