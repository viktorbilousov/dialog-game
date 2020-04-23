package debug.game.runners

import debug.items.runner.DialogItemAutoRunner
import game.Game
import game.Runner
import debug.record.models.Record
import debug.record.phrase.GameRecordPhrase
import debug.record.service.RecordFileIO
import debug.record.service.RecordPlayer
import models.World
import models.items.dialog.ADialog
import models.items.dialog.DebugDialog
import models.items.runner.RunnerConfigurator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class AutoRunner(game: Game, world: World) : Runner(game, world){

    private var recordPlayer: RecordPlayer = RecordPlayer(Record("empty"));

    companion object {
        private val logger = LoggerFactory.getLogger(AutoRunner::class.java) as Logger
    }

    init {

        val map = game.dialogs.values
            .map { ADialog.createFrom<DebugDialog>(it) }.associateBy { it.id }
        map.values.forEach{ it.transformIfCurrentItemIsPhrase = phrase@ { ph ->
            if(recordPlayer.haveNext()) RunnerConfigurator.setRunner(ph,
                DialogItemAutoRunner(recordPlayer.getNextStep())
            )
            else RunnerConfigurator.setDefaultRunner(ph)
            return@phrase ph;
        } }

        game.dialogs.clear();
        game.dialogs.putAll(map)
    }

    override fun run() {
        loadAndSelectRecords()
        super.run()
    }

    private fun loadAndSelectRecords(){
        val records = RecordFileIO.load();
        if(records == null){
            logger.warn("File is Empty!")
            return;
        }
        records.forEachIndexed(){index, record -> println("[${index+1}] ${record.description}") }
        var num : Int? = 0;
        while (num == 0) {
            println("Enter the number:\n>")
            val input = Scanner(System.`in`)
            val stringInput = input.nextLine()
            num = stringInput.toIntOrNull();
            if (num == null || num > records.size || num < 1) {
                println("InputError: please enter number")
                num = 0;
            }
        }
        this.recordPlayer = RecordPlayer(records[num!!-1])
        logger.info("selected record : ${records[num -1]}")
    }
}