package tools

import dialog.game.phrases.collections.AnswerChooserCollection
import dialog.system.models.answer.Answer
import dialog.system.models.items.phrase.AFilteredPhrase
import dialog.system.models.items.phrase.APhrase
import dialog.system.models.items.phrase.DebugFilteredPhrase


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

    public fun run(): Answer {
       return  phrase.run()
    }
}