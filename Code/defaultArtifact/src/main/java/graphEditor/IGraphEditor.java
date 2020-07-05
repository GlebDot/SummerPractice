package graphEditor;

import graph.*;

public interface IGraphEditor {
    public abstract void setEditState(boolean isEditState);
    public abstract IGraph getGraph();
}