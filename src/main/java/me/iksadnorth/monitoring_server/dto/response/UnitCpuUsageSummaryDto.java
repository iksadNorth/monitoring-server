package me.iksadnorth.monitoring_server.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.iksadnorth.monitoring_server.entity.CpuUsageSummary;

import java.time.LocalDateTime;

@RequiredArgsConstructor @Getter
public class UnitCpuUsageSummaryDto {
    private final LocalDateTime createdAt;
    private final Double minUsage;
    private final Double maxUsage;
    private final Double avgUsage;

    public static UnitCpuUsageSummaryDto fromEntity(CpuUsageSummary entity) {
        return new UnitCpuUsageSummaryDto(entity.getCreatedAt(), entity.getMinUsage(), entity.getMaxUsage(), entity.getAvgUsage());
    }
}
