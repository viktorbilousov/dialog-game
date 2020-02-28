package minigames.tea.phrase

import minigames.tea.TeaGame
import minigames.tea.models.Collection
import minigames.tea.tables.TeaTable
import minigames.tea.tools.TeaGameUtils
import models.Answer
import models.items.phrase.FilteredPhrase
import models.items.phrase.PhrasePrinter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import phrases.fabric.FiltersCollection
import phrases.fabric.PrinterFabric
import tools.table.TablePrinter

class TeaGamePhraseConfigurator(val phrase : FilteredPhrase){

    private  val menuLabel = "TEA MENU"
        private  val legendLabel = "LEGEND"
        private  val teaAnswersLabel = "TEA ANSWERS"
        private  val flowerAnswersLabel = "FLOWER ANSWERS"
        private  val tableLabel = "GAME TABLE"

        public fun flowerAnswer(onlyFlowerName : Boolean): TeaGamePhraseConfigurator {

            phrase.addAnswerFilter("flowerAnswerCreator", FiltersCollection.addAnswersInsteadLabel(
                flowerAnswersLabel,
                TeaGameUtils.getFlowersAsAnswers(
                    onlyFlowerName
                )
            ))

            phrase.phrasePrinter = object : PhrasePrinter {
                override fun printTextDialog(text: String, answer: Array<Answer>) {
                    PrinterFabric.defTextPrinter(text);
                    println(TeaGameUtils.getLegend())
                    PrinterFabric.defAnswersPrinter(answer)
                }
            }

            return this
        }

    public fun teasAnswer(onlyName : Boolean): TeaGamePhraseConfigurator {

        phrase.addAnswerFilter("flowerAnswerCreator", FiltersCollection.addAnswersInsteadLabel(
            teaAnswersLabel,
            TeaGameUtils.getTeasAsAnswers(
                onlyName
            )
        ))
        return this
    }
    public fun legend(): TeaGamePhraseConfigurator{
        phrase.addPhrasesFilter("addLegend", FiltersCollection.replaceLabelToTextPhrase(legendLabel, TeaGameUtils.getLegend()))
        return this;
    }

    public fun teasMenuPhrase() : TeaGamePhraseConfigurator {

        phrase.addPhrasesFilter("addTeasMenu", FiltersCollection.replaceLabelToTextPhrase(
            menuLabel ,
            TablePrinter().table(TeaTable().addAll(Collection.getTeas())).toPrettyString()
        ))

        return this
    }

    public fun gameTablePhrase() : TeaGamePhraseConfigurator {
        phrase.addPhrasesFilter("addGametable", FiltersCollection.replaceLabelToTextPhrase(
            tableLabel ,
            TeaGameUtils.getGameTable(
                TeaGame.currentTea,
                TeaGame.goalTea
            )
        ))
        return this
    }

    public fun applyPhrases() : TeaGamePhraseConfigurator {
        phrase.addPhrasesFilter("applyAll", FilteredPhrase.Order.Last,  FiltersCollection.applyAllPhrasesToOne)
        return this
    }

}