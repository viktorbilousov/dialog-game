package game.debug.record.phrase

import game.debug.record.models.GameRecorder
import game.debug.record.models.RecordAnswers
import models.Answer
import models.items.phrase.*
import models.items.runner.RunnerConfigurator


class GameRecordPhrase(id: String, phrases: Array<String>,  answers: Array<Answer>) : DebugFilteredPhrase(id, phrases, answers) {

    init {
        val runner = RunnerConfigurator.setDebugRunner(this);

        afterFilter = { a, p, _ ->
            val tmp = a.toMutableList()
            if (GameRecorder.isRecorded) {
                tmp.add(RecordAnswers.RECORD_STOP)
                tmp.add(RecordAnswers.RECORD_BREAK)
            } else {
                tmp.add(RecordAnswers.RECORD_START)
            }
            FilterResult(
                tmp.toTypedArray(),
                p
            )
        }

        updatePrinter {
            object : PhrasePrinter {
                override fun printTextDialog(text: String, answer: Array<Answer>) {
                    val state = if (GameRecorder.isRecorded) "Rec" else "Stop"
                    println("RECORD STATE: " + GameRecorder.isRecorded)
                    val tmp = answer.toMutableList()
                    tmp.removeAll(RecordAnswers.array())

                    it.printTextDialog(text, tmp.toTypedArray());

                    println("--------------------")

                    for (i in 0 until answer.size) {
                        if (answer[i].id.startsWith("debug.record."))
                            println("[${i + 1}] ${answer[i].text}\n")
                    }

                }
            }
        }

        updateAnswerChooser {
            object : AnswerChooser {
                override fun chooseAnswer(answers: Array<Answer>): Answer {
                    val res = it.chooseAnswer(answers);
                    if (res == RecordAnswers.RECORD_START) {
                        GameRecorder.startRecord()
                        runner.restart()
                    } else if (res == RecordAnswers.RECORD_STOP) {
                        GameRecorder.stopRecord()
                        runner.restart()
                    } else if (res == RecordAnswers.RECORD_BREAK) {
                        GameRecorder.breakRecord()
                        runner.restart()
                    } else {
                        if (GameRecorder.isRecorded) {
                            GameRecorder.recordStep(res)
                        }
                    }
                    return res;
                }
            }
        }

    }
}

