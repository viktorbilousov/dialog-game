package tools

class FiltersUtils {
   companion object {
       public fun getFilterLabels(str: String): Array<String>? {
           val list = arrayListOf<String>()
           var line = str;
           while (getFirstFilterLabel(line) != null) {
               list.add(getFirstFilterLabel(line)!!);
               line = removeLastLabel(line)!!
           }
           if (list.isEmpty()) return null
           return list.toTypedArray();
       }

       public fun getFilterLabelsInsideText(str: String): Array<String>?{
           var label = ""
           val labelsList = arrayListOf<String>()
           var isFound = false;
           loop@ for(c in str){
               when(c) {
                   '[' -> {
                       isFound = true;
                       continue@loop;
                   }
                   ']' -> {
                       isFound = false;
                       if (label.isNotEmpty()){
                           labelsList.add(label)
                           label = ""
                       }
                       continue@loop;
                   }
                   else -> {
                       if(isFound) label += c
                   }
               }
           }
           if(labelsList.isEmpty()) return null;

           return labelsList.toTypedArray()
       }


       public fun getFirstFilterLabel(text: String): String? {
           if(text == "") return null;
           if (text.trim()[0] == '[' && text.trim().indexOf(']') > 1) {
               return text.trim().substring(1, text.trim().indexOf(']'))
           }
           return null
       }

       public fun removeLabels(str: String): String {
           var line = str;
           while (true) {
               val lastLabel = getFirstFilterLabel(line) ?: return line
               line = line.subSequence(lastLabel.length + 2, line.length).toString().trim()
           }
       }

       public fun removeLastLabel(str: String): String? {
           val lastLabel = getFirstFilterLabel(str) ?: return str
           return str.subSequence(lastLabel.length + 2, str.length).toString().trim()

       }

       public fun getParameterValue(label: String) : String?{
           if (!label.contains('=')) return null;
           val value = label.split("=")
           return value[1].trim();
       }

        public fun getParameterName(label: String) : String?{
           if (!label.contains('=')) return null;
           val value = label.split("=")
           return value[0].trim();
       }

       public fun isLabelParametric(label: String) : Boolean{
           val arr = label.split("=")
           return arr.size == 2 && arr[1].isNotEmpty()
       }
   }
}