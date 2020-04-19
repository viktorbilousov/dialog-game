package debug.items.runner

import models.Answer
import models.items.ADialogItem
import models.items.runner.DialogItemRunner

class DialogItemAutoRunner(private val nextAnswerId: String) : DialogItemRunner{
    override fun runItem(item: ADialogItem): Answer {
        item.answers.forEach { if(it.id == nextAnswerId ) return it }
        throw IllegalArgumentException("answer id: $nextAnswerId not found")
    }
}