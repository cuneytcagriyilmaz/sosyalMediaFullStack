package com.sosyalmedia.notificationservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSettingsUpdateDTO {

    @NotEmpty(message = "Reminder days cannot be empty")
    private List<Integer> reminderDays; // [14, 7, 3, 1]

    private Boolean isActive;
}