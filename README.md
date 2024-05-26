# 서버 CPU 사용률 모니터링 시스템 구현

---

## 프로젝트 설정 및 실행 방법
> 우선 JVM이 설치되어 있다는 전제 하에 서술하겠습니다.
> 해당 프로젝트는 H2 DB를 사용하는 개발 환경[test]과 MariaDB를 사용하는 운영 환경[prod]라는 Profile이 존재합니다.


> 우선 운영 환경[prod]에서의 프로젝트 실행 방법입니다.
- Git Clone을 한 뒤, Project Root에서 아래와 같은 명령어를 입력하면, 운영 환경[prod]에서 프로젝트가 실행됩니다.
- 빠른 프로젝트 재현을 위해, Build 결과물인 Jar 파일을 함께 Git Commit을 수행했습니다.
- 추가로 MariaDB 역시, Amazon RDS를 이용해서 미리 스키마와 테스트 데이터를 설정 및 적재했습니다.
- 때문에, 아래 명령어 만으로 운영 환경[prod]에서의 프로젝트를 실행할 수 있습니다.
```text
java -Dspring.datasource.password={Email에_첨부드린_DB_Password} -Dspring.profiles.default=prod -jar build/libs/monitoring-server-0.0.1-SNAPSHOT.jar
```


> 그 다음은 개발 환경[test]에서의 프로젝트 실행 방법입니다.
- Git Clone을 한 뒤, Project Root에서 아래와 같은 명령어를 입력하면, 개발 환경[test]에서 프로젝트가 실행됩니다.
```text
java -Dspring.profiles.default=test -jar build/libs/monitoring-server-0.0.1-SNAPSHOT.jar
```


> MariaDB의 스키마 설정 및 데이터 적재를 위한 덤프 파일은 "src/main/resources/data/dump-for-mariadb.sql"를 참고하시면 됩니다.

## API 문서 작성
| Description     | URI                      | Method | Parameter                                                                 | Response                                                                                          |
|-----------------|--------------------------|--------|--------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------|
| 분 단위 조회 API | /api/v1/cpu-usage/minute | GET    | dateFrom: ‘yyyy-MM-dd HH:mm:ss 형식 날짜’<br>dateTo: ‘yyyy-MM-dd HH:mm:ss 형식 날짜’ | {<br>“arrayCpuUsage” : [<br>{<br>”createdAt” : “2024-05-20T00:00:00”, <br>“usage”: 10.0<br>}, …<br>],<br>} |
| 시 단위 조회 API | /api/v1/cpu-usage/hour   | GET    | date: ‘yyyy-MM-dd HH:mm:ss 형식 날짜’                                    | {<br>“arrayCpuUsage” : [<br>{<br>""createdAt"":""2024-05-20T00:00:00"",<br>""minUsage"":10.0,<br>""maxUsage"":30.0,<br>""avgUsage"":20.0<br>}, …<br>],<br>} |
| 일 단위 조회 API | /api/v1/cpu-usage/day    | GET    | dateFrom: ‘yyyy-MM-dd HH:mm:ss 형식 날짜’<br>dateTo: ‘yyyy-MM-dd HH:mm:ss 형식 날짜’ | {<br>“arrayCpuUsage” : [<br>{<br>""createdAt"":""2024-05-20T00:00:00"",<br>""minUsage"":10.0,<br>""maxUsage"":30.0,<br>""avgUsage"":20.0<br>}, …<br>],<br>} |

- 위 [프로젝트 설정 및 실행 방법] 내용을 통해 서버가 존재한다면 아래 URL을 통해 API에 접근이 가능합니다.
  - http://localhost:8080/api/v1/cpu-usages/minute?dateFrom=2024-05-20%2000%3A00%3A00&dateTo=2024-05-20%2000%3A10%3A00
  - http://localhost:8080/api/v1/cpu-usages/hour?date=2024-05-20%2000%3A00%3A00
  - http://localhost:8080/api/v1/cpu-usages/day?dateFrom=2024-05-20%2000%3A00%3A00&dateTo=2024-05-21%2000%3A00%3A00
- 위와 같은 링크를 통해 접근할 수도 있지만 Swagger API 문서를 통해 API를 살펴보실 수 있습니다.
  - http://localhost:8080/swagger


