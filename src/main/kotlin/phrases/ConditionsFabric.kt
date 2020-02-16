package phrases

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
                } else if (maxCnt < count) {
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
            return filteredList.toTypedArray()
        }
        
        private fun getFilterLabel(text: String): String? {
            if( text.trim()[0] == '[' && text.trim().indexOf(']') > 1 ) {
                return text.trim().substring(0, text.trim().indexOf(']')+1)
            }
            return null
        } 
    }
}