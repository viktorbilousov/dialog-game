package game.debug.record.models

class RecordPlayer(record: Record){
    private var currentIndex = 0;
    private val array = record.toArray();
    public fun getNextStep(): String{
        return array[currentIndex++];
    }
    public fun haveNext() : Boolean{
        return currentIndex < array.size
    }
    public fun restart() {
        currentIndex = 0;
    }
}