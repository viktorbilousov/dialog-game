package dialog.game.phrases.filters.labels

class FilterLabelCollection() {

    val filters: HashMap<String, FilterLabel>


    init {
        filters = hashMapOf<String, FilterLabel>();
        filters["GET"] = FilterLabel("GET")
        filters["NOT"] = FilterLabel("NOT")
        // <- SetBooleanFilter ->
        filters["SET"] = FilterLabel("SET")
        filters["UNSET"] = FilterLabel("UNSET")
        // <- IfElseFilter, IfElsePreparation ->
        filters["IF"] = FilterLabel("IF")
        filters["ELSE"] = FilterLabel("ELSE")
        filters["ELSEIF"] = FilterLabel("ELSE IF")
        filters["FI"] = FilterLabel(" FI")

        // <- IfElseFilterV2 ->
        filters["IF_SYS"] = FilterLabel("IF_SYS")
        filters["ELSE_SYS"] = FilterLabel("ELSE_SYS")
        filters["ELSEIF_SYS"] = FilterLabel("ELSEIF_SYS")
        filters["FI_SYS"] = FilterLabel("FI_SYS")

        // <- PutFilter ->
        filters["PUT"] = FilterLabel("PUT")

        //  <- IncertFilter ->

        filters["INST"] = FilterLabel("INST")
        //<- DebugFilter ->
        filters["DEBUG"] = FilterLabel("DEBUG")

        //<- ParamSetBooleanFilter ->
        filters["SETV"] = FilterLabel("SETV", 1)
        filters["UNSETV"] = FilterLabel("UNSETV")


        //<- IntComparingFilter ->
        filters["INT"] = FilterLabel("INT", 1)


        //<- GetVariableFilter ->
        filters["GETV"] = FilterLabel("GETV", 1)
        filters["NOTV"] = FilterLabel("NOTV")

        //<- JoinVariableFilter ->
        filters["JOIN"] = FilterLabel("JOIN")

        //<- MailFilter ->
        filters["MAIL"] = FilterLabel("MAIL")
        //<- = _root_ide_package_.dialog.game.phrases.filters.labels.FilterLabel RAND ->
        filters["RAND"] = FilterLabel("RAND")

        //<- IntSimpleArithmeticsFilter ->
        filters["SETI"] = FilterLabel("SETI", 1)

        //<- CookiesPhrase ->
        filters["INIT_CUP"] = FilterLabel("INIT_CUP")
        filters["RESET_CUP"] = FilterLabel("RESET_CUP")
        filters["EMPTY_CUP"] = FilterLabel("EMPTY_CUP")
        filters["POUR_CUP"] = FilterLabel("POUR_CUP", 1);
    }

    val GET: FilterLabel
        get() = filters["GET"]!!
    val NOT: FilterLabel
        get() = filters["NOT"]!!

    // <- SetBooleanFilter ->
    val SET: FilterLabel
        get() = filters["SET"]!!
    val UNSET: FilterLabel
        get() = filters["UNSET"]!!

    // <- IfElseFilter, IfElsePreparation ->
    val IF: FilterLabel
        get() = filters["IF"]!!
    val ELSE: FilterLabel
        get() = filters["ELSE"]!!
    val ELSEIF: FilterLabel
        get() = filters["ELSE"]!!
    val FI: FilterLabel
        get() = filters["FI"]!!

    // <- IfElseFilterV2 ->
    val IF_SYS: FilterLabel
        get() = filters["IF_SYS"]!!
    val ELSE_SYS: FilterLabel
        get() = filters["ELSE_SYS"]!!
    val ELSEIF_SYS: FilterLabel
        get() = filters["ELSEIF_SYS"]!!
    val FI_SYS: FilterLabel
        get() = filters["FI_SYS"]!!

    // <- PutFilter ->
    val PUT: FilterLabel
        get() = filters["PUT"]!!

    //  <- IncertFilter ->

    val INST: FilterLabel
        get() = filters["INST"]!!

    //<- DebugFilter ->
    val DEBUG = filters["DEBUG"]!!

    //<- ParamSetBooleanFilter ->
    val SETV: FilterLabel
        get() = filters["SETV"]!!
    val UNSETV: FilterLabel
        get() = filters["UNSETV"]!!


    //<- IntComparingFilter ->
    val INT: FilterLabel
        get() = filters["INT"]!!


    //<- GetVariableFilter ->
    val GETV: FilterLabel
        get() = filters["GETV"]!!
    val NOTV: FilterLabel
        get() = filters["NOTV"]!!

    //<- JoinVariableFilter ->
    val JOIN: FilterLabel
        get() = filters["JOIN"]!!

    //<- MailFilter ->
    val MAIL: FilterLabel
        get() = filters["MAIL"]!!

    //<- : FilterLabel
    val RAND: FilterLabel
        get() = filters["RAND"]!!

    //<- IntSimpleArithmeticsFilter ->
    val SETI: FilterLabel
        get() = filters["SETI"]!!

    //<- CookiesPhrase ->
    val INIT_CUP: FilterLabel
        get() = filters["INIT_CUP"]!!
    val RESET_CUP: FilterLabel
        get() = filters["RESET_CUP"]!!
    val EMPTY_CUP: FilterLabel
        get() = filters["EMPTY_CUP"]!!
    val POUR_CUP: FilterLabel
        get() = filters["POUR_CUP"]!!


    public fun parse(string: String): FilterLabel? {
        filters.values.forEach { if (it.name.toUpperCase() == string.toUpperCase()) return it }
        return null
    }

    public fun contains(string: String): Boolean {
        return parse(string) != null;
    }

}