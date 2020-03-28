package tools

import models.items.phrase.Phrase

class TestPhraseTools{

    companion object{
        public fun setTestPrinter(phrase: Phrase) : TestPrinter{
            val printer = TestPrinter();
            phrase.phrasePrinter = printer;
            return printer;
        }
    }

}