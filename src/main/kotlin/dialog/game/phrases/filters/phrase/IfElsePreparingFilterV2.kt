package dialog.game.phrases.filters.phrase

import dialog.game.phrases.filters.FilterLabel
import dialog.game.phrases.filters.PhraseFilter
import dialog.system.models.Answer

import tools.FiltersUtils
import java.lang.IllegalArgumentException


/**
 * [IF/ ELSE IF][if/else if condition]
 * [text condition1] text1
 * [text condition2] text1
 *
 *
 * ---- example 1
 * [IF][if condition][THEN][text condition1] text1
 * [text condition2] text2
 * [ELSE IF][else if condition][THEN][text condition] text
 * [ElSE][else condition] text
 *  transform to
 *
 * [IF_SYS]
 * [if condition][text condition1] text2
 * [if condition][text condition2] text1
 * [ELSEIF_SYS][else if condition]
 * [text condition] text
 * [ElSE_SYS]
 * text
 * [FI_SYS]
 * ----
 *
 *
 * [IF][if conditions] text
 * [ELSE IF][else if condition] text
 * [ElSE][else condition] text
 *
 */
class IfElsePreparingFilterV2() : PhraseFilter {
    override fun filterPhrases(phrases: Array<String>, count: Int): Array<String> {
        var result = mutableListOf<String>()
        phrases.forEach {
            val brokeLine = makeLineBreak(it);
            if(brokeLine.size == 1){
                result.add(brokeLine[0])
            }else if(brokeLine.size == 2){
                result.add(brokeLine[0])
                result.add(brokeLine[1])
            }
        }
        val indexes = getIndexesToAddFILabel(result)
        indexes.reversedArray().forEach {
            if(result.size == it) {
                result.add("[FI]")
            }else {
                result.add(it, "[FI]")
            }
        }


        return result.map { transformLabel(it) }.toTypedArray()
    }
    override fun filterAnswers(answer: Array<Answer>, count: Int): Array<Answer> {
       val result = mutableListOf<Answer>()
        answer.forEach { 
            val brokeLine = makeLineBreak(it.text);
            if(brokeLine.size == 1){
                result.add(Answer(it.id, brokeLine[0], it.type))
            }else if(brokeLine.size == 2){
                val label = FiltersUtils.getFilterLabels(brokeLine[0])!![0].label;
                result.add(Answer("${it.id}.${label}", brokeLine[0], it.type))
                result.add(Answer(it.id, brokeLine[1], it.type))
            }
        }
        val indexes = getIndexesToAddFILabel(result.map { it.text })
        indexes.reversedArray().forEach {
            if(result.size == it )             {
                val answerId = result[it-1].id;
                result.add(Answer("${answerId}.FI", "[FI]"))
            }
            else {
                val answerId = result[it].id;
                result.add(it, Answer("${answerId}.FI", "[FI]"))
            }
        }

        result.map { it.text = transformLabel(it.text) }
        return result.toTypedArray();
        
    }

    private fun isNeedBreak(itemText: String) : Boolean{
        val label = FiltersUtils.getFirstFilterLabel(itemText) ?: return false
        return when(label){
            FilterLabel.IF, FilterLabel.ELSE, FilterLabel.ELSEIF -> {
                FiltersUtils.removeFirstLabel(itemText)!!.trim() != ""
            }
            else -> false
        }
    }

    private fun makeBreak(string: String) : Array<String>{
        val firstLabel = FiltersUtils.getFirstFilterLabel(string)
            ?: throw IllegalArgumentException("label not found");
        val res = arrayOf("","")
        res[0] = firstLabel.toString()
        res[1] = FiltersUtils.removeFirstLabel(string)!!.trim()
        return res;
    }


    
    private fun transformLabel(string: String): String{
        val firstLabel = FiltersUtils.getFirstFilterLabel(string) ?: return string;
        val res = FiltersUtils.removeFirstLabel(string);
        val message = "this is system tag and must be removed"
        return when(firstLabel){
            FilterLabel.IF -> "${FilterLabel.IF_SYS}$res $message"
            FilterLabel.ELSEIF -> "${FilterLabel.ELSEIF_SYS}$res $message"
            FilterLabel.ELSE -> "${FilterLabel.ELSE_SYS}$res $message"
            FilterLabel.FI -> "${FilterLabel.FI_SYS}$res $message"
            else -> return string;
        }
    }
    


    private var condition = "";

    private fun makeLineBreak(line: String) : Array<String>{
        val isContainLabel = isContainConditionsLabel(line);
        val res = arrayListOf<String>()
        if(isContainLabel) {
            condition = ""
            /* [IF/ELSE IF] [condition] text
             ---- to -----
             [IF/ELSE IF]
             [condition] text */
            if(isNeedBreak(line)){
                res.addAll(makeBreak(line))
              /*   if after break last line has only conditions labels  ( IF, ELSE was only condition without text)
                 and text must to be below ( (in next line)
                 so save condition and remove this line
                 ex: [IF/ELSE IF] [condition]
                 text 1
                 text 2
                 to
                 [IF/ELSE IF]
                 [condition] text1
                 [condition] text2
                */
                if(FiltersUtils.removeLabels(res.last()) == ""){
                    condition = res.last(); // IF ELSE condition save and add to text below
                    res.removeAt(res.size-1);
                }
                return res.toTypedArray()
            }
        }
        // if line goes between IF(else if) and else if
        // add condition from IF/ElseIf (if exits) to line
        res.add(condition + line);
        return res.toTypedArray()

    }

    
    private fun getIndexesToAddFILabel(lines: Collection<String>): Array<Int>{
        val list = arrayListOf<Int>()

        var lastElseLabelIndex: Int? = null;
        
        lines.forEachIndexed{ index, line ->
            
            val label = FiltersUtils.getFirstFilterLabel(line) ?: return@forEachIndexed
            
            when(label){
                FilterLabel.ELSE -> lastElseLabelIndex = index;
                FilterLabel.FI -> lastElseLabelIndex = null;
                FilterLabel.IF, FilterLabel.ELSEIF -> {
                    if(lastElseLabelIndex != null) // FI not found
                    { 
                        // [ELSE]    +0
                        // some text +1 
                        // [FI]      +2
                        list.add(lastElseLabelIndex!! + 2)
                        lastElseLabelIndex = null;
                    }
                }
            }
        }
        if(lastElseLabelIndex != null){
            list.add(lastElseLabelIndex!! + 2)
        }
        return list.toTypedArray();
        
    } 

    private fun isContainConditionsLabel(str: String) : Boolean{
        val labels = FiltersUtils.getFilterLabels(str) ?: return false;

        return  labels.contains(FilterLabel.IF)
                || labels.contains(FilterLabel.ELSEIF)
                || labels.contains(FilterLabel.ELSE)
                || labels.contains(FilterLabel.FI)
    }

}