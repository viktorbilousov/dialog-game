import com.tinkerpop.blueprints.Vertex
import com.tinkerpop.blueprints.impls.tg.TinkerGraph
import dialog.system.models.Answer
import dialog.system.models.AnswerType
import dialog.system.models.Indexable
import dialog.system.models.items.text.PhraseText
import dialog.system.models.items.text.PhraseTextStream
import dialog.system.models.router.Router
import dialog.system.models.router.RouterStream
import game.Game
import dialog.game.game.Loader
import dialog.game.game.Tester

import org.junit.jupiter.api.Test
import java.io.File

class GameLoaderTest {


    private val worldRouterId = Game.settings["world-router-id"]

   @Test
    public fun load(){

        val pathToGraphs  = File("src/test/resources/graphs").absolutePath
        val pathToRouter  = File("src/test/resources/routers").absolutePath
        val pathToPhrases = File("src/test/resources/phrases").absolutePath
        println(pathToGraphs)
        writeTestGame(pathToPhrases, pathToRouter, pathToGraphs)
        val game = Game();
        Loader(game).load(pathToPhrases, pathToRouter, pathToGraphs)
        Tester.testGame(game);
        println(game)
    }

   private fun writeTestGame(pathToPhrases : String, pathToRouters : String, pathToGraphs: String) {

       val phraseTexts = generatePhraseText(1, 1000);

       for(i in phraseTexts.indices){
           val id = phraseTexts[i].id.toInt();
           if( (id - 1 ) % 10 == 0 && id  > 10) {
               phraseTexts[i-1].answers[0].type = AnswerType.EXIT
           }
       }

       val main = generateRouter("main", 1, 10);
       main.isResetToStart = false;
       val rooms = arrayListOf<Router>();
       for (i in 1..10) {
           rooms.add(generateRouter("room.$i", 10 * i + 1, 10 * (i + 1)))
       }

       val graph = TinkerGraph();
       val v = hashMapOf<Int, Vertex>()
       v[0] = graph.addVertex("main");
       v[0]!!.setProperty(Indexable.ID_NAME,"main");
       for (i in 1..10) {
           v[i] = graph.addVertex("room.$i");
           v[i]!!.setProperty(Indexable.ID_NAME,"room.$i");
           graph.addEdge(null, v[0], v[i], "${v[0]?.id}->${v[i]?.id}")
           graph.addEdge(null, v[i], v[0], "${v[i]?.id}->${v[0]?.id}")
       }

       val world = Router(worldRouterId as String, graph);
       world.startPointId = "main";

       PhraseTextStream.write(phraseTexts, "$pathToPhrases//phrases_test.json")
       RouterStream.write(main, "$pathToRouters//main.json", pathToGraphs);
       RouterStream.write(world, "$pathToRouters//world.json", pathToGraphs);
       RouterStream.write(rooms.toTypedArray(), "$pathToRouters//rooms.json", pathToGraphs);
         assert(true)
   }


    private fun generatePhraseText(minIndex: Int, maxIndex: Int) : Array<PhraseText> {
        val phrases = arrayListOf<PhraseText>()
        for(i in minIndex..maxIndex){
            var answer =  Answer("${i+1}","go to ${i+1}");
            if(i == maxIndex) {
                answer.type = AnswerType.EXIT
            }
            phrases.add(PhraseText("$i", "text od phrase $i", arrayOf(answer)))
        }
        return phrases.toTypedArray()
    }
    private fun generateRouter(id: String, minIndex: Int, maxIndex: Int) : Router{
        val graph = TinkerGraph();
        val v = hashMapOf<Int, Vertex>()
        for(i in minIndex .. maxIndex){
            v[i] = graph.addVertex("$i");
            v[i]!!.setProperty(Indexable.ID_NAME, "$i")
            if(i > minIndex ){
                graph.addEdge(null, v[i-1], v[i], "${i-1}->${i}")
            }
        }
        val r =  Router(id, graph)
        r.startPointId= minIndex.toString()
       return r
    }




}