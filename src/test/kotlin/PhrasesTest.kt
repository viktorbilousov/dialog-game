import models.Answer
import models.items.phrase.FilteredPhrase
import org.junit.jupiter.api.Test
import phrases.AutoPhrase
import phrases.CountPhrase
import phrases.ParametricIfElsePhrase
import phrases.ParametricPhrase
import phrases.collections.AnswerChooserCollection
import phrases.configurator.FilteredPhraseConfigurator
import tools.TestPhraseTools
import tools.TestPrinter

class PhrasesTest {
    @Test
    fun test_AutoPhrase(){
       val phrase =  createTestPhrase<AutoPhrase>(arrayOf(Answer("ok", "ans1") , Answer("error", "answ2") ))
        val res = phrase.run()
        assert(res.id == "ok")
    }

    @Test
    fun test_ParametricPhrase_phrase_get(){
       val testParams = hashMapOf<String, Any?>()
        testParams["test"] = true;

        val phrase =  createTestPhrase<ParametricPhrase>(
            arrayOf(
                "[GET=dsjfh] error" ,
                "[GET=test] ok"
            ),
            arrayOf(
                Answer("1", "answ")
            )
        )
        FilteredPhraseConfigurator(phrase, testParams).parametric()
        phrase.answerChooser = AnswerChooserCollection.first();
        val printer = TestPhraseTools.setTestPrinter(phrase)

        phrase.run()
        assert(printer.lastPhrase == "ok")
    }


    @Test
    fun test_ParametricIfElsePhrase_phrase(){
        val testParams = hashMapOf<String, Any?>()
        testParams["if"] = true;
        testParams["elseif"] = false;
        testParams["else"] = false;

        val phrase =  createTestPhrase<ParametricIfElsePhrase>(
            arrayOf(
                "[IF] [GET=if] if_answ" ,
                "[ELSE IF] [GET=elseif] elseif_answ",
                "[ELSE] [GET=else] else_answ"
            ),
            arrayOf(
                Answer("1", "answ")
            )
        )
        FilteredPhraseConfigurator(phrase, testParams).parametricIfElseStatement()
        phrase.answerChooser = AnswerChooserCollection.first();
        val printer = TestPhraseTools.setTestPrinter(phrase)

        phrase.run()
        assert(printer.lastPhrase == "if_answ")

        testParams["if"] = false;
        testParams["elseif"] = true;

        phrase.run()
        assert(printer.lastPhrase == "elseif_answ")

        testParams["elseif"] = false;
        testParams["else"] = true;

        phrase.run()
        assert(printer.lastPhrase == "else_answ")
    }


    @Test
    fun test_ParametricIfElsePhrase_answer_multiply(){
        val testParams = hashMapOf<String, Any?>()
        testParams["test1"] = true;
        testParams["test2"] = false;
        testParams["test3"] = true;

        val phrase =  createTestPhrase<ParametricIfElsePhrase>(
            arrayOf(
                Answer("1", "[IF][GET=test2] if_answ") ,
                Answer("2", "[ELSE IF][GET=test1][GET=test2] elseif1_false"),
                Answer("2", "[ELSE IF][GET=test3][GET=test1] elseif2_ok"),
                Answer("2", "[ELSE] else_answ")
            )
        )
        FilteredPhraseConfigurator(phrase, testParams).parametricIfElseStatement()
        phrase.answerChooser = AnswerChooserCollection.first();

        var res = phrase.run()
        assert(res.text == "elseif2_ok")
    }

    @Test
    fun test_ParametricIfElsePhrase_answer(){
        val testParams = hashMapOf<String, Any?>()
        testParams["if"] = true;
        testParams["elseif"] = false;
        testParams["else"] = false;

        val phrase =  createTestPhrase<ParametricIfElsePhrase>(
            arrayOf(
                Answer("1", "[IF][GET=if] if_answ") ,
                Answer("2", "[ELSE IF][GET=elseif] elseif_answ"),
                Answer("2", "[ELSE][GET=else] else_answ")
            )
        )
        FilteredPhraseConfigurator(phrase, testParams).parametricIfElseStatement()
        phrase.answerChooser = AnswerChooserCollection.first();

        var res = phrase.run()
        assert(res.text == "if_answ")

        testParams["if"] = false;
        testParams["elseif"] = true;

         res = phrase.run()
        assert(res.text== "elseif_answ")

        testParams["elseif"] = false;
        testParams["else"] = true;

        res = phrase.run()
        assert(res.text == "else_answ")
    }

