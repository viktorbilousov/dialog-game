package phrases.filters.phrase

import models.Answer
import phrases.filters.FilterLabel
import phrases.filters.PhraseFilter
import tools.FiltersUtils
import java.lang.IllegalArgumentException

class IfElsePreparingFilter() : PhraseFilter {
    override fun filterPhrases(phrases: Array<String>, count: Int): Array<String> {
        val list = arrayListOf<String>()
        phrases.forEach {
            if (isNeedBreak(it)){
                list.addAll(makeBreak(it))
                if(FiltersUtils.getFirstFilterLabel(it) == FilterLabel.ELSE){
                    list.add(FilterLabel.FI_SYS.toString())
                }
            }
            else{
                list.add(it)
            }
        }
       return list.map { transformLabel(it) }.toTypedArray()
    }
    override fun filterAnswers(answer: Array<Answer>, count: Int): Array<Answer> {
        val list = arrayListOf<Answer>()
        answer.forEach {
            if (isNeedBreak(it.text)){
                list.addAll(makeBreak(it))
                if(FiltersUtils.getFirstFilterLabel(it.text) == FilterLabel.ELSE){
                    list.add(Answer("${it.id}.FI", "${FilterLabel.FI_SYS}" ))
                }
            }else{
                list.add(it);
            }
        }
        return list.map { Answer(it.id, transformLabel(it.text), it.type) }.toTypedArray()
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
        val res = arrayOf("", "")
        res[0] = firstLabel.toString()
        res[1] = FiltersUtils.removeFirstLabel(string)!!.trim()
        return res;
    }

    private fun makeBreak(answer: Answer) : Array<Answer>{
        val firstLabel = FiltersUtils.getFirstFilterLabel(answer.text)
            ?: throw IllegalArgumentException("label not found");
        return arrayOf(
            Answer("${answer.id}.${firstLabel.name}", firstLabel.toString() ),
            Answer(answer.id, FiltersUtils.removeFirstLabel(answer.text)!!.trim(), answer.type)
        );
    }

    private fun transformLabel(string: String): String{
        val firstLabel = FiltersUtils.getFirstFilterLabel(string) ?: return string;
        val res = FiltersUtils.removeFirstLabel(string);
        val message = "this is system tag and must be removed"
        return when(firstLabel){
            FilterLabel.IF -> "${FilterLabel.IF_SYS}$res $message"
            FilterLabel.ELSEIF -> "${FilterLabel.ELSEIF_SYS}$res $message"
            FilterLabel.ELSE -> "${FilterLabel.ELSE_SYS}$res $message"
            FilterLabel.FI_SYS -> "${FilterLabel.FI_SYS} $message"
            FilterLabel.FI -> "${FilterLabel.FI_SYS} $message"
            else -> return string;
        }
    }
}