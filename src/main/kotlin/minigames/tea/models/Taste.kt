package minigames.tea.models

data class Taste(var taste: Int, var color: Int, var smell: Int, var vitamin: Int, var aftertaste: Int){

   companion object{
       fun empty() : Taste{
           return Taste(0,0,0,0,0)
       }
   }


    fun sum(taste: Taste){
        this.taste += taste.taste
        this.color += taste.color
        this.smell += taste.smell
        this.vitamin += taste.vitamin
        this.aftertaste += taste.aftertaste
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Taste

        if (taste != other.taste) return false
        if (color != other.color) return false
        if (smell != other.smell) return false
        if (vitamin != other.vitamin) return false
        if (aftertaste != other.aftertaste) return false

        return true
    }

    override fun hashCode(): Int {
        var result = taste
        result = 31 * result + color
        result = 31 * result + smell
        result = 31 * result + vitamin
        result = 31 * result + aftertaste
        return result
    }

    public fun toArray(): Array<Int>{
        return arrayOf(taste, color, smell, vitamin, aftertaste)
    }
}
