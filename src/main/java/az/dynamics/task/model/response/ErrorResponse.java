package az.dynamics.task.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Kanan
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;

}
