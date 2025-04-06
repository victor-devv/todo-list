package com.victor_devv.todo_list.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoRequest {

    @NotBlank(message = "title is required")
    @Size(min = 1, max = 255, message = "title must be between 1 and 255 characters")
    private String title;

    @Size(max = 1000, message = "description cannot exceed 1000 characters")
    private String description;

    private String priority; // LOW, MEDIUM, HIGH, URGENT

    private String status; // PENDING, IN_PROGRESS, COMPLETED, CANCELLED

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dueDate;
}
