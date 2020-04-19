package game

import models.Answer
import models.AnswerType
import models.World
import models.items.ADialogItem
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException


open class Runner(private val game: Game, private val world: World): Runnable {

    companion object{
        private val logger = LoggerFactory.getLogger(Runner::class.java) as Logger
    }

    public override fun run(){
        logger.info(">> run")
        run(world.start())
        logger.info("<< run")
    }

    private fun run(startPoint : ADialogItem){
        logger.info(">> run $startPoint")
        var currentPoint =  startPoint
        var answer = Answer.enter()
        loop@ while(true){
            answer = currentPoint.run()
            logger.info("answer from '${currentPoint.id}' is $answer")
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
                val answ = it.startFrom(id) as Answer
                logger.info("<< answer from $id is $answ" )
                teleport(answ.id);
                logger.info("<< goToPhrase $id" )
                return;
            };
        }
        throw IllegalArgumentException("phrase $id not found")
    }

}
