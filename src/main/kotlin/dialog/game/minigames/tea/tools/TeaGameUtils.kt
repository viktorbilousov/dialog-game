package dialog.game.minigames.tea.tools

import dialog.game.minigames.tea.models.Collection
import dialog.game.minigames.tea.models.Flower
import dialog.game.minigames.tea.models.MixedTea
import dialog.game.minigames.tea.models.Taste
import dialog.game.minigames.tea.models.Tea
import dialog.game.minigames.tea.tables.MixedTeaTable
import dialog.game.minigames.tea.tables.NamedTasteTable
import dialog.game.minigames.tea.tables.TeaTable
import dialog.system.models.Answer
import org.apache.commons.lang.StringUtils
import tools.CommonUtils
import tools.table.TablePrinter

class TeaGameUtils{

    companion object {

        private const val spaceCnt = 5;
        private const val namespaceCnt = 12;

        private fun calcSummaryTea(mixedTea: MixedTea): Taste {
            val summaryTaste = Taste.empty()
            for (flower in mixedTea.flowers) {
                summaryTaste.sum(flower.taste)
            }
            return summaryTaste
        }

        fun getGameTable(mixedTea: MixedTea, goalTea: Tea?): String {
            val summaryTaste = calcSummaryTea(mixedTea)
            val printer = TablePrinter()
            val mixedTeaTable = MixedTeaTable(mixedTea);
            val summaryTable = NamedTasteTable()

            var goalTeaTable : TeaTable? = null;
            if(goalTea != null)
                goalTeaTable = TeaTable(goalTea)


            summaryTable.addTaste("cумма", summaryTaste)
            val res = printer
                .table(mixedTeaTable)
                .resize(5)
                .line()
                .table(summaryTable)

            if(goalTea != null) res.table(goalTeaTable!!)

            return res.toPrettyString()

        }

        public fun getFlowersAsAnswers(onlyNames: Boolean): Array<Answer> {
            if (onlyNames) return Collection.getFlowers().map { Answer(it.name, it.name) }.toTypedArray()
            return Collection.getFlowers().map { Answer(it.name,
                "${it.name}${getSpaces(12 - it.name.length)}" +
                        "B=${getNumWithSign(it.taste.taste)}\t" +
                        "Ц=${getNumWithSign(it.taste.color)}\t" +
                        "З=${getNumWithSign(it.taste.smell)}\t" +
                        "Вит=${getNumWithSign(it.taste.vitamin)}\t" +
                        "П=${getNumWithSign(it.taste.aftertaste)}") }.toTypedArray()
        }

        public fun getTeasAsAnswers(onlyNames: Boolean): Array<Answer> {
            if (onlyNames) return Collection.getTeas().map { Answer(it.name, it.name) }.toTypedArray()
            return Collection.getTeas().map { Answer(it.name, it.toString()) }.toTypedArray()
        }

        public fun getAnswerLegendString(): String {
            var arr = NamedTasteTable.legend.keys
                .toList()
                .map { StringUtils.center(it, spaceCnt) }
                .joinToString(separator = "") { it }
            return "${CommonUtils.spaces(namespaceCnt + 3)} $arr"
        }

        public fun getFlowerString(flower: Flower): String {
            var arr = flower.taste
                .toArray()
                .map { StringUtils.rightPad(CommonUtils.intToStr(it), spaceCnt) }
                .joinToString(separator = "") { it }
            return "${StringUtils.rightPad(flower.name, namespaceCnt)} $arr"
        }

        public fun getLegend(): String {
            return NamedTasteTable.legend.map { "${it.key}=${it.value} " }.joinToString(separator = "") { it }
        }


        public fun answerToTea(answer: Answer): Tea?{
            val name = answer.text.split(" ")[0].trim();
            for (tea in Collection.getTeas()) {
                if(tea.name == name) return tea
            }
            return null
        }
        public fun answerToFlower(answer: Answer): Flower?{
            val name = answer.text.split(" ")[0].trim();
            for (tea in Collection.getFlowers()) {
                if(tea.name == name) return tea;
            }
            return null
        }

        public fun getNumWithSign(num: Int) : String{
            if(num >= 0) return "+$num"
            return "$num"
        }
        public fun getSpaces(num: Int) : String{
            var spaces = "";
            while (spaces.length < num) spaces += " ";
            return spaces;
        }

    }
}