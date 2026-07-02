package com.black.alert.model;

import com.black.alert.enums.AlertType;
import com.black.alert.enums.Severity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertEvent {

    private Long alertId;
    private String serviceId;
    private AlertType type;
    private Severity severity;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime triggeredAt;
}
