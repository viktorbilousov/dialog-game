package dialog.game.maingame

import dialog.system.models.router.Router
import dialog.system.tools.RouterTester
import game.Game
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class Tester {

companion object {

    private val logger = LoggerFactory.getLogger(Tester::class.java) as Logger

    public fun testRouter(router: Router): Boolean{
        logger.debug(">> Test router: ${router.id}")
        try {
            RouterTester.test(router)
                .isGraphRelated()
                .isAllVertexHasItems()
                .checkTypesOfPhases()
                .isItemsLinkedCorrectly();
        } catch (e: Exception) {
            println(e.printStackTrace())
            logger.error("router $router did not passed all test: $e")
            logger.debug("<< Test router: false")
            return false;
        }
        logger.info(" router ${router.id} passed all test")
        logger.debug("<< Test router: true")

        return true;
    }

    public fun testGame(game: Game){
        val dialogs = game.dialogs;
        val globalRouter = game.world?.worldRouter;
        var errorMessage = ""

        val errorList = arrayListOf<String>()
        for (dialog in dialogs.values) {
            if(!testRouter(dialog.router) && dialog.id != Game.settings["world-router-id"]) {
                errorList.add(dialog.id)
            }
        }

        if(game.world == null){
            logger.error("global router is null")
            errorMessage = "Global router is null,"
        }
        else if(!testGlobalRouter(globalRouter!!) && game.world != null){
            errorList.add(globalRouter!!.id)
        }


        if (errorList.isNotEmpty()) {
            throw IllegalArgumentException("$errorMessage Some routers did not pass test : ${errorList.toTypedArray().contentToString()} ")
        }
        logger.info("<< testRouters")

    }

    public fun testRouters(routers: List<Router>) {
       logger.info(">> testRouters")
        val errorList = arrayListOf<String>()
        for (router in routers) {
            if (router.id == Game.settings["world-router-id"]) {
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

    public fun testGlobalRouter(router: Router) : Boolean {
        logger.info(">> testGlobalRouter")
        logger.info("Test world router: ${router.id}")
        if (router.id != Game.settings["world-router-id"]) {
            logger.error("this router in not global router")
            logger.info("<< testGlobalRouter false")
            return false
        }
        try {
            RouterTester.test(router)
                .isAllVertexHasItems()
                .isItemsLinkedCorrectly();
        } catch (e: java.lang.Exception) {
            logger.error("world router $router not passed all test: ${e.message}r")
            logger.info("<< testGlobalRouter false")
            return false;
        }
        logger.info("world router $router passed all test")
        logger.info("<< testGlobalRouter true")
        return true;

    }
}

}