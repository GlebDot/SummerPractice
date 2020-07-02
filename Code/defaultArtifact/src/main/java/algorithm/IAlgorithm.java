package algorithm;

import logger.*;
import graph.*;

public interface IAlgorithm {
    public abstract AlgorithmMessage stepForward();
    public abstract void initAlgorithm(Graph g);
}