package game.debug.record.models

import models.Answer
import java.lang.IllegalStateException

class GameRecorder {
    companion object{

        var isRecorded = false
        private set;

        private val records = arrayListOf<Record>()
        private var currentRecord : Record? = null;

        public fun startRecord(){
            if(isRecorded) return;
            isRecorded = true;
            currentRecord = Record();
        }
        public fun stopRecord(){
            if(!isRecorded) return;
            isRecorded = false;
            records.add(currentRecord!!);
        }

        public fun breakRecord(){
            isRecorded = false;
            currentRecord = null;
        }

        public fun getRecords(): Array<Record>{
          return  records.toTypedArray()
        }

        public fun recordStep(choice : Answer){
            if(!isRecorded) throw IllegalStateException("Record is stopped")
            currentRecord!!.addNextStep(choice.id)
        }
    }
}