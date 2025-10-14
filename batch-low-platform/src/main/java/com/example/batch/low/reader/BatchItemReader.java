package com.example.batch.low.reader;

import com.example.batch.low.model.BatchData;
import com.example.batch.low.repository.BatchDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class BatchItemReader implements ItemReader<BatchData> {

    private static final Logger logger = LoggerFactory.getLogger(BatchItemReader.class);
    
    @Autowired
    private BatchDataRepository batchDataRepository;
    
    private List<BatchData> dataItems;
    private AtomicInteger index = new AtomicInteger(0);
    private JobParameters jobParameters;
    
    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobParameters = stepExecution.getJobParameters();
        String status = jobParameters.getString("status", "PENDING");
        
        logger.info("Reading batch data with status: {}", status);
        dataItems = batchDataRepository.findByStatus(status);
        logger.info("Found {} items to process", dataItems.size());
    }
    
    @Override
    public BatchData read() {
        if (index.get() < dataItems.size()) {
            BatchData item = dataItems.get(index.getAndIncrement());
            logger.debug("Reading item: {}", item.getId());
            return item;
        }
        return null;
    }
}