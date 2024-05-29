package me.iksadnorth.monitoring_server.repository;

import me.iksadnorth.monitoring_server.entity.CpuUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CpuUsageRepository extends JpaRepository<CpuUsage, Long>, CustomCpuUsageRepository {
}
