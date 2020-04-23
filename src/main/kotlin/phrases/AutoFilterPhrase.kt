package phrases

import models.Answer
import models.items.phrase.FilteredPhrase

class AutoFilterPhrase(id: String, phrases: Array<String>,  answers: Array<Answer>) : FilteredPhrase(id, phrases, answers){

}