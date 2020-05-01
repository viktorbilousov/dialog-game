package dialog.game.minigames.tea.models

class MixedTea(): Tea("mixedTea", Taste(0,0,0,0,0)){

    public val flowers : Array<Flower>
    get() = flowersList.toTypedArray();

    private val flowersList = ArrayList<Flower>()

    public fun addFlower(flower: Flower){
        taste.sum(flower.taste)
        flowersList.add(flower);
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MixedTea

        if (taste != other.taste) return false

        return true
    }

    override fun hashCode(): Int {
        var result = taste.hashCode() * 31;
        return result
    }

    override fun toString(): String {
        return "$taste\t${flowers.map { it.name }.toList().toTypedArray().contentToString()}"
    }

}