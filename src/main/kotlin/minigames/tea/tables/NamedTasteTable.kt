package minigames.tea.tables

import minigames.tea.models.Taste
import tools.table.SimpleTable

open class NamedTasteTable : SimpleTable() {

    companion object {
        public val legend = linkedMapOf(
            Pair("В", "Вкус"),
            Pair("Ц", "Цвет"),
            Pair("З", "Запах"),
            Pair("Вит", "Витаминность"),
            Pair("П", "Послевкусие")
        )
    }
    init {
        addEmptyColumns("Имя", "В", "Ц","З","Вит","П")
    }

     fun addTaste(name: String, taste: Taste){
         addRow(mapOf(Pair("Имя", name), Pair( "В", getNumWithSign(taste.taste)), Pair( "Ц", getNumWithSign(taste.color)),
             Pair( "З", getNumWithSign(taste.smell)), Pair("Вит", getNumWithSign(taste.vitamin)),
             Pair("П", getNumWithSign(taste.aftertaste))))
     }

   private fun getNumWithSign(num: Int) : String{
        if(num >= 0) return "+$num"
        return "$num"
    }
}