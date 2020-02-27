package tools

import java.lang.StringBuilder

class TablePrinter(private val table: Table) {

    private val headSeparator = "-"
    private val columnsSeparator = "  |  "

    private val columnsSizes = hashMapOf<String, Int>()

    private fun recalcColumnSizes() {
        table.columns.entries.forEach { entry ->
            val max = entry.value
                .map {
                    if (it == null) return@map "NULL"
                    return@map it
                }
                .map { it.toString().length }.max()!!

            if (columnsSizes[entry.key] == null) columnsSizes[entry.key] = max;
            else if (columnsSizes[entry.key]!! < max) columnsSizes[entry.key] = max;
        }
    }

    fun print(printHead: Boolean = true){
        println(toPrettyString(printHead))
    }

    fun toPrettyString(printHead: Boolean = true): String {
        recalcColumnSizes()
        val head = printHead()
        val body = printBody()
        head.addAll(body);
        return head.joinToString(separator = "\n") { it }
    }


    private fun printBody(): ArrayList<String> {
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

    private fun printHead(): ArrayList<String> {
        val res = arrayListOf("", "")
        val commonSize = columnsSizes.values.sum() + columnsSizes.size*this.columnsSeparator.length - 1;
        for (i in 1..commonSize) {
            res[1] += headSeparator
        }
        table.columnsNames.forEach { res[0] += printWord(it, columnsSizes[it]!!, true) + columnsSeparator }
        res[0] = res[0].substring(0, res[0].lastIndex - 1)
        return res;
    }

    private fun printWord(string: String, size: Int, centred: Boolean = false): String {
        var res = string.trim();
        var i = 0;
        while (res.length < size) {
            if(i %2 == 0) res += " "
            else res = " $res";
            if(centred) i++;
        };

        return res;
    }
}
