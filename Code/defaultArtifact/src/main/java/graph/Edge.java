package graph;

import java.util.Objects;

/**Class for storing information about edge of the graph */
public class Edge {
    Vertex start;
    Vertex end;
    int weight;

    public Edge(int weight, Vertex start, Vertex end){
        this.start = start;
        this.end = end;
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

//    public void changeWeight(int weight){
//        this.weight = weight;
//    }

}