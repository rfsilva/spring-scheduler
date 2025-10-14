package com.example.batch.low.repository;

import com.example.batch.low.model.BatchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchResultRepository extends JpaRepository<BatchResult, Long> {
}