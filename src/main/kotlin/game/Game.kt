package game

import models.World
import models.items.DialogItem
import models.items.dialog.Dialog

class Game {

    val settings = HashMap<String, Any?>()
    val phrases = hashMapOf<String, DialogItem>()
    val dialogs = hashMapOf<String, Dialog>()
    var world: World? = null;
    init {
        settings["world-router-id"] = "router.world"
        settings["graphs-folder"] = "./src/resources/graphs"
        settings["routers-folder"] = "./src/resources/routers"
        settings["phrases-folder"] = "./src/resources/phrases"
    }

}