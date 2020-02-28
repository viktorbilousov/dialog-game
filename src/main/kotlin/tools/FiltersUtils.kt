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


       public fun getFirstFilterLabel(text: String): String? {
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
   }
}