package phrases

import models.Answer
import models.items.phrase.FilteredPhrase
import printer.PrinterFabric

class AutoPhrase(id: String, phrases: Array<String>,  answers: Array<Answer>) : FilteredPhrase(id, phrases, answers){
    init {
        this.phrasePrinter = PrinterFabric.autoPrinter()
        this.addAnswerFilter("chooser", ConditionsFabric.countAnswer)
    }
}