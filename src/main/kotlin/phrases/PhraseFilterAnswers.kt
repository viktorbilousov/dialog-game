package phrases

import models.Answer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PhraseFilterAnswers (id: String, phrases: Array<String>, answers : Array<Answer> ) : GamePhrase(id, phrases, answers ) {

    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger

    }


    private var filter : (answers: Array<Answer>, phrase: GamePhrase) -> Array<Answer> = {
            answers: Array<Answer>, _: GamePhrase ->
        answers
    }


    public fun setFilter(filter :  (answers :Array<Answer>, phrase: GamePhrase)-> Array<Answer>){
        this.filter = filter;
    }

    override fun body(inputAnswer: Answer): Answer {
        logger.info(">> body $id: inputAnswer=$inputAnswer")
        val filtredAnswers = filter(answers.clone(),  this as GamePhrase)
        val res = tools.PhrasePrinter.printTextDialog(phrases[0], filtredAnswers);
        logger.info("<< body $id: outputAnswer=$res")
        return res;
    }


}

