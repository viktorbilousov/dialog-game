package phrases.collections

import models.Answer
import models.AnswerType
import models.items.phrase.SimplePhrase

class PhraseCollection {
    companion object{
        public fun plugExitPhrase(id: String) : SimplePhrase {
            return SimplePhrase(
                id,
                "PLUG",
                arrayOf(
                    Answer(
                        "exit",
                        "go back",
                        AnswerType.EXIT
                    )
                )
            )
        }
    }
}