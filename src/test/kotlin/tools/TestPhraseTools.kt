package tools

import models.Answer
import models.items.phrase.Phrase
import models.items.phrase.PhrasePrinter
import phrases.fabric.AnswerChooserFabric

class TestPhraseTools{

    companion object{
        public fun setTestPrinter(phrase: Phrase) : TestPrinter{
            val printer = TestPrinter();
            phrase.phrasePrinter = printer;
            return printer;
        }
    }

}