package dialog.game.minigames.tea

import dialog.game.game.GameData
import dialog.game.minigames.tea.models.Collection
import dialog.game.minigames.tea.models.MixedTea
import dialog.game.minigames.tea.models.Tea
import dialog.game.minigames.tea.service.TeaQuality
import dialog.game.minigames.tea.tables.TeaTable
import dialog.game.minigames.tea.tools.TeaGameUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import tools.table.TablePrinter
import kotlin.random.Random

class TeaGame {
    companion object {
        private val logger = LoggerFactory.getLogger(TeaGame::class.java) as Logger

        var currentTea = MixedTea();
        var goalTea: Tea? = null
        var quality: TeaQuality.Quality?  = null
        val guessTea: Tea

        init {
            val menuLabel = "TEA MENU"
            val legendLabel = "LEGEND"
            val teaAnswersLabel = "TEA ANSWERS"
            val flowerFullAnswersLabel = "FLOWER ANSWERS TABLE"
            val flowerAnswersLabel = "FLOWER ANSWERS"
            val gameTableLabel = "GAME TABLE"
            val qualityLabel = "QUALITY"
            val qualityAnswer = "ANSWER"
            val nearestTea = "NEAREST_TEA"


            GameData.addTextToReplace(qualityLabel){ quality() }
            GameData.addTextToReplace(qualityAnswer) { answer() }
            GameData.addTextToReplace(nearestTea) { TeaQuality.nearestToCollection(currentTea).name }
            GameData.addTextToReplace(legendLabel, TeaGameUtils.getLegend())
            GameData.addTextToReplace(menuLabel, TablePrinter().table(TeaTable().addAll(Collection.getTeas())).toPrettyString())
            GameData.addTextToReplace(gameTableLabel){
                TeaGameUtils.getGameTable(
                    currentTea,
                    goalTea
                )
            }

            GameData.addAnswersToInsert(teaAnswersLabel, *TeaGameUtils.getTeasAsAnswers(true))
            GameData.addAnswersToInsert(flowerAnswersLabel, *TeaGameUtils.getFlowersAsAnswers(true))
            GameData.addAnswersToInsert(flowerFullAnswersLabel, *TeaGameUtils.getFlowersAsAnswers(false))
            //ask.tea
            val index = Random.nextInt(Collection.getTeas().size)
            guessTea = Collection.getTeas()[index]
            GameData.gameVariables["target_tea_${guessTea.name}"] = true

        }


        public fun reset() {
            logger.info("GAME RESET")
            currentTea = MixedTea()
        }
        public fun quality(): String {
            val comparedTea = goalTea ?: TeaQuality.nearestToCollection(currentTea)
            quality = TeaQuality.calcQuality(currentTea, comparedTea)
            logger.info("GAME QUALITY: $quality")
            return when(quality){
                TeaQuality.Quality.GOOD -> {"Хороший"}
                TeaQuality.Quality.MIDDLE -> {"Нормальный"}
                TeaQuality.Quality.BAD -> {"Неудачный"}
                else -> "NAN"
            }
        }
        public fun answer() : String{
           var res = ""
            if(goalTea == null){
                val nearestTea = TeaQuality.nearestToCollection(currentTea);
                val isEquals = nearestTea.name == guessTea.name
                res =  if(isEquals) "Ого, это именно то, что я хотел! "
                        else "Хм, ну это не совсем то, что я хотел, "
                 when(quality){
                    TeaQuality.Quality.GOOD -> {
                        res +=   if(isEquals) "И вкусно как получилось)"
                                else "но получилось очень вкусно!"
                    }
                    TeaQuality.Quality.MIDDLE -> {
                        res +=   if(isEquals) "И на вкус вроде бы нормально должно выйти"
                        else "но на вкус вроде ок!"
                    }
                    TeaQuality.Quality.BAD -> {
                        res +=   if(isEquals) "Но вкус у него какой-то не такой что ли..."
                                    else "но вкус у него какой-то не такой что ли..."
                    }
                }
            }else{
                when(quality){
                    TeaQuality.Quality.GOOD -> {
                        res = "Ммммм, это должен быть очень вкусный чай"
                    }
                    TeaQuality.Quality.MIDDLE -> {
                        res =  "О, вроде как нормально получилось"
                    }
                    TeaQuality.Quality.BAD -> {
                        res =   "Что то он какой то не такой что ли... В прошлый раз мы с тобой вроде другой пили"
                    }
                }
            }
            return res
        }


    }
}