package dialog.game.game


import dialog.game.models.World
import dialog.system.models.items.ADialogItem
import dialog.system.models.items.dialog.Dialog
import dialog.system.models.items.text.PhraseText
import dialog.system.models.items.text.PhraseTextFabric
import dialog.system.models.items.text.PhraseTextStream
import dialog.system.models.router.Router
import dialog.system.models.router.RouterStream
import game.Game
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Files.isRegularFile
import java.nio.file.Paths
import kotlin.streams.toList
import java.io.File



class Loader(private val game: Game) {
    companion object {
        private val logger = LoggerFactory.getLogger(Loader::class.java) as Logger
    }

    public fun load(phrasesTextFolder: String, routersFolder: String, graphFolder: String) {
        logger.info(">> load phrasesTextFolder=$phrasesTextFolder , routersFolder=$routersFolder , graphFolder=$graphFolder")
        var error = false;
        var routers = emptyList<Router>()
        logger.info("")
        logger.info("----- load phrases ------")
        logger.info("")

            loadPhrases(phrasesTextFolder).forEach {
                //{phrase text: class=dialog.game.phrases.AutoPhrase, id=game.kitchen.tea.p2.enter, phrases=[game.treehause.tea_game. part2], answers=[{id:treehause.game.tea_game.part2, text:'part2', type=ENTER}]}
                if (logger.isDebugEnabled) logger.debug("read Phrase $it")
                else logger.info("read Phrase {phrase text: class=${it.clazz}, id=${it.id}")

                try {
                    game.phrases[it.id] = PhraseTextFabric.toPhrase(it);
                } catch (e: Exception) {
                    logger.error("Phrases Load error: ${e.cause}", e)
                    logger.error(it.toString())
                    error = true;
                }
            }

        logger.info("")
        logger.info("----- load Routers ------")
        logger.info("")

        try {
            routers = loadRouters(routersFolder, graphFolder)
        } catch (e: Exception) {
            logger.error("Router Loading error: ${e.message}", e)
            error = true;
        }

        routers.forEach {
            it.items = game.phrases as HashMap<String, ADialogItem>;
        }


        logger.info("")
        logger.info("----- global router ------")
        logger.info("")


        var globalRouter: Router? = null;
        for (router in routers) {
            if (router.id != Game.settings["world-router-id"]) {
                game.dialogs[router.id] = Dialog(router.id, router)
            }else{
                globalRouter = router
            }
        }

        if (globalRouter == null) {
            logger.error("A World router not found!")
            error = true;
        } else {
            globalRouter.items = game.dialogs as HashMap<String, ADialogItem>
            game.world = World(globalRouter)
            logger.info("global router = ${game.world!!.worldRouter.id}")

        }

        if (error) {
            logger.error("Game Loading completed unsuccessfully")
            throw IllegalArgumentException("Game Loading completed unsuccessfully")
        }
        logger.info("<< load")
    }

    private fun loadPhrases(phrasesTextFolder: String): List<PhraseText> {
        logger.info(">> loadPhrases from $phrasesTextFolder ")

        val phrasesTextList = arrayListOf<PhraseText>()

        val filesInPhrasesFolder = Files
            .walk(Paths.get(phrasesTextFolder))
            .filter { isRegularFile(it) }
            .map {File(phrasesTextFolder, it.fileName.toString()).absolutePath }
            .toList();

        val sb = StringBuilder();
        filesInPhrasesFolder.forEach {
            sb.append("\n").append(it)
        }
        logger.debug("Found files: ${sb}")

        val errorList = arrayListOf<String>()

        for (s in filesInPhrasesFolder) {
            logger.info("read file ${s.substringAfterLast("/")}")
            val texts: Array<PhraseText> = try {
                PhraseTextStream.readMany(s)!!
            } catch (e: IllegalArgumentException) {
                logger.info("error: try to read as single phrase")
                try {
                    arrayOf(PhraseTextStream.readOne(s)!!)
                } catch (e: IllegalArgumentException) {
                    logger.error("error read file $s: skip")
                    errorList.add(s);
                    continue;
                }
            }
            phrasesTextList.addAll(texts);
        }
        if (errorList.isNotEmpty()) {
            logger.error("Some phrases was not read : ${errorList.toTypedArray().contentToString()}")
            throw IllegalArgumentException("Some phrases have not been read : ${errorList.toTypedArray().contentToString()}")
        }
        logger.info("<< loadPhrases")
        return phrasesTextList;

    }

    private fun loadRouters(routersFolder: String, graphFolder: String): ArrayList<Router> {
        logger.info(">> loadRouters routersFolder=$routersFolder, graphFolder=$graphFolder")
        val routersList = arrayListOf<Router>()
        val errorList = arrayListOf<String>()

        val filesInRoutersFolder = Files
            .walk(Paths.get(routersFolder))
            .filter { isRegularFile(it) }
            .map { "$routersFolder/${it.fileName}" }
            .toList();

        val sb = StringBuilder();
        filesInRoutersFolder.forEach {
            sb.append("\n").append(it)
        }
        logger.debug("Found files: $sb")

        for (s in filesInRoutersFolder) {
            val routers = try {
                RouterStream.readMany(s, graphFolder)
            } catch (e: IllegalArgumentException) {
                logger.info("error: try to read as single router")
                try {
                    arrayOf(RouterStream.readOne(s, graphFolder))
                } catch (e: IllegalArgumentException) {
                    logger.error("error read file $s: skip")
                    errorList.add(s);
                    continue;
                }
            }
            if(logger.isDebugEnabled)logger.debug("routers readed : ${routers.contentToString()}")
            else logger.info("routers readed : ${routers.map { it.id }.toTypedArray().joinToString()}" )

            routersList.addAll(routers);
        }
        if (errorList.isNotEmpty()) {
            throw IllegalArgumentException("Some phrases have not been read : ${errorList.toTypedArray().contentToString()} ")
        }
        logger.info("<< loadRouters")
        return routersList;
    }

}