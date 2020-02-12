package game

import models.World
import models.items.DialogItem
import models.items.dialog.Dialog
import models.items.text.PhraseText
import models.items.text.PhraseTextFabric
import models.items.text.PhraseTextStream
import models.router.Router
import models.router.RouterStream
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Files.isRegularFile
import java.nio.file.Paths
import kotlin.streams.toList


class Loader(private val game: Game) {
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }

    public fun load(phrasesTextFolder: String, routersFolder: String, graphFolder: String) {
        logger.info(">> load")
        var error = false;
        var routers = emptyList<Router>()

        try {
            loadPhrases(phrasesTextFolder).forEach {
                game.phrases[it.id] = PhraseTextFabric.toPhrase(it);
            }
        } catch (e: Exception) {
            logger.error("Phrases Load error: ${e.message}")
            error = true;
        }

        try {
            routers = loadRouters(routersFolder, graphFolder);
        } catch (e: Exception) {
            logger.error("Router Loading error: ${e.message}")
            error = true;
        }

        routers.forEach {
            it.items = game.phrases;
        }

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
            globalRouter.items = game.dialogs as HashMap<String, DialogItem>
            game.world = World(globalRouter)
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
            .map { "$phrasesTextFolder/${it.fileName}" }
            .toList();

        val sb = StringBuilder();
        filesInPhrasesFolder.forEach {
            sb.append("\n").append(it)
        }
        logger.info("Found files: ${sb}")

        val errorList = arrayListOf<String>()

        for (s in filesInPhrasesFolder) {
            logger.info("read file $s")
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
        logger.info(">> loadDialogsAndWorld routersFolder=$routersFolder, graphFolder=$graphFolder")
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
        logger.info("Found files: $sb")

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
            logger.info("routers readed : ${routers.contentToString()}")
            routersList.addAll(routers);
        }
        if (errorList.isNotEmpty()) {
            throw IllegalArgumentException("Some phrases have not been read : ${errorList.toTypedArray().contentToString()} ")
        }
        logger.info("<< loadDialogsAndWorld")
        return routersList;
    }



}