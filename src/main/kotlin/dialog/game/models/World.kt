package dialog.game.models

import dialog.system.models.Answer
import dialog.system.models.items.dialog.ADialog
import dialog.system.models.router.Router
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class World(public val worldRouter: Router) {

    companion object{
        private val logger = LoggerFactory.getLogger(World::class.java) as Logger

    }

    private val way = ArrayList<String>()

    fun start() : ADialog {
        logger.info(">> start ")
        way.add(worldRouter.startPoint.id);
        logger.info("added ${worldRouter.startPoint.id}, way: ${way.toTypedArray().contentToString()}")
        logger.info("<< start return : ${worldRouter.startPoint}")
        return worldRouter.startPoint as ADialog
    }

    fun enterTo(answer: Answer) : ADialog {
        logger.info(">> enterTo $answer ")
        val res = worldRouter.getNext(answer)
        way.add(res.id);
        logger.info("added ${res.id}, way: ${way.toTypedArray().contentToString()}")
        logger.info("<< enterTo return: ${res.id}")
        return res as ADialog;
    }

    fun exit() : ADialog {
        logger.info(">> exit ")
         way.removeAt(way.lastIndex)
        logger.info("removed ${way.last()}, way: ${way.toTypedArray().contentToString()}")
        val res = worldRouter.getNext(Answer.enter(way.last()));
        logger.info("<< exit : return ${res.id}")
        return res as ADialog;
    }

    fun isRoot(): Boolean {
        return way.size == 1;
    }


}