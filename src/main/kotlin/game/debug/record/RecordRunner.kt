package game.debug.record

import game.Game
import game.Runner
import models.World
import models.items.dialog.ADialog
import models.items.dialog.DebugDialog
import models.items.phrase.DebugFilteredPhrase

class RecordRunner( game: Game, world: World) : Runner(game, world){
    init {
        game.dialogs.values
            .map { ADialog.createFrom<DebugDialog>(it) }
            .forEach{it.transformIfCurrentItemIsPhrase<DebugFilteredPhrase>() }
    }

}