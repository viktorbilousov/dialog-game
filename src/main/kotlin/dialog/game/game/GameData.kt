package dialog.game.game

import dialog.game.minigames.tea.TeaGame
import dialog.system.models.Answer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class GameData {
    companion object {
        public val variablePhrases: HashMap<String, () -> Array<String>> = HashMap();
        public val variableAnswers: HashMap<String, () -> Array<Answer>> = HashMap();
        public val variableTexts: HashMap<String, () -> String> = HashMap();
        public val gameVariables = HashMap<String, Any?>()

        private val logger = LoggerFactory.getLogger(GameData::class.java) as Logger


        init {
            addTextToReplace("var") {
                gameVariables.map { "${it.key}=${it.value}" }.joinToString ( "\n" )
            }
        }

        public fun boolGameVar(name: String): Boolean
        {
            if(gameVariables[name] == null || gameVariables[name] !is Boolean) return false
            return gameVariables[name].toString().toBoolean()
        }


        public fun addTextToReplace(key: String, text: String) {
            logger.info("add text to replace: $key")
            variableTexts[key] = { text }
        }

        public fun addTextToReplace(key: String, lambda: () -> String) {
            logger.info("add text to replace: $key")
            variableTexts[key] = lambda
        }

        public fun addPhraseToInsert(key: String, lambda: () -> Array<String>){
            logger.info("add phrase to insert: $key")
            variablePhrases[key] = lambda
        }

        public fun addPhraseToInsert(key: String, vararg phrases: String){
            logger.info("add phrase to insert: $key")
            variablePhrases[key] = {phrases as Array<String>}
        }


        public fun addAnswersToInsert(key: String, lambda: () -> Array<Answer>) {
            logger.info("add answers to insert: $key")
            variableAnswers[key] = lambda
        }

        public fun addAnswersToInsert(key: String, vararg answTexts: String) {
            logger.info("add answers to insert: $key")
            variableAnswers[key] = { answTexts.toList().map { Answer("NAN", it) }.toTypedArray() }
        }

        public fun addAnswersToInsert(key: String, vararg answs: Answer) {
            logger.info("add answers to insert: $key")
            variableAnswers[key] = { answs as Array<Answer> }
        }
    }
}