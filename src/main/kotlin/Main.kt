import com.tinkerpop.blueprints.impls.tg.TinkerGraph
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReader
import game.Game
import game.Game.Companion.settings
import game.Loader
import game.Runner
import models.items.phrase.EmptyPhrase
import phrases.PhraseFabric

class Main {
    companion object{
        @JvmStatic
        fun main(args: Array<String>) {
            val game = Game();
            game.phrases["plug"] = PhraseFabric.PlugExitPhrase("plug");
            Loader(game).load(
                settings["phrases-folder"] as String,
                settings["routers-folder"] as String,
                settings["graphs-folder"] as String
            )
            Runner(game, game.world!!).run();
        }
    }
}