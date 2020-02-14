
import game.Game
import game.Game.Companion.settings
import game.Loader
import game.Runner
import game.Tester
import phrases.ConditionsFabric
import phrases.PhraseFabric
import phrases.PhraseFilterAnswers

class Main {
    companion object{
        @JvmStatic
        fun main(args: Array<String>) {
            val game = Game();
            game.phrases["plug"] = PhraseFabric.plugExitPhrase("plug");
            Loader(game).load(
                settings["phrases-folder"] as String,
                settings["routers-folder"] as String,
                settings["graphs-folder"] as String
            )

            Tester.testGame(game);
            (game.phrases["world.forest"]as PhraseFilterAnswers).setFilter(ConditionsFabric.firstTimeDiffAnswer)

            Runner(game, game.world!!).run();
        }
    }
}