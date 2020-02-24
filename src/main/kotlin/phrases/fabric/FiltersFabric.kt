package phrases.fabric

import game.Game
import models.Answer

class FiltersFabric {
    companion object {

        public val removeLabelPhrases = fun(phrases: Array<String>, count: Int): Array<String> {
            return phrases.map {
                if(getFilterLabel(it) != "debug")  return@map removeLabel(
                    it
                )
                return@map it
            }.toTypedArray()
        }

        public val removeLabelAnswers= fun(answers: Array<Answer>, count: Int): Array<Answer> {
            return answers.map {
                if(getFilterLabel(it.text) != "debug")  it.text =
                    removeLabel(it.text)
                return@map it
                }.toTypedArray()
        }

        public val notCountAnswer = fun(answers: Array<Answer>, count: Int): Array<Answer>{
            return answers.filter {
                notCountFilter(
                    getFilterLabel(
                        it.text
                    ), count
                )
            }.toTypedArray()
        }

        public val notCountPhrase = fun(answers: Array<String>, count: Int): Array<String>{
            return answers.filter {
                notCountFilter(
                    getFilterLabel(
                        it
                    ), count
                )
            }.toTypedArray()
        }

        public fun removeLabel(str: String) : String{
            val filter = getFilterLabel(str) ?: return str;
            return str.subSequence(filter.length + 2 , str.length).toString().trim()

        }

        public val countAnswer = fun(answers: Array<Answer>, count: Int): Array<Answer> {
            var maxCnt = 0;
            for (answer in answers) {
                val filterLabel =
                    getFilterLabel(answer.text);
                if (filterLabel != null) {
                    val number = filterLabel.toIntOrNull() ?: continue
                    if (number > maxCnt) maxCnt = number
                }
            }

            return answers
                .filter {
                    val filterLabel = getFilterLabel(it.text)
                        ?: return@filter true;
                    return@filter countFilter(
                        filterLabel,
                        maxCnt,
                        count
                    )
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

        private fun notCountFilter (label: String?, count: Int): Boolean {
            return label == null
                    || !label.startsWith("!")
                    || label.removePrefix("!").toIntOrNull() == null
                    || label.removePrefix("!").toIntOrNull() != count

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
            return  phrases
                .filter {
                    val filterLabel = getFilterLabel(it) ?: return@filter true;
                    return@filter countFilter(
                        filterLabel,
                        maxCnt,
                        count
                    )
                }
                .toTypedArray()
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