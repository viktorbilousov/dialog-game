package phrases

import game.Game
import models.Answer
import models.items.phrase.Phrase

class ConditionsFabric {
    companion object {
        public val firstTimeDiffAnswer =
            fun(answers: Array<Answer>, count: Int): Array<Answer> {
                val filterFirst = "[F]"
                val filterOther = "[S]"
                val filteredList = arrayListOf<Answer>()
                answers.forEach {
                    var type = 0;
                    if (it.text.trim().substring(0, 3) == filterFirst) {
                        it.text = it.text.subSequence(3, it.text.length).toString().trim()
                        type = 1
                    };
                    else if (it.text.trim().substring(0, 3) == filterOther) {
                        it.text = it.text.subSequence(3, it.text.length).toString().trim()
                        type = 2
                    };
                    if(type == 0 ){
                        filteredList.add(it);
                    }
                    if (count == 1 && type == 1) {
                        filteredList.add(it);
                    } else if (count > 1 && type == 2){
                        filteredList.add(it);
                    }
                }
                if (filteredList.isEmpty()) {
                    return answers
                }

                return filteredList.toTypedArray()
            }

        public val countAnswer = fun(answers: Array<Answer>, count: Int): Array<Answer> {

            val lastFilter = "[*]"
            var maxCnt = 0;
            for (answer in answers) {
                val filterLabel = getFilterLabel(answer.text);
                if (filterLabel != null) {
                    if (filterLabel == lastFilter) continue
                    val number = filterLabel.substring(1, filterLabel.length - 1).toIntOrNull()
                    if (number == null) continue
                    if (number > maxCnt) maxCnt = number
                }
            }

            val filteredList = arrayListOf<Answer>()

            for (answer in answers) {
                val filterLabel = getFilterLabel(answer.text)
                if (filterLabel == null) {
                    filteredList.add(answer)
                } else {
                    answer.text = answer.text.subSequence(filterLabel.length, answer.text.length).toString().trim()
                    if (maxCnt < count) {
                        if(filterLabel == lastFilter){
                            filteredList.add(answer)
                        }else{
                            continue
                        }
                    } else {
                        val number = filterLabel.substring(1, filterLabel.length - 1).toIntOrNull()
                        if (number == null || number == count) {
                            filteredList.add(answer)
                        }
                    }
                }
            }
            return filteredList.toTypedArray()
        }


        public val countPhrase = fun(phrases: Array<String>, count: Int): Array<String> {

            val lastFilter = "[*]"
            var maxCnt = 0;
            for (phrase in phrases) {
                val filterLabel = getFilterLabel(phrase);
                if (filterLabel != null) {
                    if (filterLabel == lastFilter) continue
                    val number = filterLabel.substring(1, filterLabel.length - 1).toIntOrNull()
                    if (number == null) continue
                    if (number > maxCnt) maxCnt = number
                }
            }
            val filteredList = arrayListOf<String>()
            for (phrase in phrases) {
                val filterLabel = getFilterLabel(phrase)
                if (filterLabel == null) {
                    filteredList.add(phrase)
                } else{
                    val phrase = phrase.subSequence(filterLabel.length, phrase.length).toString().trim()
                    if (maxCnt < count) {
                        if(filterLabel == lastFilter){
                            filteredList.add(phrase)
                        }else{
                            continue
                        }
                    } else {
                        val number = filterLabel.substring(1, filterLabel.length - 1).toIntOrNull()
                        if (number == null || number == count) {
                            filteredList.add(phrase)
                        }
                    }
                }
            }
            return filteredList.toTypedArray()
        }


        private fun getFilterLabel(text: String): String? {
            if( text.trim()[0] == '[' && text.trim().indexOf(']') > 1 ) {
                return text.trim().substring(0, text.trim().indexOf(']')+1)
            }
            return null
        }

        public val debugAnswerFilter =
            fun(answers: Array<Answer>, count: Int): Array<Answer> {
                return answers.filter {
                    val debug = Game.settings["debug"] as Boolean
                    if(debug) return@filter true
                    if(getFilterLabel(it.text) == null) return@filter true
                    if(getFilterLabel(it.text) == "[debug]") return@filter false
                    return@filter true
                }.toTypedArray()
            }
    }
}