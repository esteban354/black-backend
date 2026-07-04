package com.black.notification_service.mapper;

import com.black.notification_service.dto.NotificationResponseDto;
import com.black.notification_service.model.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationResponseDto toDto(Notification notification);
}
