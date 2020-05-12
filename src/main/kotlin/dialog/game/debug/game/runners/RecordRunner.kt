package dialog.game.debug.game.runners

import dialog.game.debug.record.models.Record
import dialog.game.debug.record.phrase.GameRecordPhrase
import dialog.game.debug.record.service.GameRecorder
import dialog.game.debug.record.service.RecordFileIO
import game.Game
import dialog.game.game.GameData
import dialog.game.game.Runner
import dialog.game.models.World
import dialog.system.models.items.dialog.ADialog
import dialog.system.models.items.dialog.DebugDialog

import java.lang.Exception
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
        GameData.gameVariables["dialog.game.debug.records"] = set;
    }

    override fun run() {
        try {
            super.run()
        }catch (e: Exception){
            if(GameRecorder.isRecorded){
                val record = GameRecorder.stopRecord() ?: return
                RecordFileIO.safe(record)
                throw e;
            }
        }
    }
}