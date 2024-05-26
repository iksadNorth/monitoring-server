package me.iksadnorth.monitoring_server.repository;

import me.iksadnorth.monitoring_server.entity.CpuUsage;
import me.iksadnorth.monitoring_server.entity.CpuUsageSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CpuUsageRepository extends JpaRepository<CpuUsage, Long> {

    @Query(
            "SELECT c FROM CpuUsage c " +
                    "WHERE :dateFrom <= c.createdAt AND c.createdAt < :dateTo " +
                    "ORDER BY c.createdAt"
    )
    List<CpuUsage> findByCreatedAtBetween(LocalDateTime dateFrom, LocalDateTime dateTo);

    @Query(
            "SELECT MIN(c.createdAt) as createdAt, MIN(c.cpuUsage) as minUsage, " +
                    "MAX(c.cpuUsage) as maxUsage, AVG(c.cpuUsage) as avgUsage " +
                    "FROM CpuUsage c " +
                    "WHERE :dateFrom <= c.createdAt AND c.createdAt < :dateTo " +
                    "GROUP BY HOUR(c.createdAt) " +
                    "ORDER BY MIN(c.createdAt)"
    )
    List<CpuUsageSummary> findStaticsUsageWithRangeByHourUnit(LocalDateTime dateFrom, LocalDateTime dateTo);

    @Query(
            "SELECT MIN(c.createdAt) as createdAt, MIN(c.cpuUsage) as minUsage, " +
                    "MAX(c.cpuUsage) as maxUsage, AVG(c.cpuUsage) as avgUsage " +
                    "FROM CpuUsage c " +
                    "WHERE :dateFrom <= c.createdAt AND c.createdAt < :dateTo " +
                    "GROUP BY DAY(c.createdAt) " +
                    "ORDER BY MIN(c.createdAt)"
    )
    List<CpuUsageSummary> findStaticsUsageWithRangeByDayUnit(LocalDateTime dateFrom, LocalDateTime dateTo);
}
