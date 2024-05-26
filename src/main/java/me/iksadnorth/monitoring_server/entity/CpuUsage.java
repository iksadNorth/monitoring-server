package me.iksadnorth.monitoring_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Entity @Table(name = "CPU_USAGE")
@Getter @Setter @NoArgsConstructor
public class CpuUsage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double cpuUsage;

    private LocalDateTime createdAt;

    public CpuUsage(Double cpuUsage, LocalDateTime createdAt) {
        this.cpuUsage = cpuUsage;
        this.createdAt = createdAt;
    }
}
