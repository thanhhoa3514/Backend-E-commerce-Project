package com.project.e_commerce.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.MappedSuperclass;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter

@Data
@MappedSuperclass
public class BaseResponse {
    @CreationTimestamp
    @JsonProperty("created_at")
    private LocalDateTime created_at;

    @UpdateTimestamp
    @JsonProperty("updated_at")
    private LocalDateTime updated_at;

}
