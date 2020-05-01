package tools.table

interface Table {

    val columnsCnt : Int
    val rowsCnt: Int
    val columns : LinkedHashMap<String, ArrayList<Any?>>
    val rows : List<LinkedHashMap<String, Any?>>
    val columnsNames : Array<String>

    fun addRow(row: Map<String, Any?>)
    fun addColumn(columnName: String, column : List<Any?>)
    fun removeRow(index: Int) :  Map<String, Any?>?
    fun removeColumn(columnName: String) : ArrayList<Any?>?

    fun isEmpty() : Boolean {return rowsCnt == 0}

}