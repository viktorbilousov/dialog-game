package game.debug.record.models

import models.Answer

class RecordAnswers {
    companion object{
        public val RECORD_START
            get() = Answer("debug.record_start", "start record")

        public val RECORD_STOP
            get() = Answer("debug.record_stop", "stop record")

        public val RECORD_BREAK
            get() = Answer("debug.record_break", "break record")

        public fun array() : Array<Answer>{
            return arrayOf(
                RECORD_START,
                RECORD_STOP,
                RECORD_BREAK
            )
        }
    }
}