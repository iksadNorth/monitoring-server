package me.iksadnorth.monitoring_server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.iksadnorth.monitoring_server.dto.request.DateTimeRangeRequest;
import me.iksadnorth.monitoring_server.dto.request.DateTimeSpotRequest;
import me.iksadnorth.monitoring_server.dto.response.DayCpuUsageResponse;
import me.iksadnorth.monitoring_server.dto.response.HourCpuUsageResponse;
import me.iksadnorth.monitoring_server.dto.response.MinuteCpuUsageResponse;

@Tag(name = "CPU 사용률 데이터 조회 API", description = "주기적으로 관측된 CPU 사용률을 조회하는 API입니다.")
public interface CpuUsageControllerDocs {
    @Operation(summary = "분 단위 조회", description = "지정한 시간 구간의 분 단위 CPU 사용률을 조회합니다. 최근 1주 데이터만 제공합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "조회 시작일과 조회 종료일 모두 설정해주세요.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "조회 시작일이 조회 종료일보다 늦을 수 없습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "최근 1주 데이터 제공만 제공됩니다.", content = @Content(mediaType = "application/json"))
    })
    @Parameters({
            @Parameter(name = "dateFrom", description = "조회 시작일", example = "2024-05-20 00:00:00"),
            @Parameter(name = "dateTo", description = "조회 종료일", example = "2024-05-20 00:10:00")
    })
    MinuteCpuUsageResponse readMinuteCpuUsage(DateTimeRangeRequest request);

    @Operation(summary = "시 단위 조회", description = "지정한 날짜의 시 단위 CPU 최소/최대/평균 사용률을 조회합니다. 최근 3달 데이터만 제공합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "조회 시점이 설정되지 않았습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "최근 3달 데이터 제공만 제공됩니다.", content = @Content(mediaType = "application/json"))
    })
    @Parameters({
            @Parameter(name = "date", description = "조회 일자", example = "2024-05-20 00:00:00")
    })
    HourCpuUsageResponse readHourCpuUsage(DateTimeSpotRequest request);

    @Operation(summary = "일 단위 조회", description = "지정한 날짜 구간의 일 단위 CPU 최소/최대/평균 사용률을 조회합니다. 최근 1년 데이터만 제공합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "조회 시작일과 조회 종료일 모두 설정해주세요.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "조회 시작일이 조회 종료일보다 늦을 수 없습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "최근 1년 데이터 제공만 제공됩니다.", content = @Content(mediaType = "application/json"))
    })
    @Parameters({
            @Parameter(name = "dateFrom", description = "조회 시작일", example = "2024-05-20 00:00:00"),
            @Parameter(name = "dateTo", description = "조회 종료일", example = "2024-05-21 00:00:00")
    })
    DayCpuUsageResponse readDayCpuUsage(DateTimeRangeRequest request);
}
