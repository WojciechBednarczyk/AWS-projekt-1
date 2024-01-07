package pl.edu.pwr.backend.exception;

public class GameIsFinishedException extends RuntimeException {
    public GameIsFinishedException(String errorMessage) {
        super(errorMessage);
    }
}
