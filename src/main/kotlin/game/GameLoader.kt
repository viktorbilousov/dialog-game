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
import tools.RouterTester
import java.nio.file.Files
import java.nio.file.Files.isRegularFile
import java.nio.file.Paths
import kotlin.streams.toList


class GameLoader(private val game: Game) {
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java) as Logger
    }

    public fun load(phrasesTextFolder: String, routersFolder: String, graphFolder: String) {
        logger.info(">> load")
        var error = false;
        var routers = emptyList<Router>()
        var texts = emptyList<PhraseText>()

        try {
            texts = loadPhrases(phrasesTextFolder)
        } catch (e: Exception) {
            logger.error("Phrases Load error: ${e.message}")
            error = true;
        }

        texts.forEach {
            val phrase = PhraseTextFabric.toPhrase(it);
            game.phrases[phrase.id] = phrase;
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

        try {
            testRouters(routers)
        } catch (e: Exception) {
            logger.error("Test not passed: ${e.message}")
            error = true;
        }

        for (router in routers) {
            if (router.id != game.settings["world-router-id"]) {
                game.dialogs[router.id] = Dialog(router.id, router)
            }
        }

        var globalRouter: Router? = null;
        routers.forEach {
            if (it.id == game.settings["world-router-id"]) {
                globalRouter = it;
                return@forEach;
            }
        }
        if (globalRouter == null) {
            logger.error("A World router not found!")
            error = true;
        } else {
            globalRouter!!.items = game.dialogs as HashMap<String, DialogItem>
            try {
                testGlobalRouter(globalRouter!!)
            } catch (e: Exception) {
                logger.error("Test not passed: ${e.message}")
                error = true;
            }

            game.world = World(globalRouter!!)
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

    private fun testRouters(routers: List<Router>) {
        logger.info(">> testRouters")
        val errorList = arrayListOf<String>()
        for (router in routers) {
            if (router.id == game.settings["world-router-id"]) {
                continue;
            } else {
                logger.info("Test router: ${router.id}")
                try {
                    RouterTester.test(router)
                        .isGraphRelated()
                        .isAllVertexHasItems()
                        .checkTypesOfPhases()
                        .isItemsLinkedCorrectly();
                } catch (e: Exception) {
                    println(e.printStackTrace())
                    logger.error("router $router did not passed all test: $e")
                    errorList.add(router.id)
                    continue
                }
                logger.info(" router $router passed all test")
            }
        }
        if (errorList.isNotEmpty()) {
            throw IllegalArgumentException("Some routers did not pass test : ${errorList.toTypedArray().contentToString()} ")
        }
        logger.info("<< testRouters")
    }

    private fun testGlobalRouter(router: Router) {
        logger.info(">> testGlobalRouter")
        logger.info("Test world router: ${router.id}")
        if (router.id != game.settings["world-router-id"]) {
            throw IllegalArgumentException("this router in not global router")
        }
        try {
            RouterTester.test(router)
                .isAllVertexHasItems()
                .isItemsLinkedCorrectly();
        } catch (e: java.lang.Exception) {
            throw IllegalArgumentException("world router $router not passed all test: ${e.message}r")
        }
        logger.info("world router $router passed all test")
        logger.info("<< testGlobalRouter")

    }

}