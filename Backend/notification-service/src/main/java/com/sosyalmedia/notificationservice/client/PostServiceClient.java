package com.sosyalmedia.notificationservice.client;

import com.sosyalmedia.notificationservice.client.dto.PostBasicDTO;
import com.sosyalmedia.notificationservice.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// ⚠️ DUMMY: Post service henüz yok, URL geçici
@FeignClient(name = "post-service", path = "/api/posts", url = "http://localhost:9999")
public interface PostServiceClient {

    @GetMapping("/{id}")
    ApiResponse<PostBasicDTO> getPostById(@PathVariable("id") Long postId);

    @GetMapping("/customer/{customerId}")
    ApiResponse<java.util.List<PostBasicDTO>> getPostsByCustomer(@PathVariable("customerId") Long customerId);
}