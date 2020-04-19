import debug.game.runners.AutoRunner
import debug.game.runners.RecordRunner
import debug.record.service.GameRecorder
import game.*
import game.Game.Companion.settings
import models.World
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class GameMain {
    companion object{

        private val runner : Class<out Runner> = RecordRunner::class.java;

        private val logger = LoggerFactory.getLogger(GameMain::class.java) as Logger
        @JvmStatic
        fun main(args: Array<String>) {
            GameRecorder();
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

            //val gameRunner: Runnable = Runner(game, game.world!!);
           // val gameRunner: Runner = RecordRunner(game, game.world!!)
           // val gameRunner = AutoRunner(game, game.world!!)
            val gameRunner = runner.getConstructor(Game::class.java, World::class.java).newInstance(game, game.world!!)
            gameRunner.run()
        }
    }
}