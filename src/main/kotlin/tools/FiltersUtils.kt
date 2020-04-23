package tools

import phrases.filters.Labels

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

       public fun getSimpleLabels(str: String): Array<String>?{
           val arr = getFilterLabels(str) ?: return null
           return arr.filter { !isLabelParametric(it) }.toTypedArray()
       }

       public fun getParametricLabels(str: String): Array<String>?{
           val arr = getFilterLabels(str) ?: return null
           return arr.filter { isLabelParametric(it) }.toTypedArray()
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

       public fun indexOfLabelAfter(index: Int, label: Labels, textLabels: Array<String>): Int?{
           for (i in index until textLabels.size){
               var currLabel: String? = (if(isLabelParametric(textLabels[i])) getParameterName(textLabels[i])
               else textLabels[i])
                   ?: return null;

               currLabel = currLabel!!.toUpperCase()

               if(currLabel.startsWith(label.label.toUpperCase())) {
                   return i;
               };
           }
           return null;
       }
       public fun indexOfLabel(label: Labels, textLabels: Array<String>): Int?{
          return indexOfLabelAfter(0, label, textLabels)
       }

       public fun countOfLabels(textLabels: Array<String>, label: Labels) : Int{
           return textLabels.filter { it.startsWith(label.label) }.count()
       }

//       public fun getParameterLabelsAfter(label: Labels, line:  String): Array<String>?{
//           val labels = FiltersUtils.getFilterLabels(line) ?: return null
//           val list = arrayListOf<String>()
//           var addToList = false;
//           labels.forEach {
//               if(Labels.parse(it) == label) {
//                   addToList = true
//                   return@forEach
//               };
//               if(addToList) {
//                   if (isLabelParametric(it)) list.add(it)
//                   else addToList = false;
//               }
//           }
//           return list.toTypedArray()
//       }
   }
}