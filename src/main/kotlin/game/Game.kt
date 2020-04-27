package game

import minigames.tea.TeaGame
import models.World
import models.items.dialog.ADialog
import models.items.phrase.APhrase
import phrases.collections.PhraseCollection
import java.io.File

class Game {

    companion object {
        public val settings = HashMap<String, Any?>()

        init {

            TeaGame.javaClass

            settings["debug"] = false
            settings["world-router-id"] = "router.world"
            settings["graphs-folder"] = File("src/main/resources/graphs").absolutePath
            settings["routers-folder"] = File("src/main/resources/routers").absolutePath
            settings["phrases-folder"] = File("src/main/resources/phrases").absolutePath
        }
    }


    public val isDebug: Boolean
    get() = settings["debug"] == true

    val phrases = hashMapOf<String, APhrase>()
    val dialogs = hashMapOf<String, ADialog>()
    var world: World? = null
        set(value) {
            field = value;
        }

    init {

        phrases["plug"] = PhraseCollection.plugExitPhrase("plug");

        /* Loader(this).load(
             settings["phrases-folder"] as String,
             settings["routers-folder"] as String,
             settings["graphs-folder"] as String
         )
         init()
         Tester.testGame(this);*/
    }

    public fun debug(isEnable: Boolean) {
        settings["debug"] = isEnable
        if (isEnable) {
            dialogs["world"]!!.router.startPointId = "debug.world.startpoint"
        }
    }


}