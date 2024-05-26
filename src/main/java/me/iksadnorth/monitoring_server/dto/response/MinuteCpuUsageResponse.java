package me.iksadnorth.monitoring_server.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor @Getter
public class MinuteCpuUsageResponse {
    private final List<UnitCpuUsageDto> arrayCpuUsage;
}
