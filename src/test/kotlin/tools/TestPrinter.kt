package tools

import models.Answer
import models.items.phrase.PhrasePrinter

class TestPrinter()  : PhrasePrinter {
    public var lastPhrase : String? = null
        private set;

    override fun printTextDialog(text: String, answer: Array<Answer>) {
        lastPhrase = text;
    }
}