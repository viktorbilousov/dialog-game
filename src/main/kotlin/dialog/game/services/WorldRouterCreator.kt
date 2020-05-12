package dialog.game.services

import com.tinkerpop.blueprints.Graph
import com.tinkerpop.blueprints.Vertex
import com.tinkerpop.blueprints.impls.tg.TinkerGraph
import dialog.game.game.Loader
import dialog.system.models.Indexable
import dialog.system.models.items.ADialogItem
import dialog.system.models.items.dialog.ADialog
import dialog.system.models.router.Router
import game.Game
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.awt.Dialog

// todo: not work
class WorldRouterCreator {
    companion object{

        private val logger = LoggerFactory.getLogger(WorldRouterCreator::class.java) as Logger

        private val defaultStartPoindId = "world"
        private val isResetToStart = false
        private val id = Game.settings["world-router-id"].toString()

        private var cnt = 0;

        fun create(dialogs: HashMap<String, ADialog>): Router {
            return Router(
                 id,
                createGraph(dialogs.values.toTypedArray()),
                dialogs as HashMap<String, ADialogItem>,
                defaultStartPoindId,
                isResetToStart
            )

        }

        private fun createGraph(array: Array<ADialog>): Graph{
            val graph = TinkerGraph()
            var vertices = HashMap<String, Vertex>()
            array.forEach {
                val vertex = graph.addVertex("${it.id}")
                vertex.setProperty(Indexable.ID_NAME, "${it.id}")
                vertices[it.id] = vertex;
            }
            array.forEach {phraseFrom ->
                phraseFrom.answers.forEach { phraseTo ->
                    logger.info("create adge as  ${phraseFrom.id} -> ${phraseTo}")
                    graph.addEdge(
                        cnt++,
                        vertices[phraseFrom.id],
                        vertices[phraseTo.id],
                        "${vertices[phraseFrom.id]!!.getProperty<String>(Indexable.ID_NAME)} -> " +
                                "${vertices[phraseTo.id]!!.getProperty<String>(Indexable.ID_NAME)}"
                    )
                }
            }
            return graph;


        }
    }
}