package dialog.game.debug.record.answer

import dialog.system.models.answer.Answer

class RecordAnswers {
    companion object{
        public val RECORD_START
            get() = Answer("dialog.game.debug.record_start", "start record")

        public val RECORD_STOP
            get() = Answer("dialog.game.debug.record_stop", "stop record")

        public val RECORD_BREAK
            get() = Answer("dialog.game.debug.record_break", "break record")

        public fun array() : Array<Answer>{
            return arrayOf(
                RECORD_START,
                RECORD_STOP,
                RECORD_BREAK
            )
        }
    }
}