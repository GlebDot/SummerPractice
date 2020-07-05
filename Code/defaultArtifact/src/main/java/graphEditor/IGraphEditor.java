package graphEditor;

import graph.*;

public interface IGraphEditor {
    public abstract void setState(boolean isEditState);
    public abstract IGraph getGraph();
}