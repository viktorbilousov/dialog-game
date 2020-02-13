package phrases

import models.Answer
import models.AnswerType
import models.items.phrase.EmptyPhrase
import models.items.phrase.Phrase
import models.items.phrase.SimplePhrase
import models.items.text.PhraseText

class PhraseFabric {
    companion object{
        public fun plugExitPhrase(id: String) : SimplePhrase{
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