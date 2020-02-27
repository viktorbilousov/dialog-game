package minigames.tea.tools

import minigames.tea.models.MixedTea
import minigames.tea.models.Tea
import tools.Table

class MixedTeaTable(tea: MixedTea) {

    private val tasteTable = NamedTasteTable();

    public val table : Table
        get() = tasteTable.table;

   init {
       for (flower in tea.flowers) {
           tasteTable.addTaste(flower.name, flower.taste)
       }
   }

}