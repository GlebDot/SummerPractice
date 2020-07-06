package graphEditor;

import graph.*;

public interface IGraphEditor {
    public abstract void setEditState(boolean isEditState);
    public abstract IGraph getGraph();
    public abstract void setCurrentEdge();
    public abstract void setCurrentVertex();

    public abstract void clearEditor();
    public abstract void loadGraph(Graph graph);
}