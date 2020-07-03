package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**Class for storing information about graph and working with it */
public class Graph implements IGraph {

    protected Map<Vertex, ArrayList<Edge> > graph;
    protected Vertex startVertex;
    protected int countOfVertex;
    protected int countOfEdge;

    public Graph(){
        graph = new HashMap<>();
        startVertex = null;
        countOfEdge = 0;
        countOfVertex = 0;
    }

    @Override
    public Graph clone() throws CloneNotSupportedException  {
        Graph newG = new Graph();
        Vertex[] allVertexOld = graph.keySet().toArray(new Vertex[0]);
        for(Vertex v: allVertexOld){
            newG.addVertex(v.clone());
        }

        Vertex[] allVertexNew = newG.graph.keySet().toArray(new Vertex[0]);
        Vertex start = null;
        Vertex end = null;
        for(Vertex v: allVertexOld){
            ArrayList<Edge> edgesOld = graph.get(v);
            for(Edge e: edgesOld){
                for(Vertex newVertx: allVertexNew){
                    newVertx = (newVertx.number == startVertex.number)?(newG.startVertex=newVertx):newVertx;//так ли
                    if(newVertx.number == e.start.number){
                        start = newVertx;
                    }
                    if(newVertx.number == e.end.number){
                        end = newVertx;
                    }
                    if(start != null && end != null){
                        newG.addEdge(new Edge(e.weight, start, end));
                        start = end = null;
                        break;
                    }
                }
            }

        }

        return newG;
    }

    @Override
    public void addVertex(Vertex v) {
        countOfVertex++;
        graph.put(v, new ArrayList<>());
    }

    @Override
    public void addEdge(Edge e) {
        boolean isAlreadyExists = false;
        if(graph.containsKey(e.start)&&graph.containsKey(e.end)){
            ArrayList<Edge> forCheck = graph.get(e.start);
            for(Edge tmp: forCheck){
                if(tmp.equals(e)){
                    isAlreadyExists = true;
                    break;
                }
            }
            if(!isAlreadyExists){
                forCheck.add(e);
                countOfEdge++;
            }
        }
    }

    @Override
    public void deleteVertex(Vertex v) {

    }

    @Override
    public void deleteEdge(Edge e) {

    }
}