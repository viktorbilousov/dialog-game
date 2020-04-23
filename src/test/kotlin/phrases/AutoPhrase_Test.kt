package phrases

import models.Answer
import org.junit.jupiter.api.Test
import tools.PhrasesTestUtils.Companion.createTestPhrase

class AutoPhrase_Test {
    @Test
    fun test_AutoPhrase(){
        val phrase =  createTestPhrase<AutoPhrase>(arrayOf(Answer("ok", "ans1") , Answer("error", "answ2") ))
        val res = phrase.run()
        assert(res.id == "ok")
    }
}