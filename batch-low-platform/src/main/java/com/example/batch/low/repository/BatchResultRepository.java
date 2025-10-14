package com.example.batch.low.repository;

import com.example.batch.low.model.BatchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchResultRepository extends JpaRepository<BatchResult, Long> {
    
    List<BatchResult> findByBatchDataId(Long batchDataId);
}