package rs.raf.demo.exceptions;

import lombok.Getter;
import rs.raf.demo.model.ErrorMessage;

@Getter
public class VacuumException extends Exception{
    private ErrorMessage errorMessage;

    public VacuumException(String message) {
        super(message);
    }

    public VacuumException(ErrorMessage errorMessage) {
        super(errorMessage.getErrorMessage());
        this.errorMessage = errorMessage;
    }
}
