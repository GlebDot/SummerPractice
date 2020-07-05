package logger;

import javafx.collections.ObservableList;

//should use singleton and proxy patterns
public class Logger implements ILogger {

    private static Logger instance;

    public ObservableList<String> strList;

    private Logger(ObservableList<String> list){
        this.strList = list;
    }

    public static Logger getInstance(ObservableList<String> list) {
        if (instance == null) {
            instance = new Logger(list);
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
}