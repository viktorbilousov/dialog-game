package game

import models.Answer
import models.AnswerType
import models.World
import models.items.DialogItem
import java.lang.IllegalArgumentException


class GameRunner(game: Game, world: World) {

    private val game: Game = game;
    private val world : World = world;

    private fun run(){
        run(world.start())
    }

    private fun run(startPoint : DialogItem){
        var currentPoint =  startPoint
        var answer = Answer.enter()
        loop@ while(true){
            answer = currentPoint.run(answer)

            currentPoint =  when(answer.type){
                AnswerType.ENTER -> world.enterTo(answer)
                AnswerType.EXIT -> {

                    if(world.isRoot()) {
                        break@loop;
                    };
                    world.exit()
                };
                else -> {
                    throw IllegalArgumentException("incorrect answer type")
                }
            }
        }
    }


    fun teleport(id: String) {
        val res = world.worldRouter.goTo(id);
        if(res == null) throw IllegalArgumentException("dialog(room) $id not found")
        run(res)
    }

    fun goToPhrase(id: String){
        for (it in game.dialogs.values) {
            if(it.containsItem(id)) {
                val answ = it.startFrom(id, Answer.enter()) as Answer
                teleport(answ.id);
                return;
            };
        }
        throw IllegalArgumentException("phrase $id not found")
    }

}
