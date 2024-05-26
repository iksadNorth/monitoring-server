package me.iksadnorth.monitoring_server.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.iksadnorth.monitoring_server.dto.response.*;
import me.iksadnorth.monitoring_server.entity.CpuUsage;
import me.iksadnorth.monitoring_server.exception.MonitoringServerException;
import me.iksadnorth.monitoring_server.repository.CpuUsageRepository;
import me.iksadnorth.monitoring_server.util.UsageUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CpuUsageService {
    private final CpuUsageRepository cpuUsageRepository;
    private final UsageUtil usageUtil;

    @Transactional
    public void collectCpuUsage() {
        try {
            double cpuUsage = usageUtil.getCpuUsage();
            LocalDateTime createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

            CpuUsage entity = new CpuUsage(cpuUsage, createdAt);
            cpuUsageRepository.save(entity);

        } catch (Exception e) {
            log.warn("데이터 수집 실패!");
            if (log.isWarnEnabled()) {
                e.printStackTrace();
            }
        }
    }

    public MinuteCpuUsageResponse readMinuteCpuUsage(LocalDateTime dateFrom, LocalDateTime dateTo) {
        validateParameterOfMinuteCpuUsage(dateFrom, dateTo);

        dateFrom = dateFrom.truncatedTo(ChronoUnit.MINUTES);
        dateTo = dateTo.truncatedTo(ChronoUnit.MINUTES);

        List<UnitCpuUsageDto> ArrayCpuUsage = cpuUsageRepository.findByCreatedAtBetween(dateFrom, dateTo)
                .stream()
                .map(UnitCpuUsageDto::fromEntity)
                .toList();
        return new MinuteCpuUsageResponse(ArrayCpuUsage);
    }

    public void validateParameterOfMinuteCpuUsage(LocalDateTime dateFrom, LocalDateTime dateTo) {
        if (dateFrom == null || dateTo == null) {
            throw new MonitoringServerException(HttpStatus.BAD_REQUEST, "조회 시작일과 조회 종료일 모두 설정해주세요.");
        }

        if (dateFrom.compareTo(dateTo) > 0) {
            throw new MonitoringServerException(HttpStatus.BAD_REQUEST, "조회 시작일이 조회 종료일보다 늦을 수 없습니다.");
        }

        LocalDateTime dateTimeValid = LocalDateTime.now().minus(1, ChronoUnit.WEEKS);
        if (dateFrom.compareTo(dateTimeValid) <= 0) {
            throw new MonitoringServerException(HttpStatus.BAD_REQUEST, "최근 1주 데이터 제공만 제공됩니다.");
        }
    }

    public HourCpuUsageResponse readHourCpuUsage(LocalDateTime date) {
        validateParameterOfHourCpuUsage(date);

        LocalDateTime dateFrom = date.truncatedTo(ChronoUnit.HOURS);
        LocalDateTime dateTo = dateFrom.plus(1, ChronoUnit.DAYS);

        List<UnitCpuUsageSummaryDto> ArrayCpuUsage = cpuUsageRepository.findStaticsUsageWithRangeByHourUnit(dateFrom, dateTo)
                .stream()
                .map(UnitCpuUsageSummaryDto::fromEntity)
                .toList();
        return new HourCpuUsageResponse(ArrayCpuUsage);
    }

    public void validateParameterOfHourCpuUsage(LocalDateTime date) {
        if (date == null) {
            throw new MonitoringServerException(HttpStatus.BAD_REQUEST, "조회 시점이 설정되지 않았습니다.");
        }

        LocalDateTime dateTimeValid = LocalDateTime.now().minus(3, ChronoUnit.MONTHS);
        if (date.compareTo(dateTimeValid) <= 0) {
            throw new MonitoringServerException(HttpStatus.BAD_REQUEST, "최근 3달 데이터 제공만 제공됩니다.");
        }
    }

    public DayCpuUsageResponse readDayCpuUsage(LocalDateTime dateFrom, LocalDateTime dateTo) {
        validateParameterOfDayCpuUsage(dateFrom, dateTo);

        dateFrom = dateFrom.truncatedTo(ChronoUnit.DAYS);
        dateTo = dateTo.truncatedTo(ChronoUnit.DAYS);

        List<UnitCpuUsageSummaryDto> ArrayCpuUsage = cpuUsageRepository.findStaticsUsageWithRangeByDayUnit(dateFrom, dateTo)
                .stream()
                .map(UnitCpuUsageSummaryDto::fromEntity)
                .toList();
        return new DayCpuUsageResponse(ArrayCpuUsage);
    }

    public void validateParameterOfDayCpuUsage(LocalDateTime dateFrom, LocalDateTime dateTo) {
        if (dateFrom == null || dateTo == null) {
            throw new MonitoringServerException(HttpStatus.BAD_REQUEST, "조회 시작일과 조회 종료일 모두 설정해주세요.");
        }

        if (dateFrom.compareTo(dateTo) > 0) {
            throw new MonitoringServerException(HttpStatus.BAD_REQUEST, "조회 시작일이 조회 종료일보다 늦을 수 없습니다.");
        }

        LocalDateTime dateTimeValid = LocalDateTime.now().minus(1, ChronoUnit.YEARS);
        if (dateFrom.compareTo(dateTimeValid) <= 0) {
            throw new MonitoringServerException(HttpStatus.BAD_REQUEST, "최근 1년 데이터 제공만 제공됩니다.");
        }
    }
}
