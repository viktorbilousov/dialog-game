package phrases.collections

import models.items.phrase.PhraseChooser
import java.util.*

class PhraseChoosersCollections {
    companion object{
        public fun random() = object : PhraseChooser{
            private val random = Random();
            override fun choose(phrases: Array<String>): String {
                return phrases[random.nextInt(phrases.size)]
            }
        }
        public fun first() = object : PhraseChooser{
            override fun choose(phrases: Array<String>): String {
                return phrases[0]
            }
        }
    }
}