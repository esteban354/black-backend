package com.black.alert.mapper;

import com.black.alert.dto.AlertResponseDto;
import com.black.alert.model.Alert;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlertMapper {

    AlertResponseDto toDto(Alert alert);
}
