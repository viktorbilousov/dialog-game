package debug.record.service

import debug.record.models.Record
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.*


class RecordFileIO {
    companion object {

        private val logger = LoggerFactory.getLogger(RecordFileIO::class.java) as Logger

        private const val FILE_NAME= "game_records"
        private const val FILE_PATH= "src/main/resources"
        private val FILE = File("$FILE_PATH/$FILE_NAME")

        fun read(inputStream: BufferedInputStream): Array<Record>? {
            logger.info(">> read")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val res = ArrayList<Record>()
            while (reader.ready()) {
                val line = reader.readLine();
                if(line == null){
                    logger.warn("read empty line!")
                    continue;
                }
                val rec = RecordParser.fromString(line.trim())
                logger.info("read record: $line")
                if(rec == null){
                    logger.error("Error by Parsing line: '$line'")
                    continue;
                }
                logger.info("parsed record: $rec")
                res.add(rec)
            }
            logger.info("<< read")
            if (res.size == 0) return null
            return res.toTypedArray();
        }

        fun read(file: File): Array<Record>? {
            if(!file.exists()) file.createNewFile();
            return read(file.inputStream().buffered())
        }

        fun write(outputStream: BufferedOutputStream, records: Array<Record>) {
            logger.info(">> write ${outputStream.toString()}")
            val writer = BufferedWriter(OutputStreamWriter(outputStream))
            writer.use {
                records.forEach { r ->
                    val line = RecordParser.toString(r)
                    logger.info("write: $line")
                    it.write(line + "\n")
                }
            }
            logger.info("<< write")
        }

        fun write(file: File, records: Array<Record>) {
            write(file.outputStream().buffered(), records);
        }

        fun updateFile(file: File, records: Array<Record>){
            logger.info(">> update file :$file, records: ${records.contentToString()}:")
            val list = records.toMutableSet()
            if(file.exists()){
                read(file)?.forEach { list.add(it) }
            }
            write(file, list.toTypedArray())
            logger.info("<< update")
        }

        fun load(): Array<Record>?{
            logger.info(">> load")
            if(!FILE.exists()) {
                logger.warn("cannot load: file not exist")
                return null
            };
            val res =  read(FILE);
            logger.info("<< load")
            return res;
        }

        fun safe(records: Array<Record>){
            updateFile(FILE, records);
        }

        fun safe(record : Record){
            safe(arrayOf(record))
        }

    }
}