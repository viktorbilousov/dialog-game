package game

import models.Answer

class GameData {
    companion object {
        public val variablePhrases: HashMap<String, () -> String> = HashMap();
        public val variableAnswers: HashMap<String, () -> Array<Answer>> = HashMap();
        public val gameVariables = HashMap<String, Any?>()

        public fun boolGameVar(name: String): Boolean
        {
            if(gameVariables[name] == null || gameVariables[name] !is Boolean) return false
            return gameVariables[name].toString().toBoolean()
        }


        public fun addPhrase(label: String, text: String) {
            variablePhrases[label] = { text }
        }

        public fun addPhrase(label: String, lambda: () -> String) {
            variablePhrases[label] = lambda
        }

        public fun addAnswers(label: String, lambda: () -> Array<Answer>) {
            variableAnswers[label] = lambda
        }

        public fun addAnswers(label: String, answText: String) {
            addAnswers(label, arrayOf(answText))
        }

        public fun addAnswers(label: String, answ: Answer) {
            addAnswers(label, arrayOf(answ))
        }

        public fun addAnswers(label: String, answTexts: Array<String>) {
            variableAnswers[label] = { answTexts.toList().map { Answer("NAN", it) }.toTypedArray() }
        }

        public fun addAnswers(label: String, answs: Array<Answer>) {
            variableAnswers[label] = { answs }
        }
    }
}