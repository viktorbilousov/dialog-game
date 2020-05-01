package tools.table

import org.apache.commons.lang.StringUtils
import java.lang.StringBuilder

class TablePrinter() {

    private val headSeparator = "-"
    private val columnsSeparator = "  |  "

    private val columnsSizes = hashMapOf<String, Int>()
    private val table = SimpleTable();

    private val lineIndex = sortedSetOf<Int>()


    public fun table( table: Table) : TablePrinter{
        this.table.merge(table);
        return this;
    }

    public fun emptyRows(cnt: Int) : TablePrinter{
        val emptyRow = table.columnsNames.associate { Pair(it, "") }
        for (i in 0 until cnt){
            table.addRow(emptyRow)
        }
        return this;
    }

    public fun line()  : TablePrinter{
        lineIndex.add(table.rowsCnt)
        return this;
    }

    public fun print(printHead: Boolean = true){
        println(toPrettyString(printHead))
    }

    public fun resize(cnt: Int) : TablePrinter{
        val emptyRow = table.columnsNames.associate { Pair(it, "") }
        val rowsCnt = table.rowsCnt
        for (i in rowsCnt until cnt){
            table.addRow(emptyRow)
        }
        return this;
    }


    public fun toPrettyString(printHead: Boolean = true): String {
        recalcColumnSizes()

        val result = arrayListOf<String>()
        if(printHead) result.addAll(buildHead())
        result.addAll( buildBody());

        lineIndex.reversed().forEach {
            var index = it;
            if(printHead) index++;
            if(result.lastIndex > index) result.add(index, getLine())
        }

        return result.joinToString(separator = "\n") { it }
    }




    private fun recalcColumnSizes() {

            table.columnsNames.forEach {
                if(columnsSizes[it] == null || columnsSizes[it]!! < it.length)  columnsSizes[it] = it.length;
            }

            if (!table.isEmpty()) {
                table.columns.entries.forEach { entry ->
                    val max = entry.value
                        .map {
                            if (it == null) return@map "NULL"
                            return@map it
                        }
                        .map { it.toString().length }.max()!!

                    if (columnsSizes[entry.key]!! < max) columnsSizes[entry.key] = max;
                }
            }


    }

    private fun buildBody(): ArrayList<String> {
        val arrayList = arrayListOf<String>()
        for (i in 0 until table.rowsCnt) {
            var line = StringBuilder()
            for (entry in table.columns) {
                val word = entry.value[i] ?: "NULL";
                line = line.append(printWord(word.toString(), columnsSizes[entry.key]!!)).append(this.columnsSeparator)
            }
            line = line.delete(line.lastIndex - 1, line.lastIndex)
            arrayList.add(line.toString())
        }
        return arrayList
    }

    private fun buildHead(): ArrayList<String> {
        lineIndex.add(0);
        val res = arrayListOf("")
        table.columnsNames.forEach{ res[0] += printWord(it, columnsSizes[it]!!, true) + columnsSeparator }
        res[0] = res[0].substring(0, res[0].lastIndex - 1)
        return res;
    }

    private fun buildEmptyRows(cntRows: Int): ArrayList<String>{
        val arrayList = arrayListOf<String>()
        for (i in 0 until cntRows){
            var line = StringBuilder()
            for (entry in table.columns) {
                line = line.append(printWord("", columnsSizes[entry.key]!!)).append(this.columnsSeparator)
            }
            line = line.delete(line.lastIndex - 1, line.lastIndex)
            arrayList.add(line.toString())
        }
        return arrayList;
    }

    private fun printWord(string: String, size: Int, centred: Boolean = false): String {
        if(centred) return StringUtils.center(string, size)
         return StringUtils.rightPad(string, size)
    }

    private fun getLine() : String{
        var res = ""
        val commonSize = columnsSizes.values.sum() + columnsSizes.size*this.columnsSeparator.length - 1;
        for (i in 1..commonSize) {
            res += headSeparator
        }
        return res;
    }

}
