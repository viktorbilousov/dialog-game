package dialog.game.phrases.filters

enum class FilterLabel (val label: String, val intRange : Int = 0) {

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


    // <- PutFilter ->
    PUT("PUT"),

    //  <- IncertFilter ->

    INST("INST"),


    //<- DebugFilter ->
    DEBUG("DEBUG"),


    //<- ParamSetBooleanFilter ->
    SETV("SETV", 1),
    UNSETV("UNSETV"),


    //<- IntComparingFilter ->
    INT("INT", 1),


    //<- GetVariableFilter ->
    GETV("GETV", 1),
    NOTV("NOTV"),

    //<- JoinVariableFilter ->
    JOIN("JOIN"),

    //<- MailFilter ->
    MAIL("MAIL"),
    //<- RAND ->
    RAND("RAND"),

    //<- IntSimpleArithmeticsFilter ->
    SETI("SETI",1),

    //<- CookiesPhrase ->
    INIT_CUP("INIT_CUP"),
    RESET_CUP("RESET_CUP"),
    EMPTY_CUP("EMPTY_CUP"),
    POUR_CUP("POUR_CUP",1);


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