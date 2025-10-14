package com.example.batch.low.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchData {
    
    @Id
    private Long id;
    
    private String name;
    
    private Integer value;
    
    private String status;
    
    private LocalDateTime createdDate;
    
    private LocalDateTime processedDate;
}