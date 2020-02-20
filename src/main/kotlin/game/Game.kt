package game

import models.World
import models.items.DialogItem
import models.items.dialog.Dialog
import phrases.PhraseFabric
import java.io.File

class Game {

    companion object{
       public val settings = HashMap<String, Any?>()
        init {
            settings["debug"] = false
            settings["world-router-id"] = "router.world"
            settings["graphs-folder"] = File("src/main/resources/graphs").absolutePath
            settings["routers-folder"] = File("src/main/resources/routers").absolutePath
            settings["phrases-folder"] = File("src/main/resources/phrases").absolutePath
        }
    }


    val phrases = hashMapOf<String, DialogItem>()
    val dialogs = hashMapOf<String, Dialog>()
    var world: World? = null
    set(value) {
        field = value;
    }

    init{

        phrases["plug"] = PhraseFabric.plugExitPhrase("plug");

        /* Loader(this).load(
             settings["phrases-folder"] as String,
             settings["routers-folder"] as String,
             settings["graphs-folder"] as String
         )
         init()
         Tester.testGame(this);*/
    }

    private fun init(){

    }
    public fun debug(isEnable: Boolean){
        settings["debug"] = isEnable
        if(isEnable) {
            dialogs["world"]!!.router.startPointId = "debug.world.startpoint"
        }
    }


}