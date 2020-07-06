package graphEditor;

import graph.*;

public interface IGraphEditor {
    public abstract void setEditState(boolean isEditState);
    public abstract IGraph getGraph();
    public abstract void setCurrentEdge(Edge e);
    public abstract void setCurrentVertex(Vertex v);

    public abstract void clearEditor();
    public abstract void loadGraph(Graph graph);
    public abstract void rerunEditor();
}