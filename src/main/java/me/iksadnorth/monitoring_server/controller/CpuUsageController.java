package me.iksadnorth.monitoring_server.controller;

import lombok.RequiredArgsConstructor;
import me.iksadnorth.monitoring_server.dto.request.DateTimeRangeRequest;
import me.iksadnorth.monitoring_server.dto.request.DateTimeSpotRequest;
import me.iksadnorth.monitoring_server.dto.response.DayCpuUsageResponse;
import me.iksadnorth.monitoring_server.dto.response.HourCpuUsageResponse;
import me.iksadnorth.monitoring_server.dto.response.MinuteCpuUsageResponse;
import me.iksadnorth.monitoring_server.service.CpuUsageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cpu-usages")
@RequiredArgsConstructor
public class CpuUsageController implements CpuUsageControllerDocs {
    private final CpuUsageService cpuUsageService;

    @GetMapping("/minute")
    public MinuteCpuUsageResponse readMinuteCpuUsage(@ModelAttribute DateTimeRangeRequest request) {
        return cpuUsageService.readMinuteCpuUsage(request.getDateFrom(), request.getDateTo());
    }

    @GetMapping("/hour")
    public HourCpuUsageResponse readHourCpuUsage(@ModelAttribute DateTimeSpotRequest request) {
        return cpuUsageService.readHourCpuUsage(request.getDate());
    }

    @GetMapping("/day")
    public DayCpuUsageResponse readDayCpuUsage(@ModelAttribute DateTimeRangeRequest request) {
        return cpuUsageService.readDayCpuUsage(request.getDateFrom(), request.getDateTo());
    }
}
