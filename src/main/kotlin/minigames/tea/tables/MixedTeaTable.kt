package minigames.tea.tables

import minigames.tea.models.MixedTea

class MixedTeaTable(tea: MixedTea) : NamedTasteTable() {
   init {
       for (flower in tea.flowers) {
           addTaste(flower.name, flower.taste)
       }
   }
}