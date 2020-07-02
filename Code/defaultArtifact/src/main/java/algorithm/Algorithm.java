package algorithm;

import logger.*;
import graph.*;

public class Algorithm implements IAlgorithm {
    IGraph graph;

    @Override
    public AlgorithmMessage stepForward() {
        return new AlgorithmMessage();
    }

    @Override
    public void initAlgorithm(Graph g) {
        
    }
}