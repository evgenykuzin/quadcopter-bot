package ru.jekajops.quadcopterbot.exceptions;

public class ProcessException extends Exception {
    private final StatusType statusType;

    public ProcessException(StatusType statusType) {
        this.statusType = statusType;
    }

    public ProcessException(String message, StatusType statusType) {
        super(message);
        this.statusType = statusType;
    }

    public ProcessException(String message, Throwable cause, StatusType statusType) {
        super(message, cause);
        this.statusType = statusType;
    }

    public ProcessException(Throwable cause, StatusType statusType) {
        super(cause);
        this.statusType = statusType;
    }

    public StatusType getStatusType() {
        return statusType;
    }
}
