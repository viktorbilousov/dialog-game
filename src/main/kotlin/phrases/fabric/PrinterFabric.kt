package phrases.fabric

import models.Answer
import models.items.phrase.PhrasePrinter
import tools.AnswersTool
import tools.SimplePhrasePrinter
import java.lang.StringBuilder

class PrinterFabric {
    companion object {

        private val defPrinter = SimplePhrasePrinter()

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

    }
}
