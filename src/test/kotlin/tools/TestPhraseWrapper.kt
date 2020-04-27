package tools

import models.Answer
import models.items.phrase.AFilteredPhrase
import models.items.phrase.APhrase
import models.items.phrase.DebugFilteredPhrase
import phrases.collections.AnswerChooserCollection

class TestPhraseWrapper(_phrase: AFilteredPhrase) {

    public lateinit var resultAnswers: Array<Answer>;
    public lateinit var resultPhrases: Array<String>;

    public val phrase: DebugFilteredPhrase = APhrase.convertTo(_phrase)

    init {
        this.phrase.answerChooser = AnswerChooserCollection.first()
        this.phrase.afterFilter = { answers, phrases, it ->
            resultAnswers = answers;
            resultPhrases = phrases;
            AFilteredPhrase.FilterResult(answers, phrases)

        }
    }
}