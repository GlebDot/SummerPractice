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
    private boolean isEndOfCycle;

    public AlgorithmMessage(){
        message = null;
        viewingEdge = null;
        changeV = null;
        startV = null;
        isFinish = false;
        isEndOfCycle = false;
    }

    public AlgorithmMessage(String mes) {
        message = mes;
        viewingEdge = null;
        changeV = null;
        startV = null;
        isFinish = false;
        isEndOfCycle = false;
    }

    public AlgorithmMessage(String mes, Edge viewingEdge, boolean isFinish, boolean isEndOfCycle) {
        message = mes;
        this.viewingEdge = viewingEdge;
        if(viewingEdge!= null) {
            this.changeV = viewingEdge.end;
            this.startV = viewingEdge.start;
        }
        else{
            this.changeV = null;
            this.startV = null;
        }
        this.isFinish = isFinish;
        this.isEndOfCycle = isEndOfCycle;
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

    public boolean isEndOfCycle() {
        return this.isEndOfCycle;
    }
}