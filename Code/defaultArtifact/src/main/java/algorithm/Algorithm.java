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
    protected int cycleChangeCounter;
    protected ArrayList<Edge> allEdge;
    public boolean isFinish;

    public Algorithm(){
        graph = null;
        indexOuterLoop = 0;
        indexInnerLoop = 0;
        cycleChangeCounter = 0;
        allEdge = new ArrayList<>();
        isFinish = false;
    }

    @Override
    public AlgorithmMessage stepForward() {
        String mes = "";
        if(indexInnerLoop == 0){
            mes = mes + "The beginning of a new cycle №: " + (indexOuterLoop+1)+"\n";
        }
        if(isFinish){
            return new AlgorithmMessage("The algorithm has already completed work");// already done work
        }
        if(indexOuterLoop >= (graph.countOfVertex - 1)){
            isFinish = true;
            mes += "The cycle has been passed Vertex - 1 times. Checking for negative weight cycle in graph... \n";
            String forNegC ="";
            boolean check = false;
            for(Edge tmp: allEdge){
                if (tmp.end.distance > (tmp.start.distance + tmp.weight)){
                    check = true;
                    forNegC = findNegativeCycle();
                    break;
                }
            }
            if(check){
                mes+="There is a negative weight cycle \n"+forNegC;
            }else{
                mes+="There isn't a negative weight cycle \n";
            }
            mes = mes+ "The algorithm has completed work."+answer();

            return new AlgorithmMessage(mes, null, true, true); // DONE
        }
        if(indexInnerLoop<allEdge.size()){
            Edge tmp = allEdge.get(indexInnerLoop);
            indexInnerLoop++;
            boolean isEndOfCycle = (indexInnerLoop == allEdge.size())?true:false;
            if(tmp.start.isCheck == true) {
                if(tmp.end.isCheck == false){
                    tmp.end.isCheck = true;
                    tmp.end.distance = tmp.start.distance + tmp.weight;
                    cycleChangeCounter++;
                    mes = mes + "Considered edge: "+tmp.start.name+"-"+tmp.end.name+". Changed distance for vertex "+ tmp.end.name+" from infinity: "+ tmp.end.distance;
                    return new AlgorithmMessage(mes, tmp, false,isEndOfCycle );//changed distance from infinity
                }
                if ((tmp.end.distance > (tmp.start.distance + tmp.weight))) {
                    tmp.end.distance = tmp.start.distance + tmp.weight;
                    cycleChangeCounter++;
                    mes = mes + "Considered edge: "+tmp.start.name+"-"+tmp.end.name+". Changed distance for vertex "+ tmp.end.name+" because found distance are less "+ tmp.end.distance;
                    return new AlgorithmMessage(mes, tmp, false, isEndOfCycle);//end v distance changed because found distance are less
                }else{
                    mes = mes +  "Considered edge: "+tmp.start.name+"-"+tmp.end.name+". Nothing changed because found distance larger";
                    return new AlgorithmMessage(mes, tmp, false, isEndOfCycle);// nothing changed because found distance larger
                }
            }else{
                mes = mes +  "Considered edge: "+tmp.start.name+"-"+tmp.end.name+"Nothing changed because start vertex has mark \"infinity\"";
                return new AlgorithmMessage(mes, tmp, false, isEndOfCycle); // nothing changed because startV "infinity"
            }
        }else{
            indexOuterLoop++;
            indexInnerLoop = 0;
            if(cycleChangeCounter == 0){
                isFinish = true;
                mes += "The algorithm has completed work because nothing has changed on the current cycle, also it's mean that there isn't a negative weight cycle. \n"+answer();
                return new AlgorithmMessage(mes, null, true, true);
            }
            cycleChangeCounter = 0;
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
        if(graph.startVertex != null) {
            graph.startVertex.isStart = true;
            graph.startVertex.isCheck = true;
        }
    }

    public String answer(){
        Vertex[] allVertexNew = graph.graph.keySet().toArray(new Vertex[0]);
        Arrays.sort(allVertexNew);
        String ans = "\nResult: \n Start vertex: " + graph.startVertex.name+"\n";
        for(Vertex v: allVertexNew){
            boolean isInf = v.isCheck;
            ans= ans + "Vertex: " + v.name + " distance " +(isInf?v.distance:"inf") + "\n";

        }
        return ans;
    }

    public String findNegativeCycle(){
        ArrayList<Edge> ed = new ArrayList<>();
        for(int i = 0; i < graph.countOfVertex - 1; i++){
            for(Edge tmp: allEdge){
                if(tmp.start.isCheck) {
                    if (tmp.end.distance > (tmp.start.distance + tmp.weight)) {
                        tmp.end.distance = tmp.start.distance + tmp.weight;
                        if (!ed.contains(tmp)) {
                            ed.add(tmp);
                        }
                        break;
                    }
                }
            }
        }
        String res= "";
        if(ed.size() != 0){
            res+="Edge in cycle:\n";
            Edge Start = ed.get(0);
            Edge tmp = Start;
            Edge tmp2 = Start;
            res+= tmp.start.name+"-"+tmp.end.name +"\n";
            int count = 1;
            while (count != ed.size()) {
                for (int i = 0; i < ed.size(); i++) {
                    tmp2 = ed.get(i);
                    if (tmp.end.name == tmp2.start.name) {
                        tmp = tmp2;
                        res += tmp.start.name + "-" + tmp.end.name + "\n";
                        count++;
                        break;
                    }
                }
            }
        }
        return res ;
    }
}