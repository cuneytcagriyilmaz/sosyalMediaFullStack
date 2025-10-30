package com.sosyalmedia.notificationservice.dto.response;


 import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
 public class PostDeadlineStatsResponse {

     private Long totalDeadlines;

     private Long upcomingWeek;

     private Long notStarted;

     private Long inProgress;

     private Long ready;

     private Long sent;

     private Long critical;

     private Long overdue;

     private Double avgDaysRemaining;
}