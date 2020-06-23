package dialog.game.phrases.filters.labels


class FilterLabel(
    public val name: String,
    public val intRange : Int = 0
)
{
    override fun toString(): String {
        return "[$name]"
    }
}