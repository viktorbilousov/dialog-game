import game.*
import game.Game.Companion.settings
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class GameMain {
    companion object{
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
        @JvmStatic
        fun main(args: Array<String>) {
            println("test")
            logger.info("--- GAME STARTING ---")
            logger.info("")
            val game = Game()
            logger.info("")
            logger.info("--- GAME LOADING ---")
            logger.info("")
            Loader(game).load(
                settings["phrases-folder"] as String,
                settings["routers-folder"] as String,
                settings["graphs-folder"] as String
            )
            game.debug(true)
            logger.info("")
            logger.info("--- GAME TESTING ---")
            logger.info("")
            Tester.testGame(game)
           // (game.phrases["world.forest"]as PhraseFilterAnswers).setFilter(ConditionsFabric.firstTimeDiffAnswer)
            logger.info("")
            logger.info("--- GAME RUNNING ---")
            logger.info("")
            Runner(game, game.world!!).run()
        }
    }
}