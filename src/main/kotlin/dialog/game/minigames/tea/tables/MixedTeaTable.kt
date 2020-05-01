package dialog.game.minigames.tea.tables

import dialog.game.minigames.tea.models.MixedTea

class MixedTeaTable(tea: MixedTea) : NamedTasteTable() {
   init {
       for (flower in tea.flowers) {
           addTaste(flower.name, flower.taste)
       }
   }
}