package game

import models.items.dialog.Dialog
import models.items.phrase.Phrase
import models.router.Router

class Game {
    val settings = HashMap<String, Any?>()
    var phrases = hashMapOf<String, Phrase>()
    var dialogs = hashMapOf<String, Dialog>()
    var routers = hashMapOf<String, Router>()

}