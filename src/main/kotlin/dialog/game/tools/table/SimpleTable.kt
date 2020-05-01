package tools.table

import kotlin.math.max

open class SimpleTable : Table {

    public override val columns = linkedMapOf<String,ArrayList<Any?>>()
    public override val rows : List<LinkedHashMap<String, Any?>>
    get() {
       val list = List(rowsCnt){ LinkedHashMap<String, Any?>() }
        for (entry in columns) {
            entry.value.forEachIndexed{ index, value ->
                list[index][entry.key] = value;
            }
        }
        return list;
    }
    public override val columnsNames : Array<String>
    get() = columns.keys.toTypedArray()


    public override val columnsCnt : Int
        get() = columns.size

    public override val rowsCnt : Int
        get()  {
            if(columns.size == 0) return 0;
            return columns.values.map { it.size }.max()!!;
        }


    public fun addEmptyColumns(vararg columnNames: String) {
        for (columnName in columnNames) {
            if( columns[columnName] == null)  columns[columnName] = ArrayList();
        }
    }

    public override fun addColumn(columnName: String, column : List<Any?>){
        if( columns[columnName] == null)  columns[columnName] = ArrayList();
        columns[columnName]!!.addAll(column);
    }

    public fun addValue(columnName: String, value : Any?) {
        if(columns[columnName] == null) columns[columnName] = ArrayList();
        columns[columnName]!!.add(value)
    }
    public override fun addRow(row: Map<String, Any?>) {
        for (columnName in row.keys) {
            if(columns[columnName] == null) columns[columnName] = ArrayList();
            columns[columnName]!!.add(row[columnName])
        }
    }

    public override fun removeRow(index: Int): Map<String, Any?>? {
        val map = HashMap<String, Any?>()
        for (column in columns) {
            map[column.key] = column.value.removeAt(index);
        }
        return map;
    }

    public override fun removeColumn(columnName: String): ArrayList<Any?>? {
       return columns.remove(columnName);
    }

    public fun merge(table: Table){

        table.columnsNames.forEach {
            if(this.columns[it] == null) {
                addEmptyColumns(it)
                for (i in 0 until rowsCnt){
                    addValue(it, null)
                }
            }
        }
        table.columns.forEach{
            addColumn(it.key, it.value);
        }
    }
}