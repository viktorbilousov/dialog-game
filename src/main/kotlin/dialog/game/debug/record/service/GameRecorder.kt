package dialog.game.debug.record.service

import dialog.game.debug.record.models.Record
import dialog.game.debug.record.tools.RecordIdGen
import dialog.system.models.Answer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.IllegalStateException
import java.util.*

class GameRecorder {
    companion object{

        private val logger = LoggerFactory.getLogger(GameRecorder::class.java) as Logger


        var isRecorded = false
        private set(value) {
            logger.info("Is Recorded = $value")
            field = value
        };

        private var currentRecord : Record? = null;

        public fun startRecord(){
            logger.info(">>start Record")
            if(isRecorded) {
                logger.info("<< start Record")
                return
            };
            isRecorded = true;
            currentRecord = Record(RecordIdGen.getNextId());
            logger.info("current record: ${currentRecord!!.id}")
            logger.info("<< start Record")
        }
        public fun stopRecord() : Record?{
            logger.info(">> stop Record")
            if(!isRecorded) return null;
            isRecorded = false;
            logger.info("<< stop Record")
            currentRecord?.let { it.description = readDescription() ?: it.id}
            return currentRecord;
        }

        public fun breakRecord(){
            logger.info(">> break Record")
            isRecorded = false;
            currentRecord = null;
            logger.info("<< break Record")
        }

        public fun recordStep(choice : Answer){
            if(!isRecorded) throw IllegalStateException("Record is stopped")
            currentRecord!!.addNextStep(choice.id)
            logger.info("Step ${choice.id} recorded!")
        }

        private fun readDescription(): String?{
            println("Enter the description:\n>")
            val input = Scanner(System.`in`)
            val stringInput = input.nextLine()
            if (stringInput == null || stringInput == "") {
                return null
            }
            return stringInput;
        }


    }
}