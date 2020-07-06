package logger;

import javafx.collections.ObservableList;

public interface ILogger {
    public abstract void logEvent(AlgorithmMessage message);

    public abstract void logEvent(String message);

    public abstract void clear();

    public abstract String prepare(String message);
}