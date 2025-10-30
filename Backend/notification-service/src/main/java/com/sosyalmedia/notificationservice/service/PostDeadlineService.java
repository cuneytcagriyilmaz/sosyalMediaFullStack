package com.sosyalmedia.notificationservice.service;

import com.sosyalmedia.notificationservice.dto.request.PostDeadlineCreateRequest;
import com.sosyalmedia.notificationservice.dto.request.PostDeadlineUpdateRequest;
import com.sosyalmedia.notificationservice.dto.response.CompanyWithDeadlinesDTO;
import com.sosyalmedia.notificationservice.dto.response.PostDeadlineResponse;
import com.sosyalmedia.notificationservice.dto.response.PostDeadlineStatsResponse;

import java.util.List;

public interface PostDeadlineService {

    PostDeadlineResponse createDeadline(PostDeadlineCreateRequest request);

    PostDeadlineResponse getDeadlineById(Long id);

    List<PostDeadlineResponse> getAllDeadlines();

    List<PostDeadlineResponse> getUpcomingDeadlines(int days);

    List<PostDeadlineResponse> getDeadlinesByCustomerId(Long customerId);

    PostDeadlineResponse updateDeadline(Long id, PostDeadlineUpdateRequest request);

    void deleteDeadline(Long id);

    PostDeadlineStatsResponse getStatistics();

    PostDeadlineResponse autoCreateForNewCustomer(Long customerId);
    List<CompanyWithDeadlinesDTO> getCompaniesWithUpcomingDeadlines(int days);

}