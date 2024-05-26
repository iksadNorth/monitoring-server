package me.iksadnorth.monitoring_server.service;

import me.iksadnorth.monitoring_server.entity.CpuUsage;
import me.iksadnorth.monitoring_server.exception.MonitoringServerException;
import me.iksadnorth.monitoring_server.fixture.DateTimeFixture;
import me.iksadnorth.monitoring_server.repository.CpuUsageRepository;
import me.iksadnorth.monitoring_server.util.UsageUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CpuUsageServiceTest {
    @InjectMocks
    private CpuUsageService cpuUsageService;

    @Mock
    private CpuUsageRepository cpuUsageRepository;
    @Mock
    private UsageUtil usageUtil;

    @Test
    void collectCpuUsage_saveDatabaseNormally() {
        // given
        when(usageUtil.getCpuUsage())
                .thenReturn(10d);

        // when
        cpuUsageService.collectCpuUsage();

        // then
        verify(cpuUsageRepository).save(
                any(CpuUsage.class)
        );
    }

    @Test
    void collectCpuUsage_handleExceptionWhenCollectingUsage() {
        // given
        when(usageUtil.getCpuUsage())
                .thenThrow(RuntimeException.class);

        // when & then
        Assertions.assertDoesNotThrow(cpuUsageService::collectCpuUsage);
    }

    @Test
    void readMinuteCpuUsage_runNormally() {
        // given
        LocalDateTime dateFrom = DateTimeFixture.getDateFromBeforeBoundaryWhenCallMinuteReadApi();
        LocalDateTime dateTo = DateTimeFixture.getDateToBeforeBoundaryWhenCallMinuteReadApi();

        when(cpuUsageRepository.findByCreatedAtBetween(any(), any()))
                .thenReturn(List.of());

        // when & then
        Assertions.assertDoesNotThrow(() -> {
            cpuUsageService.readMinuteCpuUsage(dateFrom, dateTo);
        });
    }

    @Test
    void readMinuteCpuUsage_throwErrorWhenGivenOutOfRangeDateTime() {
        // given
        LocalDateTime dateFrom = DateTimeFixture.getDateFromBoundaryWhenCallMinuteReadApi();
        LocalDateTime dateTo = DateTimeFixture.getDateToBoundaryWhenCallMinuteReadApi();

        // when & then
        Assertions.assertThrows(MonitoringServerException.class, () -> {
            cpuUsageService.readMinuteCpuUsage(dateFrom, dateTo);
        });
    }

    @Test
    void readHourCpuUsage_runNormally() {
        // given
        LocalDateTime date = DateTimeFixture.getDateBeforeBoundaryWhenCallHourReadApi();

        when(cpuUsageRepository.findStaticsUsageWithRangeByHourUnit(any(), any()))
                .thenReturn(List.of());

        // when & then
        Assertions.assertDoesNotThrow(() -> {
            cpuUsageService.readHourCpuUsage(date);
        });
    }

    @Test
    void readHourCpuUsage_throwErrorWhenGivenOutOfRangeDateTime() {
        // given
        LocalDateTime date = DateTimeFixture.getDateBoundaryWhenCallHourReadApi();

        // when & then
        Assertions.assertThrows(MonitoringServerException.class, () -> {
            cpuUsageService.readHourCpuUsage(date);
        });
    }

    @Test
    void readDayCpuUsage_runNormally() {
        // given
        LocalDateTime dateFrom = DateTimeFixture.getDateFromBeforeBoundaryWhenCallDayReadApi();
        LocalDateTime dateTo = DateTimeFixture.getDateToBeforeBoundaryWhenCallDayReadApi();

        when(cpuUsageRepository.findStaticsUsageWithRangeByDayUnit(any(), any()))
                .thenReturn(List.of());

        // when & then
        Assertions.assertDoesNotThrow(() -> {
            cpuUsageService.readDayCpuUsage(dateFrom, dateTo);
        });
    }

    @Test
    void readDayCpuUsage_throwErrorWhenGivenOutOfRangeDateTime() {
        // given
        LocalDateTime dateFrom = DateTimeFixture.getDateFromBoundaryWhenCallDayReadApi();
        LocalDateTime dateTo = DateTimeFixture.getDateToBoundaryWhenCallDayReadApi();

        // when & then
        Assertions.assertThrows(MonitoringServerException.class, () -> {
            cpuUsageService.readDayCpuUsage(dateFrom, dateTo);
        });
    }
}