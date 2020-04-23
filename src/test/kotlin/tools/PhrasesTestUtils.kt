package tools

import models.Answer
import models.items.phrase.FilteredPhrase

class PhrasesTestUtils {

    companion object {

        public inline fun <reified T : FilteredPhrase> createTestPhrase(answers: Array<Answer>): T {
            return createTestPhrase(arrayOf("phrase1", "phrase2"), answers)
        }


        public inline fun <reified T : FilteredPhrase> createTestPhrase(
            phrase: Array<String>,
            answers: Array<Answer>
        ): T {
            return T::class.java
                .getConstructor(String::class.java, Array<String>::class.java, Array<Answer>::class.java)
                .newInstance("test", phrase, answers)

        }
    }
}