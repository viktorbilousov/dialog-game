package models

import game.Game
import models.items.DialogItem
import models.items.dialog.Dialog
import models.router.Router
import java.lang.IllegalArgumentException

class World(router: Router, game: Game, startDialogId: String) {

    public val worldRouter = router;
    private var lastRouterId: String? = null;
    private var currentRouterId: String? = null;
    private val game = game;



    fun start() : DialogItem {
        currentRouterId = worldRouter.startPoint.id;
        return worldRouter.startPoint as DialogItem;
    }

    fun enterTo(answer: Answer) : DialogItem {
        val res = worldRouter.getNext(answer)
        lastRouterId = currentRouterId + ""
        currentRouterId = res.id;
        return res;
    }

    //todo fix
    fun exit() : DialogItem {
        val res = worldRouter.getNext(Answer.enter(lastRouterId!!));
        lastRouterId = currentRouterId + ""
        currentRouterId = res.id;
        return res;
    }

    fun isRoot(): Boolean {
        return currentRouterId == worldRouter.startPointId;
    }


}