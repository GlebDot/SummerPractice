package logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//should use singleton and proxy patterns
public class Logger implements ILogger {

    private static Logger instance;

    public ObservableList<String> strList;

    private Logger(){
        this.strList = FXCollections.observableArrayList("Edit history:");
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    @Override
    public void logEvent(AlgorithmMessage message) {
        strList.add(message.getMessage());
    }

    @Override
    public void logEvent(String message) {
        strList.add(message);
    }

    @Override
    public void clear() {
        strList.clear();
    }

    @Override
    public  String prepare(String message){
        StringBuilder str = new StringBuilder(message);
        int i = 0;
        int currLen = 0;
        while((i+currLen) != str.length()){
            if(str.charAt(i+currLen) == '\n'){
                i+=currLen;
                i++;
                currLen = 0;
                continue;
            }
            if(currLen == 29){
                if(str.charAt(i+currLen) == ' ' || str.charAt(i+currLen) == '\n'){
                    str.setCharAt(i+currLen, '\n');
                }
                else{
                    while(str.charAt(i+currLen) != ' ' || str.charAt(i+currLen) == '\n'){
                        currLen--;
                        if(str.charAt(i+currLen) == ' '){
                            str.setCharAt(i+currLen, '\n');
                            break;
                        }
                    }
                }
            }
            else{
                currLen++;
                continue;
            }
            i+=currLen;
            i++;
            currLen = 0;
        }
        return str.toString();
    }
}