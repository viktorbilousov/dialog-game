package phrases

import models.Answer
import org.junit.jupiter.api.Test
import phrases.collections.AnswerChooserCollection
import tools.PhrasesTestUtils.Companion.createTestPhrase
import tools.TestPrinter

class CountPhrase_Test {
    @Test
    fun test_CountPhrase_answer(){
        val phrase =  createTestPhrase<CountPhrase>(
            arrayOf(
                Answer("1", "[1] first") ,
                Answer("*", "[*] other"),
                Answer("not 2", "[!2] not second"),
                Answer("2", "[2] second")
            )
        )
        phrase.answerChooser = AnswerChooserCollection.first();

        val first = phrase.run()
        val second = phrase.run()
        val third = phrase.run()
        val fourth = phrase.run()
        assert(first.id == "1")
        assert(second.id == "2")
        assert(third.id == "*")
        assert(fourth.id == "*")
    }

    @Test
    fun test_CountPhrase_phrase(){
        val phrase =  createTestPhrase<CountPhrase>(
            arrayOf( "[*] other", "[1] phrase 1", "[!2] not phrase 3", "[2] phrase 2", "[!2] not phrase 3"),
            arrayOf(
                Answer("1", "first")
            )
        )
        phrase.answerChooser = AnswerChooserCollection.first();
        val printer = TestPrinter();
        phrase.phrasePrinter = printer;

        val first = phrase.run()
        assert(printer.lastPhrase == "phrase 1")

        val second = phrase.run()
        assert(printer.lastPhrase == "phrase 2")

        val third = phrase.run()
        assert(printer.lastPhrase == "other")

        val fourth = phrase.run()
        assert(printer.lastPhrase == "other")

    }
}