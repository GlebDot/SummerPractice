package logger;

public interface ILogger {
    public abstract void logEvent(AlgorithmMessage message);

    public abstract void clear();
}