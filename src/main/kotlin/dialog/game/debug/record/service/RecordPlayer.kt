package dialog.game.debug.record.service

import dialog.game.debug.record.models.Record

class RecordPlayer(record: Record) {
    private var currentIndex = 0;
    private val array = record.toArray();
    fun getNextStep(): String {
        return array[currentIndex++];
    }

    fun haveNext(): Boolean {
        return currentIndex < array.size
    }

    fun restart() {
        currentIndex = 0;
    }
}