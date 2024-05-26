package me.iksadnorth.monitoring_server.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.iksadnorth.monitoring_server.entity.CpuUsage;

import java.time.LocalDateTime;

@RequiredArgsConstructor @Getter
public class UnitCpuUsageDto {
    private final LocalDateTime createdAt;
    private final Double usage;

    public static UnitCpuUsageDto fromEntity(CpuUsage entity) {
        return new UnitCpuUsageDto(entity.getCreatedAt(), entity.getCpuUsage());
    }
}
