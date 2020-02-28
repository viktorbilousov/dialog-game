package minigames.tea.tools

import minigames.tea.models.Collection
import minigames.tea.models.Flower
import minigames.tea.models.MixedTea
import minigames.tea.models.Taste
import minigames.tea.models.Tea
import minigames.tea.tables.MixedTeaTable
import minigames.tea.tables.NamedTasteTable
import minigames.tea.tables.TeaTable
import models.Answer
import org.apache.commons.lang.StringUtils
import tools.CommonUtils
import tools.FiltersUtils
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

        fun getGameTable(mixedTea: MixedTea, goalTea: Tea): String {
            val summaryTaste = calcSummaryTea(mixedTea)
            val printer = TablePrinter()
            val mixedTeaTable = MixedTeaTable(mixedTea);
            val goalTeaTable = TeaTable(goalTea);
            val summaryTable = NamedTasteTable()
            summaryTable.addTaste("cумма", summaryTaste)
            return printer
                .table(mixedTeaTable)
                .resize(5)
                .line()
                .table(summaryTable)
                .table(goalTeaTable)
                .toPrettyString();

        }

        public fun getFlowersAsAnswers(onlyNames: Boolean): Array<Answer> {
            if (onlyNames) return Collection.getFlowers().map { Answer(it.name, it.name) }.toTypedArray()
            return Collection.getFlowers().map { Answer(it.name, it.toString()) }.toTypedArray()
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
            for (tea in Collection.getTeas()) {
                if(tea.name == answer.id) return tea;
            }
            return null
        }
        public fun answerToFlower(answer: Answer): Flower?{
            for (tea in Collection.getFlowers()) {
                if(tea.name == answer.id) return tea;
            }
            return null
        }

    }
}