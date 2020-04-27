import debug.game.runners.AutoRunner
import debug.game.runners.RecordRunner
import debug.record.service.GameRecorder
import game.*
import game.Game.Companion.settings
import models.Answer
import models.World
import models.items.phrase.AnswerChooser
import models.items.runner.DefaultRunner
import models.items.runner.DialogItemRunner
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import phrases.collections.AnswerChooserCollection
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass


class GameMain {
    companion object{

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

            val gameRunnerClass =
                if(game.isDebug){ selectRunner()}
                else RecordRunner::class.java

            val gameRunner = gameRunnerClass.getConstructor(Game::class.java, World::class.java).newInstance(game, game.world!!)
            gameRunner.run()
        }

        public fun selectRunner() : Class<out Runner>{
            println("Select runner:\n\n[1] Default\n[2] Record\n[3] AutoRunner")
            val answers = arrayOf(
                Answer("1", "Default"),
                Answer("2", "Recorder"),
                Answer("3", "Record Runner")
            )
            val res = AnswerChooserCollection.console().chooseAnswer(answers)
            when(res.text){
                "Default" -> return Runner::class.java
                "Recorder" -> return RecordRunner::class.java
                "Record Runner" -> return AutoRunner::class.java
            }
            throw IllegalArgumentException("unrecognised answers")
        }
    }
}