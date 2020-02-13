package phrases

import models.Answer
import models.items.DialogItem
import models.items.phrase.Phrase
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class GamePhrase(id: String, phrases: Array<String>, answers : Array<Answer> ) : Phrase(id, phrases, answers ) {


    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger

    }

    public val properties = HashMap<String, Any?>()
    public var count = 0
    private set;

    init {
        this.setBeforeFun {
            count++;
            logger.info("phrase $id count=$count")
        }
    }

}