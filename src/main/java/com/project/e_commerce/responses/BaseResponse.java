package com.project.e_commerce.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
public class BaseResponse {
    @CreationTimestamp
    @JsonProperty("created_at")
    private LocalDateTime created_at;

    @UpdateTimestamp
    @JsonProperty("updated_at")
    private LocalDateTime updated_at;

}
