package dialog.game.maingame

import dialog.system.models.Answer

class GameData {
    companion object {
        public val variablePhrases: HashMap<String, () -> Array<String>> = HashMap();
        public val variableAnswers: HashMap<String, () -> Array<Answer>> = HashMap();
        public val variableTexts: HashMap<String, () -> String> = HashMap();
        public val gameVariables = HashMap<String, Any?>()


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
            variableTexts[key] = { text }
        }

        public fun addTextToReplace(key: String, lambda: () -> String) {
            variableTexts[key] = lambda
        }

        public fun addPhraseToInsert(key: String, lambda: () -> Array<String>){
            variablePhrases[key] = lambda
        }

        public fun addPhraseToInsert(key: String, vararg phrases: String){
            variablePhrases[key] = {phrases as Array<String>}
        }


        public fun addAnswersToInsert(key: String, lambda: () -> Array<Answer>) {
            variableAnswers[key] = lambda
        }

        public fun addAnswersToInsert(key: String, vararg answTexts: String) {
            variableAnswers[key] = { answTexts.toList().map { Answer("NAN", it) }.toTypedArray() }
        }

        public fun addAnswersToInsert(key: String, vararg answs: Answer) {
            variableAnswers[key] = { answs as Array<Answer> }
        }
    }
}