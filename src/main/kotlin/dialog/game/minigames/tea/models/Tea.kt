package dialog.game.minigames.tea.models

open class Tea(val name: String, val taste: Taste){
    constructor( name: String, taste: Int,  color: Int,  smell: Int, vitamin: Int,  aftertaste: Int)
            : this(name, Taste(taste, color, smell, vitamin, aftertaste))

    override fun toString(): String {
        return "name=$name, $taste"
    }
}