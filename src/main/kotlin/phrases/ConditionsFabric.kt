package phrases

import game.Game
import models.Answer

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

        public val notCountAnswer = fun(answers: Array<Answer>, count: Int): Array<Answer>{
            return answers.filter {
                val label = getFilterLabel(it.text)
                return@filter label == null
                        || !label.startsWith("!")
                        || label.removePrefix("!").toIntOrNull() == null
                        || label.removePrefix("!").toIntOrNull() != count
            }.toTypedArray()
        }

        public val notCountPhrase = fun(answers: Array<String>, count: Int): Array<String>{
            return answers.filter {
                val label = getFilterLabel(it)
                return@filter label == null
                        || !label.startsWith("!")
                        || label.removePrefix("!").toIntOrNull() == null
                        || label.removePrefix("!").toIntOrNull() != count
            }.toTypedArray()
        }

        public val countAnswer = fun(answers: Array<Answer>, count: Int): Array<Answer> {
            var maxCnt = 0;
            for (answer in answers) {
                val filterLabel = getFilterLabel(answer.text);
                if (filterLabel != null) {
                    val number = filterLabel.toIntOrNull() ?: continue
                    if (number > maxCnt) maxCnt = number
                }
            }


            return  answers
                .filter {
                    val filterLabel = getFilterLabel(it.text) ?: return@filter true;
                    return@filter countFilter(filterLabel, maxCnt, count) }
                .map {
                val filterLabel = getFilterLabel(it.text) ?: return@map it;
                it.text = it.text.subSequence(filterLabel.length+1, it.text.length).toString().trim()
                return@map it;
            }.toTypedArray()
        }

        private fun countFilter(filterLabel: String, maxCnt: Int, count: Int) : Boolean{
            val lastFilter = "*"
            if (maxCnt < count) {
                return filterLabel == lastFilter
            } else {
                val number = filterLabel.toIntOrNull()
                if (number == null || number == count) {
                    return true
                }
            }
            return false;
        }


        public val countPhrase = fun(phrases: Array<String>, count: Int): Array<String> {
            var maxCnt = 0;
            for (phrase in phrases) {
                val filterLabel = getFilterLabel(phrase);
                if (filterLabel != null) {
                    val number = filterLabel.toIntOrNull() ?: continue
                    if (number > maxCnt) maxCnt = number
                }
            }
            val filteredList = arrayListOf<String>()
            for (phrase in phrases) {
                val filterLabel = getFilterLabel(phrase)
                if (filterLabel == null) {
                    filteredList.add(phrase)
                    continue;
                }

                val phraseText = phrase.subSequence(filterLabel.length+1, phrase.length).toString().trim()

               if(countFilter(filterLabel, maxCnt, count)) {
                    filteredList.add(phraseText)
               }

            }
            return filteredList.toTypedArray()
        }


        private fun getFilterLabel(text: String): String? {
            if( text.trim()[0] == '[' && text.trim().indexOf(']') > 1 ) {
                return text.trim().substring(1, text.trim().indexOf(']'))
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