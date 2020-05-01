package dialog.game.debug.record.tools

import dialog.game.debug.record.models.Record
import dialog.game.maingame.GameData

class RecordIdGen {
    companion object{
        private const val prefix = "dialog.game.debug.record."
        private var lastId = 0;

        public fun getNextId(): String{
            val set = GameData.gameVariables["dialog.game.debug.records"] as HashSet<Record>?
            if(set == null) lastId++;
            set!!.forEach{
                val numStr = it.id.substringAfter(prefix).toIntOrNull() ?: return@forEach;
                lastId = maxOf(numStr, lastId)
            }
            return "$prefix${++lastId}"
        }
    }

}