package minigames.tea.tools

import minigames.tea.models.Tea
import tools.Table

class TeaTable() {
    private val tasteTable = NamedTasteTable();

    public val table : Table
    get() = tasteTable.table;

   fun add(tea: Tea){
        tasteTable.addTaste(tea.name, tea.taste)
    }

    fun addAll(array: Array<Tea>){
        array.forEach { add(it) }
    }

}