package me.iksadnorth.monitoring_server.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.iksadnorth.monitoring_server.config.DateTimeFormatConfig;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor
public class DateTimeRangeRequest {
    @DateTimeFormat(pattern = DateTimeFormatConfig.FORMAT)
    private LocalDateTime dateFrom;
    @DateTimeFormat(pattern = DateTimeFormatConfig.FORMAT)
    private LocalDateTime dateTo;
}
