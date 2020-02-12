package game

import models.Answer
import models.AnswerType
import models.World
import models.items.DialogItem
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException


class Runner(private val game: Game, private val world: World) {

    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }

    private fun run(){
        logger.info(">> run")
        run(world.start())
        logger.info("<< run")
    }

    private fun run(startPoint : DialogItem){
        logger.info(">> run $startPoint")
        var currentPoint =  startPoint
        var answer = Answer.enter()
        loop@ while(true){
            answer = currentPoint.run(answer)
            logger.error("answer from $currentPoint is $answer")
            currentPoint =  when(answer.type){
                AnswerType.ENTER -> world.enterTo(answer)
                AnswerType.EXIT -> {

                    if(world.isRoot()) {
                        logger.error("exit from root")
                        break@loop;
                    };
                    world.exit()
                };
                else -> {
                    logger.error("incorrect answer type $answer")
                    throw IllegalArgumentException("incorrect answer type")
                }
            }
        }
        logger.info("<< run $startPoint")
    }


    public fun teleport(id: String) {
        logger.info(">> teleport $id" )
        val res = world.worldRouter.goTo(id) ?: throw IllegalArgumentException("dialog(room) $id not found");
        run(res)
        logger.info("<< teleport $id")

    }

    public fun goToPhrase(id: String){
        logger.info(">> goToPhrase $id" )

        for (it in game.dialogs.values) {
            if(it.containsItem(id)) {
                val answ = it.startFrom(id, Answer.enter()) as Answer
                logger.info("<< answer from $id is $answ" )
                teleport(answ.id);
                logger.info("<< goToPhrase $id" )
                return;
            };
        }
        throw IllegalArgumentException("phrase $id not found")
    }

}
