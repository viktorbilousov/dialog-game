package phrases.collections

import models.Answer
import models.items.phrase.AnswerChooser
import tools.ConsoleAnswerChooser
import java.lang.IllegalArgumentException

class AnswerChooserCollection {
    companion object{
        public fun first() : AnswerChooser {
            return object : AnswerChooser {
                override fun chooseAnswer(answers: Array<Answer>): Answer {
                    return answers[0];
                }
            }
        }

        public fun auto(index: Int) : AnswerChooser{
            return object : AnswerChooser{
                override fun chooseAnswer(answers: Array<Answer>): Answer {
                    return answers[index]
                }
            }
        }

        public fun auto(id: String) : AnswerChooser{
            return object : AnswerChooser{
                override fun chooseAnswer(answers: Array<Answer>): Answer {
                    for (answer in answers) {
                        if(answer.id == id) return answer;
                    }
                    throw IllegalArgumentException("answer $id not exit")
                }
            }
        }

        public fun console(): AnswerChooser{ return ConsoleAnswerChooser()}

    }
}