package com.sosyalmedia.notificationservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoScheduleResponse {

    private Long customerId;
    private String companyName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate firstPostDate;

    private Integer regularPostsCreated;
    private Integer holidayPostsCreated;
    private Integer totalDeadlinesCreated;

    private Integer postFrequency;  //   Integer (1-7)
    private String postFrequencyDescription;  //  Açıklama
    private List<String> platforms;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate earliestDeadline;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate latestDeadline;

    private List<DeadlineSummaryDTO> sampleDeadlines;

    private String message;
}