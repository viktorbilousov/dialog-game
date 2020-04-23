package tools

import models.items.phrase.APhrase

class TestPhraseTools{

    companion object{
        public fun setTestPrinter(phrase: APhrase) : TestPrinter{
            val printer = TestPrinter();
            phrase.phrasePrinter = printer;
            return printer;
        }
    }

}