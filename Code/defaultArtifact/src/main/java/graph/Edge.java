package graph;

import java.util.Objects;

/**Class for storing information about edge of the graph */
public class Edge {
    public Vertex start;
    public Vertex end;
    public int weight;


    public Edge(int weight, Vertex start, Vertex end){
        this.start = start;
        this.end = end;
        this.weight = weight;
    }


    public void changeWeight(int weight){
        this.weight = weight;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return weight == edge.weight && (this.start == edge.start) &&
                this.end == edge.end;
    }



}