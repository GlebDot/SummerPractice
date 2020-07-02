package algorithm;

import logger.*;
import graph.*;

/**Class for Ford-Bellman algorithm. Stores his own version of graph and work with it.
 * <p>Executes algorithm step by step with function {@link#stepForward}</p>
 */
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