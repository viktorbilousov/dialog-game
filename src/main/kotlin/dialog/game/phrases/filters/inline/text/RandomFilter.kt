package dialog.game.phrases.filters.inline.text

import dialog.game.phrases.filters.FilterLabel
import dialog.game.phrases.filters.InlineTextPhraseFilter
import dialog.system.models.Answer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tools.FiltersUtils
import java.lang.IllegalStateException
import java.util.*
import kotlin.collections.HashMap

//todo add RandomFilter to tests
/**
 * [RAND]
 * [RAND=group1]
 * [RAND=group2]
 */
class RandomFilter : InlineTextPhraseFilter(){


    private val randNumOfGroup = HashMap<String, Int>()
    private val cntOfGroup = HashMap<String, Int>()
    private val DEF_GROUP_KEY= "__def";

    companion object{
        private val logger = LoggerFactory.getLogger(RandomFilter::class.java) as Logger
    }

    override fun filterText(itemText: String, count: Int): Boolean {
        val label = findRandLabel(itemText) ?: return true;
        val group = FiltersUtils.getParameterValue(label) ?: DEF_GROUP_KEY;
        cntOfGroup[group] = cntOfGroup[group]!! + 1
        return randNumOfGroup[group] == cntOfGroup[group]
    }


    override fun filterPhrasesLogic(phrases: Array<String>, count: Int): Array<String> {
        init(phrases)
        return super.filterPhrasesLogic(phrases, count)
    }

    override fun filterAnswersLogic(answer: Array<Answer>, count: Int): Array<Answer> {
        init(answer.map { it.text }.toTypedArray())
        return super.filterAnswersLogic(answer, count)
    }

    private fun init(texts: Array<String>){
        cntOfGroup.clear()
        randNumOfGroup.clear()
        texts.forEach {
            val randLabel = findRandLabel(it) ?: return@forEach
            incrementRandCnt(randLabel);
        }

        randNumOfGroup.forEach{
            randNumOfGroup[it.key] = Random().nextInt(it.value)+1
            cntOfGroup[it.key] = 0;
        }
    }

    private fun findRandLabel(text: String): String? {
        if(!FiltersUtils.isContainLabel(text, FilterLabel.RAND)) return null;
        val labels = FiltersUtils.getParametricLabels(text, FilterLabel.RAND) ?: return null;
        if(labels.isNotEmpty()){
            if(labels.size > 1) logger.warn("Find more [RAND] in $text")
            return labels[0]; // label is parametric [RAND=groupName]
        }
        return FilterLabel.RAND.label; // label is not parametric [RAND]
    }

    private fun incrementRandCnt(label: String){
        val key = FiltersUtils.getParameterValue(label) ?: DEF_GROUP_KEY;
        if (!randNumOfGroup.containsKey(key)) randNumOfGroup [key] = 0;
        randNumOfGroup[key] = randNumOfGroup [key]!! + 1;
    }
}