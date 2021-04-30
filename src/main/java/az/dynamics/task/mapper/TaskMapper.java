package az.dynamics.task.mapper;

import az.dynamics.task.entity.Task;
import az.dynamics.task.model.response.ZippingInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * @author Kanan
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    @Mapping(target = "status", source = "process.status")
    @Mapping(target = "filePath", source = "zipFilePath")
    ZippingInfoDto toDto(Task entity);
}
