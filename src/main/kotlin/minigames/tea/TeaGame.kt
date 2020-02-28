package minigames.tea

import minigames.tea.models.MixedTea
import minigames.tea.models.Tea
import minigames.tea.service.TeaQuality
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TeaGame {
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger

        var currentTea = MixedTea();
        lateinit var goalTea: Tea

        public fun reset() {
            logger.info("GAME RESET")
            currentTea = MixedTea()
        }
        public fun quality(): TeaQuality.Quality{
            val res = TeaQuality.calcQuality(currentTea, goalTea)
            logger.info("GAME QUALITY: $res")
            return res
        }
        public fun answer() : String{
            val res = TeaQuality.calcQuality(currentTea, goalTea).toString()
            logger.info("GAME ANSWER: $res")
            return res
        }

    }
}