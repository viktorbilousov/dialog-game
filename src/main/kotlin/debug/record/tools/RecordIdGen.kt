package debug.record.tools

import debug.record.models.Record
import game.Game

class RecordIdGen {
    companion object{
        private const val prefix = "debug.record."
        private var lastId = 0;

        public fun getNextId(): String{
            val set = Game.gameVariables["debug.records"] as HashSet<Record>?
            if(set == null) lastId++;
            set!!.forEach{
                val numStr = it.id.substringAfter(prefix).toIntOrNull() ?: return@forEach;
                lastId = maxOf(numStr, lastId)
            }
            return "$prefix${++lastId}"
        }
    }

}