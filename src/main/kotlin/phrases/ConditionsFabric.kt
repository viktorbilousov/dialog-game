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
    }
}