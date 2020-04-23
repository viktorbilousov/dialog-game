package phrases.filters

enum class Labels (val label: String) {
    SET("SET"),
    UNSET("UNSET"),
    GET("GET"),
    NOT("NOT"),
    IF("IF"),
    FI("FI"),
    ELSE("ELSE"),
    ELSEIF("ELSE IF"),
    PUT("PUT"),
    DEBUG("DEBUG"),
    SETV("SETV"),
    UNSETV("UNSETV"),
    INT("INT"),
    GETV("GETV"),
    NOTV("NOTV"),
    OR("OR"),
    AND("AND");

    companion object {
        public fun parse(string: String): Labels? {
            values().forEach { if (it.label.toUpperCase() == string.toUpperCase()) return it }
            return null
        }

        public fun contains(string: String): Boolean {
            return parse(string) != null;
        }
    }
}