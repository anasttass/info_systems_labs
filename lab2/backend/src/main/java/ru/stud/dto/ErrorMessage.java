package ru.stud.dto;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Getter;

@AllArgsConstructor
public class ErrorMessage {
    private String message;
    private ErrorCode error_code;

    public ErrorMessage(ErrorCode error_code, String message){
        this.error_code=error_code;
        this.message=message;
    }

    public ErrorCode getErrorСode() {
        return error_code;
    }

    public String getMessage() {
        return message;
    }

    public void setErrorСode(ErrorCode error_code) {
        this.error_code = error_code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
