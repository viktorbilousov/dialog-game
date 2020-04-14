package game.debug.autorunner

import game.Game
import game.Runner
import game.debug.record.models.Record
import game.debug.record.models.RecordPlayer
import models.World
import models.items.dialog.ADialog
import models.items.dialog.DebugDialog
import models.items.runner.RunnerConfigurator

class AutoRunnerNew(game: Game, world: World, record: Record) : Runner(game, world){

    private val recordPlayer = RecordPlayer(record);

    init {
        game.dialogs.values
            .map { ADialog.createFrom<DebugDialog>(it) }
            .forEach{
                it.transformIfCurrentItemIsPhrase = phrase@ { ph ->
                    if(recordPlayer.haveNext()) RunnerConfigurator.setRunner(ph, DialogItemAutoRunner(recordPlayer.getNextStep()))
                    else RunnerConfigurator.setDefaultRunner(ph)
                    return@phrase ph;
                }
            }
    }

}