package algorithm;

import logger.*;
import graph.*;

import java.util.ArrayList;
import java.util.Arrays;

/**Class for Ford-Bellman algorithm. Stores his own version of graph and work with it.
 * <p>Executes algorithm step by step with function {@link#stepForward}</p>
 */
public class Algorithm implements IAlgorithm {
    protected Graph graph;
    protected int indexOuterLoop;
    protected int indexInnerLoop;
    protected ArrayList<Edge> allEdge;
    public boolean isFinish;

    public Algorithm(){
        graph = null;
        indexOuterLoop = 0;
        indexInnerLoop = 0;
        allEdge = new ArrayList<>();
        isFinish = false;
    }

    @Override
    public AlgorithmMessage stepForward() {
        if(isFinish){
            return new AlgorithmMessage("The algorithm has already completed work");// already done work
        }
        if(indexOuterLoop >= (graph.countOfVertex - 1)){
            isFinish = true;
            //На этом шаге сообщается, присутствует ли в графе цикл отрицательного веса (сделать одним циклом или по шагам?)
            String mes = "The algorithm has completed work"+answer();
            return new AlgorithmMessage(mes, null, true); // DONE
        }
        if(indexInnerLoop<allEdge.size()){
            Edge tmp = allEdge.get(indexInnerLoop);
            indexInnerLoop++;
            if(tmp.start.isCheck == true) {
                if(tmp.end.isCheck == false){
                    tmp.end.isCheck = true;
                    tmp.end.distance = tmp.start.distance + tmp.weight;
                    String mes = "Considered edge: "+tmp.start.name+"-"+tmp.end.name+". Changed distance for vertex "+ tmp.end.name+" from infinity: "+ tmp.end.distance;
                    return new AlgorithmMessage(mes, tmp, false);//changed distance from infinity
                }
                if ((tmp.end.distance > (tmp.start.distance + tmp.weight))) {
                    tmp.end.distance = tmp.start.distance + tmp.weight;
                    String mes = "Considered edge: "+tmp.start.name+"-"+tmp.end.name+". Changed distance for vertex "+ tmp.end.name+" because found distance are less "+ tmp.end.distance;
                    return new AlgorithmMessage(mes, tmp, false );//end v distance changed because found distance are less
                }else{
                    String mes = "Considered edge: "+tmp.start.name+"-"+tmp.end.name+". Nothing changed because found distance larger";
                    return new AlgorithmMessage(mes, tmp, false);// nothing changed because found distance larger
                }
            }else{
                String mes = "Considered edge: "+tmp.start.name+"-"+tmp.end.name+"Nothing changed because start vertex has mark \"infinity\"";
                return new AlgorithmMessage(mes, tmp, false); // nothing changed because startV "infinity"
            }
        }else{
            indexOuterLoop++;
            indexInnerLoop = 0;
            return stepForward();
        }
    }


    @Override
    public void initAlgorithm(Graph g) {
        indexInnerLoop = 0;
        indexOuterLoop = 0;
        isFinish = false;
        allEdge.clear();
        try {
            this.graph = g.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        Vertex[] allVertex = graph.graph.keySet().toArray(new Vertex[0]);
        Arrays.sort(allVertex);
        for(Vertex v: allVertex){
            allEdge.addAll(graph.graph.get(v));
        }
        if(graph.startVertex == null){
            if(!graph.graph.isEmpty()){
                Vertex[] allVertexNew = graph.graph.keySet().toArray(new Vertex[0]);
                for(Vertex v: allVertexNew){
                    if(v.number == 0){
                        graph.startVertex = v;
                        break;
                    }
                }
            }
        }
        graph.startVertex.isStart = true;
        graph.startVertex.isCheck = true;
    }

    public String answer(){
        Vertex[] allVertexNew = graph.graph.keySet().toArray(new Vertex[0]);
        Arrays.sort(allVertexNew);
        String ans = "\nResult: \n Start vertex: " + graph.startVertex.name+"\n";
        for(Vertex v: allVertexNew){
            ans= ans+"Vertex: "+v.number+" distance "+v.distance+"\n";

        }
        return ans;
    }
}