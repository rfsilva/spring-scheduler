package com.example.batch.low.config;

import com.example.batch.low.model.BatchData;
import com.example.batch.low.model.BatchResult;
import com.example.batch.low.processor.BatchItemProcessor;
import com.example.batch.low.reader.BatchItemReader;
import com.example.batch.low.writer.BatchItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Autowired
    private BatchItemReader reader;
    
    @Autowired
    private BatchItemProcessor processor;
    
    @Autowired
    private BatchItemWriter writer;
    
    @Bean
    public Step processStep() {
        return new StepBuilder("processStep", jobRepository)
                .<BatchData, BatchResult>chunk(5, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
    
    @Bean
    public Job processJob() {
        return new JobBuilder("processJob", jobRepository)
                .start(processStep())
                .build();
    }
}