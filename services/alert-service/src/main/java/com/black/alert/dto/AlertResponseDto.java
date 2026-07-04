package com.black.alert.dto;

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
public class AlertResponseDto {

    private Long id;
    private String serviceId;
    private String type;
    private Double thresholdValue;
    private Double actualValue;
    private String severity;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime triggeredAt;
}
