package com.example.batch.low.repository;

import com.example.batch.low.model.BatchData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchDataRepository extends JpaRepository<BatchData, Long> {
    
    List<BatchData> findByStatus(String status);
}