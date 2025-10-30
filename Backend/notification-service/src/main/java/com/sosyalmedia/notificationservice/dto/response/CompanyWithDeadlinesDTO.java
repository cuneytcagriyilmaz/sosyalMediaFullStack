package com.sosyalmedia.notificationservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyWithDeadlinesDTO {

    private Long customerId;
    private String companyName;
    private String sector;
    private String customerStatus;

    // Bu firmaya ait yaklaşan deadline'lar
    private List<PostDeadlineResponse> upcomingDeadlines;

    // İstatistikler
    private Integer totalUpcomingCount;
    private Integer criticalCount;
    private Integer warningCount;
}