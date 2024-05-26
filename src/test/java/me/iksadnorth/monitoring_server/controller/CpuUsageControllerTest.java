package me.iksadnorth.monitoring_server.controller;

import me.iksadnorth.monitoring_server.config.DateTimeFormatConfig;
import me.iksadnorth.monitoring_server.fixture.DateTimeFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class CpuUsageControllerTest {
    @Autowired
    private MockMvc mvc;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeFormatConfig.FORMAT);

    @SqlGroup({
            @Sql({"classpath:data/testcase-for-minute-api.sql"}),
            @Sql(
                    scripts = "classpath:data/clean-up.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
            )
    })
    @Test
    void readMinuteCpuUsage_runNormally() throws Exception {
        // given
        LocalDateTime dateFrom = DateTimeFixture.getDateFromBeforeBoundaryWhenCallMinuteReadApi();
        LocalDateTime dateTo = DateTimeFixture.getDateToBeforeBoundaryWhenCallMinuteReadApi();

        // when
        ResultActions resultActions = mvc.perform(
                get("/api/v1/cpu-usages/minute")
                        .param("dateFrom", dateFrom.format(formatter))
                        .param("dateTo", dateTo.format(formatter))
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk());
    }

    @SqlGroup({
            @Sql({"classpath:data/testcase-for-minute-api.sql"}),
            @Sql(
                    scripts = "classpath:data/clean-up.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
            )
    })
    @Test
    void readMinuteCpuUsage_throwErrorWhenGivenOutOfRangeDateTime() throws Exception {
        // given
        LocalDateTime dateFrom = DateTimeFixture.getDateFromBoundaryWhenCallMinuteReadApi();
        LocalDateTime dateTo = DateTimeFixture.getDateToBoundaryWhenCallMinuteReadApi();

        // when
        ResultActions resultActions = mvc.perform(
                        get("/api/v1/cpu-usages/minute")
                                .param("dateFrom", dateFrom.format(formatter))
                                .param("dateTo", dateTo.format(formatter))
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest());
    }

    @SqlGroup({
            @Sql({"classpath:data/testcase-for-hour-api.sql"}),
            @Sql(
                    scripts = "classpath:data/clean-up.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
            )
    })
    @Test
    void readHourCpuUsage_runNormally() throws Exception {
        // given
        LocalDateTime date = DateTimeFixture.getDateBeforeBoundaryWhenCallHourReadApi();

        // when
        ResultActions resultActions = mvc.perform(
                        get("/api/v1/cpu-usages/hour")
                                .param("date", date.format(formatter))
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk());
    }

    @SqlGroup({
            @Sql({"classpath:data/testcase-for-hour-api.sql"}),
            @Sql(
                    scripts = "classpath:data/clean-up.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
            )
    })
    @Test
    void readHourCpuUsage_throwErrorWhenGivenOutOfRangeDateTime() throws Exception {
        // given
        LocalDateTime date = DateTimeFixture.getDateBoundaryWhenCallHourReadApi();

        // when
        ResultActions resultActions = mvc.perform(
                        get("/api/v1/cpu-usages/hour")
                                .param("date", date.format(formatter))
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest());
    }

    @SqlGroup({
            @Sql({"classpath:data/testcase-for-day-api.sql"}),
            @Sql(
                    scripts = "classpath:data/clean-up.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
            )
    })
    @Test
    void readDayCpuUsage_runNormally() throws Exception {
        // given
        LocalDateTime dateFrom = DateTimeFixture.getDateFromBeforeBoundaryWhenCallDayReadApi();
        LocalDateTime dateTo = DateTimeFixture.getDateToBeforeBoundaryWhenCallDayReadApi();

        // when
        ResultActions resultActions = mvc.perform(
                        get("/api/v1/cpu-usages/day")
                                .param("dateFrom", dateFrom.format(formatter))
                                .param("dateTo", dateTo.format(formatter))
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isOk());
    }

    @SqlGroup({
            @Sql({"classpath:data/testcase-for-day-api.sql"}),
            @Sql(
                    scripts = "classpath:data/clean-up.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
            )
    })
    @Test
    void readDayCpuUsage_throwErrorWhenGivenOutOfRangeDateTime() throws Exception {
        // given
        LocalDateTime dateFrom = DateTimeFixture.getDateFromBoundaryWhenCallDayReadApi();
        LocalDateTime dateTo = DateTimeFixture.getDateToBoundaryWhenCallDayReadApi();

        // when
        ResultActions resultActions = mvc.perform(
                        get("/api/v1/cpu-usages/day")
                                .param("dateFrom", dateFrom.format(formatter))
                                .param("dateTo", dateTo.format(formatter))
                )
                .andDo(print());

        // then
        resultActions
                .andExpect(status().isBadRequest());
    }
}