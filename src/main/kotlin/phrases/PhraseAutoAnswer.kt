package phrases

import models.Answer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException

class PhraseAutoAnswer (id: String, phrases: Array<String>, answers : Array<Answer> ) : GamePhrase(id, phrases, answers ) {

    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }

    private var selecter : (answers: Array<Answer>, phrase: GamePhrase) -> Answer = {
            _: Array<Answer>, _: GamePhrase ->
        logger.error("selecter of $id not set")
        throw IllegalArgumentException("selecter of $id not set")
    }

    override fun body(inputAnswer: Answer): Answer {
        logger.info(">> body $id: inputAnswer=$inputAnswer")
        val res = selecter(answers.clone(),  this as GamePhrase)
        logger.info("<< body $id: outputAnswer=$res")
        return res;
    }
}
