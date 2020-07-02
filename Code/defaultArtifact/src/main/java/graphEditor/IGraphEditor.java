package graphEditor;

import graph.*;

public interface IGraphEditor {
    public abstract void addEdge(Edge e);
    public abstract void addVertex(Vertex v);

    public abstract IGraph getGraph();
}