    @Test
    fun test_ParametricPhrase_phrase_not(){
        val testParams = hashMapOf<String, Any?>()
        testParams["test"] = true;


        val phrase = createTestPhrase<ParametricPhrase>(
            arrayOf(
                "[NOT=dsjfh] ok" ,
                "[NOT=test] error"
            ),
            arrayOf(
                Answer("1", "answr")
            )
        )
        FilteredPhraseConfigurator(phrase, testParams).parametric()
        phrase.answerChooser = AnswerChooserCollection.first();
        val printer = TestPhraseTools.setTestPrinter(phrase)

        phrase.run()
        assert(printer.lastPhrase == "ok")
    }

    @Test
    fun test_ParametricPhrase_answer_multiply_get(){
        val testParams = hashMapOf<String, Any?>()
        testParams["test1"] = true;
        testParams["test2"] = true;

        val phrase =  createTestPhrase<ParametricPhrase>(
            arrayOf(
                Answer("1", "[GET=test1][GET=test2][GET=test3] error") ,
                Answer("2", "[GET=test1][GET=test2] ok")
            )
        )
        FilteredPhraseConfigurator(phrase, testParams).parametric()
        phrase.answerChooser = AnswerChooserCollection.first();

        val res = phrase.run()
        assert(res.text == "ok")
    }

    @Test
    fun test_ParametricPhrase_answer_get(){
        val testParams = hashMapOf<String, Any?>()
        testParams["test"] = true;

        val phrase =  createTestPhrase<ParametricPhrase>(
            arrayOf(
                Answer("1", "[GET=dsjfh] error") ,
                Answer("2", "[GET=test] ok")
            )
        )
        FilteredPhraseConfigurator(phrase, testParams).parametric()
        phrase.answerChooser = AnswerChooserCollection.first();

        val res = phrase.run()
        assert(res.text == "ok")
    }

    @Test
    fun test_ParametricPhrase_answer_not(){
        val testParams = hashMapOf<String, Any?>()
        testParams["test"] = true;

        val phrase =  createTestPhrase<ParametricPhrase>(
            arrayOf(
                Answer("1", "[NOT=dsjfh] ok") ,
                Answer("2", "[NOT=test] erroe")
            )
        )
        FilteredPhraseConfigurator(phrase).parametric()
        phrase.answerChooser = AnswerChooserCollection.first();

        val res = phrase.run()
        assert(res.text == "ok")
    }


    @Test
    fun test_ParametricPhrase_answer_set(){
        val testParams = hashMapOf<String, Any?>()

        val phrase =  createTestPhrase<ParametricPhrase>(
            arrayOf(
                Answer("1", "[SET=test] ok")
            )
        )
        FilteredPhraseConfigurator(phrase, testParams).parametric()
        phrase.answerChooser = AnswerChooserCollection.first();

        phrase.run()
        assert(testParams["test"] != null)
    }

    @Test
    fun test_ParametricPhrase_answer_unset(){
        val testParams = hashMapOf<String, Any?>()
        testParams["test"] = true;

        val phrase =  createTestPhrase<ParametricPhrase>(
            arrayOf(
                Answer("1", "[UNSET=test] ok")
            )
        )
        FilteredPhraseConfigurator(phrase, testParams).parametric()
        phrase.answerChooser = AnswerChooserCollection.first();

        phrase.run()
        assert(testParams["test"] == false)
    }

    @Test
    fun test_CountPhrase_answer(){
        val phrase =  createTestPhrase<CountPhrase>(
            arrayOf(
                Answer("1", "[1] first") ,
                Answer("2", "[2] second") ,
                Answer("not 2", "[!2] not second"),
                Answer("*", "[*] other")
            )
        )
        phrase.answerChooser = AnswerChooserCollection.first();

        val first = phrase.run()
        val second = phrase.run()
        val third = phrase.run()
        val fourth = phrase.run()
        assert(first.id == "1")
        assert(second.id == "2")
        assert(third .id == "*")
        assert(fourth.id == "*")
    }

    @Test
    fun test_CountPhrase_phrase(){
        val phrase =  createTestPhrase<CountPhrase>(
           arrayOf("[1] phrase 1", "[2] phrase 2", "[!2] not phrase 3", "[*] other"),
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

    private inline fun<reified T : FilteredPhrase> createTestPhrase(answers : Array<Answer>) : T{
        return createTestPhrase(arrayOf("phrase1", "phrase2"), answers)
    }


    private inline fun<reified T : FilteredPhrase> createTestPhrase(phrase: Array<String>, answers : Array<Answer>) : T{
        return T::class.java
            .getConstructor(String::class.java, Array<String>::class.java, Array<Answer>::class.java)
            .newInstance("test", phrase, answers)

    }


}