package printer

import models.Answer
import models.items.phrase.PhrasePrinter

class PrinterFabric {
    companion object{
        public fun autoPrinter() : PhrasePrinter{
            return AutoPrinter()
        }
    }
    class AutoPrinter : PhrasePrinter{
        override fun printTextDialog(text: Array<String>, answer: Array<Answer>): Answer {
            return answer[0]
        }
    }
}