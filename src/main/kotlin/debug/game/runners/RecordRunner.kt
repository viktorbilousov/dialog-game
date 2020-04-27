package debug.game.runners

import debug.record.models.Record
import debug.record.phrase.GameRecordPhrase
import debug.record.service.RecordFileIO
import game.Game
import game.GameData
import game.Runner
import models.World
import models.items.dialog.ADialog
import models.items.dialog.DebugDialog
import models.items.dialog.Dialog
import models.items.phrase.DebugFilteredPhrase
import java.util.HashSet

class RecordRunner( game: Game, world: World) : Runner(game, world){
    init {
       val map = game.dialogs.values
           .map { ADialog.convertTo<DebugDialog>(it) }.associateBy { it.id }
        map.values.forEach{ it.transformIfCurrentItemIsPhrase<GameRecordPhrase>() }

        game.dialogs.clear();
        game.dialogs.putAll(map)

        val loaded = RecordFileIO.load();
        val set = HashSet<Record>();
        if(loaded != null) set.addAll(loaded);
        GameData.gameVariables["debug.records"] = set;
    }

}