package phrases.collections

import models.Answer
import models.items.phrase.PhrasePrinter
import tools.AnswersTool
import tools.SimplePhrasePrinter
import java.lang.StringBuilder

class PrinterCollection {
    companion object {

        public val defPrinter = SimplePhrasePrinter()

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

        public fun parametric() = object : PhrasePrinter {
            override fun printTextDialog(text: String, answer: Array<Answer>) {
                val copy = AnswersTool.copyArrayOrAnswers(answer);
                val answersFiltered = FiltersCollection.removeLabelAnswersFilter.invoke(copy, 0);
                defPrinter.printTextDialog(text, answersFiltered);
            }
        }
       /* public fun debug(debuggedPrinter: PhrasePrinter, isRecorded: Boolean, lastAnswer: Answer) = object : PhrasePrinter {

            private val startRecordAnswer = Answer("debug.start_record", "debug.start_record")
            private val stopRecordAnswer = Answer("debug.stop_record", "debug.stop_record")

            override fun printTextDialog(text: String, answer: Array<Answer>) {
              //  val newAnswers = answer.toMutableList().add()
                val copy = AnswersTool.copyArrayOrAnswers(answer);
                val answersFiltered = FiltersCollection.removeLabelAnswersFilter.invoke(copy, 0);
                defPrinter.printTextDialog(text, answersFiltered);
            }
        }*/

    }
}
