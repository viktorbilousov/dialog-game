package dialog.game.phrases.collections


import dialog.game.debug.record.models.Record
import dialog.game.debug.record.service.RecordPlayer
import dialog.game.phrases.filters.inline.text.CountFilter
import dialog.system.models.Answer
import dialog.system.models.items.phrase.AnswerChooser
import dialog.system.tools.ConsoleAnswerChooser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException

class AnswerChooserCollection {
    companion object{
        public fun first() : AnswerChooser {
            return object : AnswerChooser {
                override fun chooseAnswer(answers: Array<Answer>): Answer {
                    val logger = LoggerFactory.getLogger("FirstAnswerChooser") as Logger
                    logger.info(">> choose [${answers[0]}")
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


        public fun autoPlayer(recordPlayer: RecordPlayer) : AnswerChooser{
            return object : AnswerChooser{
                override fun chooseAnswer(answers: Array<Answer>): Answer {
                    if(recordPlayer.haveNext()) {
                        val id = recordPlayer.getNextStep();
                        for (answer in answers) {
                            if (answer.id == id) return answer;
                        }
                        throw IllegalArgumentException("answer $id not exit")
                    }else{
                        return console().chooseAnswer(answers)
                    }
                }
            }
        }

        public fun console(): AnswerChooser{ return ConsoleAnswerChooser()
        }

    }
}