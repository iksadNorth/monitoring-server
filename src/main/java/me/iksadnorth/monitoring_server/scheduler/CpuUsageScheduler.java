package me.iksadnorth.monitoring_server.scheduler;

import lombok.RequiredArgsConstructor;
import me.iksadnorth.monitoring_server.service.CpuUsageService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CpuUsageScheduler {
    private final CpuUsageService cpuUsageService;

    @Scheduled(cron = "${schedule.cron.collect.cpu-usage}")
    public void runCollectCpuUsage() {
        cpuUsageService.collectCpuUsage();
    }
}
