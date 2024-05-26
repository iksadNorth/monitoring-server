package me.iksadnorth.monitoring_server.util;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsageUtil {
    private final MeterRegistry meterRegistry;

    public double getCpuUsage() {
        return meterRegistry.get("system.cpu.usage").gauge().value() * 100;
    }
}
