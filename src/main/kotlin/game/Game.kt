package game

import minigames.tea.TeaGame
import models.World
import models.items.DialogItem
import models.items.dialog.Dialog
import phrases.collections.PhraseCollection
import java.io.File

class Game {

    companion object{
       public val settings = HashMap<String, Any?>()
       public val gameVariables = HashMap<String, Any?>()

        public fun boolGameVar(name: String): Boolean
        {
            if(gameVariables[name] == null || gameVariables[name] !is Boolean) return false
            return gameVariables[name].toString().toBoolean()
        }

        init {

            TeaGame.javaClass

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

        phrases["plug"] = PhraseCollection.plugExitPhrase("plug");

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