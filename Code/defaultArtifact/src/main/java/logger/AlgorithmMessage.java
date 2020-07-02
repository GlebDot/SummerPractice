package logger;

/**This is class for sending special message to logger from Algorithm 
 * Rigth now it contains only a string of information and should be extended
*/
public class AlgorithmMessage {
    private String message;

    public AlgorithmMessage(){}

    public AlgorithmMessage(String mes) {
        message = mes;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String mes) {
        message = mes;
    }
}