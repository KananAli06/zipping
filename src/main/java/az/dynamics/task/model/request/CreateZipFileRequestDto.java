package az.dynamics.task.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author Kanan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateZipFileRequestDto {
    @NotBlank
    private String path;
}
