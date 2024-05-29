package me.iksadnorth.monitoring_server.repository;

import me.iksadnorth.monitoring_server.entity.CpuUsage;
import me.iksadnorth.monitoring_server.entity.CpuUsageSummary;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomCpuUsageRepository {
    List<CpuUsage> findByCreatedAtBetween(LocalDateTime dateFrom, LocalDateTime dateTo);

    List<CpuUsageSummary> findStaticsUsageWithRangeByHourUnit(LocalDateTime dateFrom, LocalDateTime dateTo);

    List<CpuUsageSummary> findStaticsUsageWithRangeByDayUnit(LocalDateTime dateFrom, LocalDateTime dateTo);
}
