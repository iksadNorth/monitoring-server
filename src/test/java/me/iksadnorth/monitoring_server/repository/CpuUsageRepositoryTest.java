package me.iksadnorth.monitoring_server.repository;

import lombok.extern.slf4j.Slf4j;
import me.iksadnorth.monitoring_server.entity.CpuUsage;
import me.iksadnorth.monitoring_server.entity.CpuUsageSummary;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@DataJpaTest
class CpuUsageRepositoryTest {
    @Autowired
    private CpuUsageRepository cpuUsageRepository;

    @SqlGroup({
            @Sql({"classpath:data/testcase-for-minute-api.sql"}),
            @Sql(scripts = "classpath:data/clean-up.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    void findByCreatedAtBetween() {
        // given
        final int GAP = 10;

        LocalDateTime dateFrom = LocalDateTime.of(2024, 5, 20, 0, 0);
        LocalDateTime dateTo = dateFrom.plus(GAP, ChronoUnit.MINUTES);

        // when
        List<CpuUsage> cpuUsageList = cpuUsageRepository.findByCreatedAtBetween(dateFrom, dateTo);

        // then
        // Check Usage
        Assertions.assertThat(cpuUsageList)
                .extracting(CpuUsage::getCpuUsage)
                .containsExactly(
                        10d, 40d, 40d, 40d, 40d,
                        40d, 40d, 40d, 40d, 70d
                );

        // Log EntityList
        cpuUsageList.stream()
                .map(CpuUsage::toString)
                .forEach(log::info);
    }

    @SqlGroup({
            @Sql({"classpath:data/testcase-for-hour-api.sql"}),
            @Sql(scripts = "classpath:data/clean-up.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    void findStaticsUsageWithRangeByHourUnit() {
        // given
        final int GAP = 4;

        LocalDateTime dateFrom = LocalDateTime.of(2024, 5, 20, 0, 0);
        LocalDateTime dateTo = dateFrom.plus(GAP, ChronoUnit.HOURS);

        // when
        List<CpuUsageSummary> cpuUsageList = cpuUsageRepository.findStaticsUsageWithRangeByHourUnit(dateFrom, dateTo);

        // then
        // Check MinUsage
        Assertions.assertThat(cpuUsageList)
                .extracting(CpuUsageSummary::getMinUsage)
                .containsExactly(
                        10d, 10d, 10d, 20d
                );
        // Check AvgUsage
        Assertions.assertThat(cpuUsageList)
                .extracting(CpuUsageSummary::getAvgUsage)
                .containsExactly(
                        20d, 30d, 40d, 50d
                );
        // Check MaxUsage
        Assertions.assertThat(cpuUsageList)
                .extracting(CpuUsageSummary::getMaxUsage)
                .containsExactly(
                        30d, 50d, 70d, 80d
                );

        // Log EntityList
        cpuUsageList.stream()
                .map(CpuUsageSummary::TransformToString)
                .forEach(log::info);
    }

    @SqlGroup({
            @Sql({"classpath:data/testcase-for-day-api.sql"}),
            @Sql(scripts = "classpath:data/clean-up.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    void findStaticsUsageWithRangeByDayUnit() {
        // given
        final int GAP = 2;

        LocalDateTime dateFrom = LocalDateTime.of(2024, 5, 20, 0, 0);
        LocalDateTime dateTo = dateFrom.plus(GAP, ChronoUnit.DAYS);

        // when
        List<CpuUsageSummary> cpuUsageList = cpuUsageRepository.findStaticsUsageWithRangeByDayUnit(dateFrom, dateTo);

        // then
        // Check MinUsage
        Assertions.assertThat(cpuUsageList)
                .extracting(CpuUsageSummary::getMinUsage)
                .containsExactly(
                        10d, 10d
                );
        // Check AvgUsage
        Assertions.assertThat(cpuUsageList)
                .extracting(CpuUsageSummary::getAvgUsage)
                .containsExactly(
                        20d, 30d
                );
        // Check MaxUsage
        Assertions.assertThat(cpuUsageList)
                .extracting(CpuUsageSummary::getMaxUsage)
                .containsExactly(
                        30d, 50d
                );

        // Log EntityList
        cpuUsageList.stream()
                .map(CpuUsageSummary::TransformToString)
                .forEach(log::info);
    }
}