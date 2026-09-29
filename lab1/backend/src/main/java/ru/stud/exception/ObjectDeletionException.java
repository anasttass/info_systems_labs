package ru.stud.exception;

import ru.stud.dto.ErrorCode;

public class ObjectDeletionException extends RuntimeException {

    private final ErrorCode errorCode;

    public ObjectDeletionException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
