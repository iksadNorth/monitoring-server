package me.iksadnorth.monitoring_server.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor @Getter
public class CpuUsageSummaryImpl implements CpuUsageSummary {
    private final LocalDateTime createdAt;
    private final Double minUsage;
    private final Double maxUsage;
    private final Double avgUsage;
}
