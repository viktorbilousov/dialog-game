package dialog.game.minigames.tea.tables

import dialog.game.minigames.tea.models.Tea

class TeaTable() : NamedTasteTable() {

    constructor(tea: Tea) : this(){
        add(tea)
    }

   fun add(tea: Tea): TeaTable {
        addTaste(tea.name, tea.taste)
       return this;
    }

    fun addAll(array: Array<Tea>) : TeaTable {
        array.forEach { add(it) }
        return this;
    }

}