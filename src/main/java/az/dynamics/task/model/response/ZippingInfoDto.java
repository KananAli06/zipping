package az.dynamics.task.model.response;

import az.dynamics.task.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kanan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZippingInfoDto {
    private Status status;
    private String filePath;
}
