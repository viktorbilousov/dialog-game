package phrases.fabric

import models.Answer
import models.items.phrase.PhrasePrinter
import tools.AnswersTool
import tools.SimplePhrasePrinter

class PrinterFabric {
    companion object {

        private val defPrinter = SimplePhrasePrinter()

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
