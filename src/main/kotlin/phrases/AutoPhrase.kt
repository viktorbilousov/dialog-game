package phrases

import models.Answer
import models.items.phrase.FilteredPhrase
import phrases.fabric.AnswerChooserFabric
import phrases.fabric.FiltersFabric
import phrases.fabric.PrinterFabric

class AutoPhrase(id: String, phrases: Array<String>,  answers: Array<Answer>) : CountPhrase(id, phrases, answers){
    init {
        this.answerChooser = AnswerChooserFabric.auto()
    }
}