import game.Game
import game.Game.Companion.settings
import game.Loader
import game.Runner
import game.Tester
import models.items.phrase.FilteredPhrase
import phrases.ConditionsFabric
import phrases.PhraseFabric

class Main {
    companion object{
        @JvmStatic
        fun main(args: Array<String>) {
            val game = Game()
            Loader(game).load(
                settings["phrases-folder"] as String,
                settings["routers-folder"] as String,
                settings["graphs-folder"] as String
            )

            Tester.testGame(game)
           // (game.phrases["world.forest"]as PhraseFilterAnswers).setFilter(ConditionsFabric.firstTimeDiffAnswer)
            (game.phrases["world.forest"] as FilteredPhrase).addAnswerFilter("test", ConditionsFabric.firstTimeDiffAnswer )
            Runner(game, game.world!!).run()
        }
    }
}