package logger;

import graph.Edge;
import graph.Vertex;

/**This is class for sending special message to logger from Algorithm
 * Rigth now it contains only a string of information and should be extended
*/
public class AlgorithmMessage {
    private String message;
    private Edge viewingEdge;
    private Vertex changeV;
    private Vertex startV;
    private boolean isFinish;

    public AlgorithmMessage(){
        message = null;
        viewingEdge = null;
        changeV = null;
        startV = null;
        isFinish = false;
    }

    public AlgorithmMessage(String mes) {
        message = mes;
        viewingEdge = null;
        changeV = null;
        startV = null;
        isFinish = false;
    }

    public AlgorithmMessage(String mes, Edge viewingEdge, boolean isFinish) {
        message = mes;
        this.viewingEdge = viewingEdge;
        this.changeV = viewingEdge.end;
        this.startV = viewingEdge.start;
        this.isFinish = isFinish;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String mes) {
        message = mes;
    }

    public Vertex getChangeV(){
        return this.changeV;
    }

    public  Vertex getStartV(){
        return this.startV;
    }

    public  Edge getViewingEdge(){
        return  this.viewingEdge;
    }

    public boolean isFinish(){
        return this.isFinish;
    }
}
