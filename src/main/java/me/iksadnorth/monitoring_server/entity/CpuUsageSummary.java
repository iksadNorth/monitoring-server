package me.iksadnorth.monitoring_server.entity;

import java.time.LocalDateTime;

public interface CpuUsageSummary {
    LocalDateTime getCreatedAt();
    Double getMinUsage();
    Double getMaxUsage();
    Double getAvgUsage();

    default String TransformToString() {
        return String.format(
                "\nCreatedAt | %s \nMinUsage | %s \nMaxUsage | %s \nAvgUsage | %s \n ",
                getCreatedAt().toString(),
                getMinUsage().toString(),
                getMaxUsage().toString(),
                getAvgUsage().toString()
        );
    }
}
