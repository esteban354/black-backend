package com.black.incident_service.mapper;

import com.black.incident_service.dto.IncidentResponseDto;
import com.black.incident_service.model.Incident;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IncidentMapper {

    IncidentResponseDto toDto(Incident incident);
}
