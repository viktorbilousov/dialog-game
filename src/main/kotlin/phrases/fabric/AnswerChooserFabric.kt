package phrases.fabric

import models.Answer
import models.items.phrase.AnswerChooser
import tools.ConsoleAnswerChooser

class AnswerChooserFabric {
    companion object{
        public fun auto() : AnswerChooser {
            return object : AnswerChooser {
                override fun chooseAnswer(answers: Array<Answer>): Answer {
                    return answers[0];
                }
            }
        }
    }
}