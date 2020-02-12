package game

import models.World
import models.items.DialogItem
import models.items.dialog.Dialog

class Game {

    companion object{
       public val settings = HashMap<String, Any?>()
        init {
            settings["world-router-id"] = "router.world"
            settings["graphs-folder"] = "./src/resources/graphs"
            settings["routers-folder"] = "./src/resources/routers"
            settings["phrases-folder"] = "./src/resources/phrases"
        }
    }

    val phrases = hashMapOf<String, DialogItem>()
    val dialogs = hashMapOf<String, Dialog>()
    var world: World? = null;

    init{
        Loader(this).load(
            settings["phrases-folder"] as String,
            settings["routers-folder"] as String,
            settings["graphs-folder"] as String
        )
        init()
        Tester.testGame(this);
    }

    private fun init(){

    }


}