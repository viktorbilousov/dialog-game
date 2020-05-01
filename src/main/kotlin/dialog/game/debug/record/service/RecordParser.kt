package dialog.game.debug.record.service

import dialog.game.debug.record.models.Record
import java.lang.StringBuilder

class RecordParser {
    companion object {
        private const val ARRAY_SEPARATOR = "->"
        private const val PARAMETER_SEPARATOR = ":"
        fun toString(record: Record): String {
            return StringBuilder()
                .append(record.id)
                .append(PARAMETER_SEPARATOR)
                .append(record.description)
                .append(PARAMETER_SEPARATOR)
                .append(record.toArray().joinToString(ARRAY_SEPARATOR))
                .toString()
        }
        fun fromString(string: String): Record?{
            val inputArray = string.split(PARAMETER_SEPARATOR);
            if(inputArray.size != 3){
                return null;
            }
            val record = Record(inputArray[0], inputArray[1]);
            inputArray[2].split(ARRAY_SEPARATOR).forEach{
                record.addNextStep(it)
            }
            return record;
        }
    }
}