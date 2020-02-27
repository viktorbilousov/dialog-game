package minigames.tea.tools

import minigames.tea.models.Taste
import minigames.tea.models.Tea
import tools.Table

class NamedTasteTable() {
    val table = Table()

    val legend = hashMapOf(Pair("В", "Вкус"), Pair("Ц", "Цвет"), Pair("З", "Запах"), Pair("В", "Витаминность"), Pair("П", "Послевкусие"))

    init {
        table.addEmptyColumns("Имя", "В", "Ц","З","В","П")
    }

     fun addTaste(name: String, taste: Taste){
         table.addRow(mapOf(Pair("Имя", name), Pair( "В", getNumWithSign(taste.taste)), Pair( "Ц", getNumWithSign(taste.color)),
             Pair( "З", getNumWithSign(taste.smell)), Pair("В", getNumWithSign(taste.vitamin)),
             Pair("П", getNumWithSign(taste.aftertaste))))
     }

   private fun getNumWithSign(num: Int) : String{
        if(num >= 0) return "+$num"
        return "$num"
    }
}