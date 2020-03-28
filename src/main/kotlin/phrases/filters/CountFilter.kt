package phrases.filters

import models.Answer
import phrases.collections.FiltersCollection
import tools.FiltersUtils

class CountFilter {
    companion object{
        public val notCountAnswer = fun(answers: Array<Answer>, count: Int): Array<Answer>{
            return answers.filter {
                val labels = FiltersUtils.getFilterLabels(it.text) ?: return@filter true
                for (label in labels) {
                    if(!notCountFilter(label, count))  return@filter false
                }
                return@filter true
            }.toTypedArray()
        }

        public val notCountPhrase = fun(phrases: Array<String>, count: Int): Array<String>{
            return phrases.filter {
                val labels = FiltersUtils.getFilterLabels(it) ?: return@filter true
                for (label in labels) {
                    if(!notCountFilter(label, count))  return@filter false
                }
                return@filter true
            }.toTypedArray()
        }


        public val countAnswer = fun(answers: Array<Answer>, count: Int): Array<Answer> {
            var maxCnt = 0;
            for (answer in answers) {
                val filterLabel =
                    FiltersUtils.getFirstFilterLabel(answer.text);
                if (filterLabel != null) {
                    val number = filterLabel.toIntOrNull() ?: continue
                    if (number > maxCnt) maxCnt = number
                }
            }
            return answers
                .filter {
                    val filterLabels = FiltersUtils.getFilterLabels(it.text) ?: return@filter true;
                    for (label in filterLabels) {
                        if (!countFilter(label, maxCnt, count)) return@filter false
                    }
                    return@filter true
                }.toTypedArray()
        }

        public val countPhrase = fun(phrases: Array<String>, count: Int): Array<String> {
            var maxCnt = 0;
            for (phrase in phrases) {
                val filterLabel = FiltersUtils.getFirstFilterLabel(phrase);
                if (filterLabel != null) {
                    val number = filterLabel.toIntOrNull() ?: continue
                    if (number > maxCnt) maxCnt = number
                }
            }
            return  phrases
                .filter {
                    val filterLabels = FiltersUtils.getFilterLabels(it) ?: return@filter true;
                    for (label in filterLabels) {
                        if (!countFilter(label, maxCnt, count)) return@filter false
                    }
                    return@filter true
                }
                .toTypedArray()
        }

        private fun countFilter(filterLabel: String, maxCnt: Int, count: Int) : Boolean{
            val lastFilter = "*"
            if(filterLabel == lastFilter){
                return count > maxCnt
            }
            else {
                val number = filterLabel.toIntOrNull() ?: return true
                return number == count
            }
        }

        private fun notCountFilter (label: String?, count: Int): Boolean {
            return label == null
                    || !label.startsWith("!")
                    || label.removePrefix("!").toIntOrNull() == null
                    || label.removePrefix("!").toIntOrNull() != count

        }

    }
}