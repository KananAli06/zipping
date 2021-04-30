package az.dynamics.task.exception;

import az.dynamics.task.model.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * @author Kanan
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            TaskNotFoundException.class,
            ProcessNotFoundException.class,
            FileNotFoundException.class
    })
    public final ResponseEntity<ErrorResponse> handleNotFoundException(Exception e) {
        return new ResponseEntity<>(new ErrorResponse("NOT_FOUND", e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        String message = Objects.requireNonNull(result.getFieldError()).getField() + " "
                + result.getFieldError().getDefaultMessage();
        return new ResponseEntity<>(new ErrorResponse("BAD_VALUE", message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<ErrorResponse> handleNotReadableMessage(HttpMessageNotReadableException e) {
        return new ResponseEntity<>(new ErrorResponse("BAD_REQUEST", e.getMessage())
                , HttpStatus.BAD_REQUEST);
    }

}
