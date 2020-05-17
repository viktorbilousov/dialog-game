package game

import dialog.game.game.Loader
import dialog.game.minigames.tea.TeaGame
import dialog.game.models.Minigame
import dialog.game.models.World
import dialog.game.phrases.collections.PhraseCollection
import dialog.system.models.items.dialog.ADialog
import dialog.system.models.items.phrase.APhrase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class Game {

    companion object {
        public val settings = HashMap<String, Any?>()

        private val logger = LoggerFactory.getLogger(Game::class.java) as Logger

        init {

            settings[""] = false
            settings["world-router-id"] = "router.world"
            settings["graphs-folder"] = File("src/main/resources/graphs").absolutePath
            settings["routers-folder"] = File("src/main/resources/routers").absolutePath
            settings["phrases-folder"] = File("src/main/resources/phrases").absolutePath
        }
        public val isDebug: Boolean
            get() = settings[""] == true
    }

    public fun addMinigames(vararg minigame: Minigame){
        minigame.forEach {
            logger.info("Load minigame: ${it.javaClass.simpleName}")
            it.init()
        }
    }




    val phrases = hashMapOf<String, APhrase>()
    val dialogs = hashMapOf<String, ADialog>()
    var world: World? = null
        set(value) {
            field = value;
        }

    init {
        phrases["plug"] = PhraseCollection.plugExitPhrase("plug");
    }

    public fun debug(isEnable: Boolean) {
        settings[""] = isEnable
        if (isEnable) {
            dialogs["world"]!!.router.startPointId = "dialog.game.debug.world.startpoint"
        }
    }


}