## 요구사항의 해결 방법
### CPU 사용량 수집
- CPU 사용량을 1분 마다 수집을 하기 위해, Scheduling를 이용해서 실현했습니다.
- 수집된 데이터를 데이터베이스에 저장합니다.
- 데이터 수집 실패 시 예외를 처리하고 로그를 남깁니다.
```properties
## src/main/resources/application-prod.properties

# Scheduler Config
schedule.cron.collect.cpu-usage=0 * * * * *
```
```java
// src/main/java/me/iksadnorth/monitoring_server/scheduler/CpuUsageScheduler.java
@Component
@RequiredArgsConstructor
public class CpuUsageScheduler {
    private final CpuUsageService cpuUsageService;

    @Scheduled(cron = "${schedule.cron.collect.cpu-usage}")
    public void runCollectCpuUsage() {
        cpuUsageService.collectCpuUsage();
    }
}
```
```java
// src/main/java/me/iksadnorth/monitoring_server/service/CpuUsageService.java
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
```
### 분 단위 조회를 위한 DB 쿼리문
> 단순히 DB에는 분 단위로 데이터가 저장되어 있기 때문에 SQL 문이 단조로웠습니다.
```roomsql
SELECT c FROM CpuUsage c 
WHERE :dateFrom <= c.createdAt AND c.createdAt < :dateTo 
ORDER BY c.createdAt
```
### 시 단위 조회를 위한 DB 쿼리문
> Hour 단위로 각 CPU 사용률의 최소/최대/평균값을 가지고 오기 위해 createdAt 칼럼의 Hour를 기준으로 Group By를 시키고 각 군집의 통계값을 계산합니다.
```roomsql
SELECT MIN(c.createdAt) as createdAt, MIN(c.cpuUsage) as minUsage, 
    MAX(c.cpuUsage) as maxUsage, AVG(c.cpuUsage) as avgUsage 
FROM CpuUsage c 
WHERE :dateFrom <= c.createdAt AND c.createdAt < :dateTo 
GROUP BY HOUR(c.createdAt) 
ORDER BY MIN(c.createdAt)
```
### 일 단위 조회를 위한 DB 쿼리문
> Day 단위로 각 CPU 사용률의 최소/최대/평균값을 가지고 오기 위해 createdAt 칼럼의 Day를 기준으로 Group By를 시키고 각 군집의 통계값을 계산합니다.
```roomsql
SELECT MIN(c.createdAt) as createdAt, MIN(c.cpuUsage) as minUsage, 
    MAX(c.cpuUsage) as maxUsage, AVG(c.cpuUsage) as avgUsage 
FROM CpuUsage c 
WHERE :dateFrom <= c.createdAt AND c.createdAt < :dateTo 
GROUP BY DAY(c.createdAt) 
ORDER BY MIN(c.createdAt)
``` 
### 잘못된 파라미터에 대한 예외 처리 전략
> 최대한 예외 처리 로직과 비즈니스 로직을 분리하기 위해 GlobalExceptionHandler 라는 ControllerAdvice를 이용해서 예외 처리를 수행했습니다.
```java
// src/main/java/me/iksadnorth/monitoring_server/service/CpuUsageService.java
public HourCpuUsageResponse readHourCpuUsage(LocalDateTime date) {
    validateParameterOfHourCpuUsage(date);
    ...
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
```
```java
// src/main/java/me/iksadnorth/monitoring_server/exception/GlobalExceptionHandler.java
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MonitoringServerException.class)
    public ResponseEntity<ErrorResponse> handleMonitoringServerException(MonitoringServerException e) {
        String message = e.getMessage();

        log.warn(message);

        ErrorResponse errorResponse = new ErrorResponse(message);
        return ResponseEntity.status(e.getHttpStatus()).body(errorResponse);
    }
}

```
### 데이터베이스 계층 유닛 테스트를 위한 테스트케이스
> Repository Layer 단위 테스트를 수행하기 전, 테스트에 사용될 데이터를 어찌 적재하고 정리할지 고민했습니다.
> 결국, 아래와 같이 데이터 저장 SQL 파일과 데이터 정리 SQL 파일을 테스트 전후로 실행되도록 구성했습니다.
```java
// src/test/java/me/iksadnorth/monitoring_server/repository/CpuUsageRepositoryTest.java
@SqlGroup({
        @Sql({"classpath:data/testcase-for-minute-api.sql"}),
        @Sql(scripts = "classpath:data/clean-up.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
@Test
void findByCreatedAtBetween() {
    ...
}
```