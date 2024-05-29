package me.iksadnorth.monitoring_server.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimeTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.iksadnorth.monitoring_server.entity.CpuUsage;
import me.iksadnorth.monitoring_server.entity.CpuUsageSummary;
import me.iksadnorth.monitoring_server.entity.CpuUsageSummaryImpl;
import me.iksadnorth.monitoring_server.entity.QCpuUsage;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CpuUsageRepositoryImpl implements CustomCpuUsageRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CpuUsage> findByCreatedAtBetween(LocalDateTime dateFrom, LocalDateTime dateTo) {
        QCpuUsage qCpuUsage = QCpuUsage.cpuUsage1;

        List<CpuUsage> results = jpaQueryFactory.selectFrom(qCpuUsage)
                .where(isWithinDateRange(qCpuUsage, dateFrom, dateTo))
                .orderBy(qCpuUsage.createdAt.asc())
                .fetch();

        return results;
    }

    @Override
    public List<CpuUsageSummary> findStaticsUsageWithRangeByHourUnit(LocalDateTime dateFrom, LocalDateTime dateTo) {
        QCpuUsage qCpuUsage = QCpuUsage.cpuUsage1;

        DateTimeTemplate<Integer> hourTemplate = Expressions.dateTimeTemplate(Integer.class, "HOUR({0})", qCpuUsage.createdAt);

        List<CpuUsageSummary> results = jpaQueryFactory
                .select(
                        qCpuUsage.createdAt.min().as("createdAt"),
                        qCpuUsage.cpuUsage.min().as("minUsage"),
                        qCpuUsage.cpuUsage.max().as("maxUsage"),
                        qCpuUsage.cpuUsage.avg().as("avgUsage")
                )
                .from(qCpuUsage)
                .where(isWithinDateRange(qCpuUsage, dateFrom, dateTo))
                .groupBy(hourTemplate)
                .orderBy(qCpuUsage.createdAt.min().asc())
                .fetch()

                .stream()
                .map(tuple -> (CpuUsageSummary) new CpuUsageSummaryImpl(
                            tuple.get(0, LocalDateTime.class),
                            tuple.get(1, Double.class),
                            tuple.get(2, Double.class),
                            tuple.get(3, Double.class)
                    )
                )
                .toList();

        return results;
    }

    @Override
    public List<CpuUsageSummary> findStaticsUsageWithRangeByDayUnit(LocalDateTime dateFrom, LocalDateTime dateTo) {
        QCpuUsage qCpuUsage = QCpuUsage.cpuUsage1;

        DateTimeTemplate<Integer> dayTemplate = Expressions.dateTimeTemplate(Integer.class, "DAY({0})", qCpuUsage.createdAt);

        List<CpuUsageSummary> results = jpaQueryFactory
                .select(
                        qCpuUsage.createdAt.min().as("createdAt"),
                        qCpuUsage.cpuUsage.min().as("minUsage"),
                        qCpuUsage.cpuUsage.max().as("maxUsage"),
                        qCpuUsage.cpuUsage.avg().as("avgUsage")
                )
                .from(qCpuUsage)
                .where(isWithinDateRange(qCpuUsage, dateFrom, dateTo))
                .groupBy(dayTemplate)
                .orderBy(qCpuUsage.createdAt.min().asc())
                .fetch()

                .stream()
                .map(tuple -> (CpuUsageSummary) new CpuUsageSummaryImpl(
                            tuple.get(0, LocalDateTime.class),
                            tuple.get(1, Double.class),
                            tuple.get(2, Double.class),
                            tuple.get(3, Double.class)
                        )
                )
                .toList();

        return results;
    }

    private BooleanExpression isWithinDateRange(QCpuUsage qCpuUsage, LocalDateTime dateFrom, LocalDateTime dateTo) {
        BooleanExpression goe = qCpuUsage.createdAt.goe(dateFrom);
        BooleanExpression lt = qCpuUsage.createdAt.lt(dateTo);
        return goe.and(lt);
    }
}
