package me.iksadnorth.monitoring_server.fixture;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DateTimeFixture {
    private static LocalDateTime addDeltaTime(LocalDateTime target) {
        return target.plus(1, ChronoUnit.MINUTES);
    }


    public static LocalDateTime getDateFromBoundaryWhenCallMinuteReadApi() {
        LocalDateTime dateTo = LocalDateTime.now();
        return dateTo.minus(1, ChronoUnit.WEEKS);
    }
    public static LocalDateTime getDateToBoundaryWhenCallMinuteReadApi() {
        return LocalDateTime.now();
    }
    public static LocalDateTime getDateFromBeforeBoundaryWhenCallMinuteReadApi() {
        LocalDateTime target = getDateFromBoundaryWhenCallMinuteReadApi();
        return addDeltaTime(target);
    }
    public static LocalDateTime getDateToBeforeBoundaryWhenCallMinuteReadApi() {
        return LocalDateTime.now();
    }


    public static LocalDateTime getDateBoundaryWhenCallHourReadApi() {
        LocalDateTime date = LocalDateTime.now();
        return date.minus(3, ChronoUnit.MONTHS);
    }
    public static LocalDateTime getDateBeforeBoundaryWhenCallHourReadApi() {
        LocalDateTime target = getDateBoundaryWhenCallHourReadApi();
        return addDeltaTime(target);
    }


    public static LocalDateTime getDateFromBoundaryWhenCallDayReadApi() {
        LocalDateTime dateTo = LocalDateTime.now();
        return dateTo.minus(1, ChronoUnit.YEARS);
    }
    public static LocalDateTime getDateToBoundaryWhenCallDayReadApi() {
        return LocalDateTime.now();
    }
    public static LocalDateTime getDateFromBeforeBoundaryWhenCallDayReadApi() {
        LocalDateTime target = getDateFromBoundaryWhenCallMinuteReadApi();
        return addDeltaTime(target);
    }
    public static LocalDateTime getDateToBeforeBoundaryWhenCallDayReadApi() {
        return LocalDateTime.now();
    }
}
