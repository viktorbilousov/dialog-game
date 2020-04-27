package tools

import models.Answer
import models.items.phrase.*

class TestPhraseTools{

    companion object{
        public fun setTestPrinter(phrase: APhrase) : TestPrinter{
            val printer = TestPrinter();
            phrase.phrasePrinter = printer;
            return printer;
        }

        public inline fun <reified T : FilteredPhrase> createTestPhrase(answers: Array<Answer>): T {
            return createTestPhrase(arrayOf("phrase1", "phrase2"), answers)
        }

        public inline fun <reified T : FilteredPhrase> createTestPhrase(answers: Array<String>): T {
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

        public inline fun <reified T : FilteredPhrase> createTestPhrase(
            phrase: Array<String>,
            answers: Array<String>
        ): T {
            var cnt = 0;
            val _answers = answers.map { Answer("test.id.${cnt++}", it ) }.toTypedArray()

            return T::class.java
                .getConstructor(String::class.java, Array<String>::class.java, Array<Answer>::class.java)
                .newInstance("test", phrase, _answers)

        }

    }

}