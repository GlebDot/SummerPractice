package graph;

public interface IGraph {
//    public abstract IGraph clone();
    public abstract void addVertex(Vertex v);
    public abstract void addEdge(Edge e);
    public abstract void deleteEdge(Edge e);
    public abstract void deleteVertex(Vertex v);
}
