package com.sosyalmedia.notificationservice.mapper;

import com.sosyalmedia.notificationservice.client.dto.CustomerBasicDTO;
import com.sosyalmedia.notificationservice.dto.request.PostDeadlineCreateRequest;
import com.sosyalmedia.notificationservice.dto.response.PostDeadlineResponse;
import com.sosyalmedia.notificationservice.entity.PostDeadline;

public interface PostDeadlineMapper {

    PostDeadline toEntity(PostDeadlineCreateRequest request);

    PostDeadlineResponse toResponse(PostDeadline entity);

    PostDeadlineResponse toResponse(PostDeadline entity, CustomerBasicDTO customer);
}