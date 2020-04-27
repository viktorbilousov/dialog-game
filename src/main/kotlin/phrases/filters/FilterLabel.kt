package phrases.filters

enum class FilterLabel (val label: String) {

    // <- GetBooleanFilter ->
    GET("GET"),
    NOT("NOT"),


    // <- SetBooleanFilter ->
    SET("SET"),
    UNSET("UNSET"),


    // <- IfElseFilter, IfElsePreparation ->
    IF("IF"),
    ELSE("ELSE"),
    ELSEIF("ELSE IF"),
    FI("FI"),

    // <- IfElseFilterV2 ->
    IF_SYS("IF_SYS"),
    ELSE_SYS("ELSE_SYS"),
    ELSEIF_SYS("ELSEIF_SYS"),
    FI_SYS("FI_SYS"),


    // <- ReplaceAnswerFilter, ReplacePhraseFilter ->
    PUT("PUT"),


    //<- DebugFilter ->
    DEBUG("DEBUG"),


    //<- ParamSetBooleanFilter ->
    SETV("SETV"),
    UNSETV("UNSETV"),


    //<- IntComparingFilter ->
    INT("INT"),


    //<- GetVariableFilter ->
    GETV("GETV"),
    NOTV("NOTV")

    ;

    companion object {
        public fun parse(string: String): FilterLabel? {
            values().forEach { if (it.label.toUpperCase() == string.toUpperCase()) return it }
            return null
        }

        public fun contains(string: String): Boolean {
            return parse(string) != null;
        }
    }

    override fun toString(): String {
        return "[$label]"
    }
}