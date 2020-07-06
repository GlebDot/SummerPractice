package logger;

/**This is class for sending special message to logger from Algorithm 
 * Rigth now it contains only a string of information and should be extended
*/
public class AlgorithmMessage {
    private String message;

    public AlgorithmMessage(){
        message = null;
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
        if(viewingEdge!= null) {
            this.changeV = viewingEdge.end;
            this.startV = viewingEdge.start;
        }
        this.isFinish = isFinish;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String mes) {
        message = mes;
    }
}