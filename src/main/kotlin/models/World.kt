package models

import game.Game
import models.items.DialogItem
import models.items.dialog.Dialog
import models.router.Router
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.IllegalArgumentException

class World(public val worldRouter: Router) {

    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger

    }

    private val way = ArrayList<String>()

    fun start() : Dialog {
        logger.info(">> start ")
        way.add(worldRouter.startPoint.id);
        logger.info("added ${worldRouter.startPoint.id}, way: ${way.toTypedArray().contentToString()}")
        logger.info("<< start return : ${worldRouter.startPoint}")
        return worldRouter.startPoint as Dialog
    }

    fun enterTo(answer: Answer) : Dialog {
        logger.info(">> enterTo $answer ")
        val res = worldRouter.getNext(answer)
        way.add(res.id);
        logger.info("added ${res.id}, way: ${way.toTypedArray().contentToString()}")
        logger.info("<< enterTo return: ${res.id}")
        return res as Dialog;
    }

    fun exit() : Dialog {
        logger.info(">> exit ")
        val res = worldRouter.getNext(Answer.enter(way.last()));
        way.removeAt(way.lastIndex)
        logger.info("removed ${res.id}, way: ${way.toTypedArray().contentToString()}")
        logger.info("<< exit : retunt ${res.id}")
        return res as Dialog;
    }

    fun isRoot(): Boolean {
        return way.size == 1;
    }


}