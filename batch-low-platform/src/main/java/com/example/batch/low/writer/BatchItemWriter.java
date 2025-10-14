package com.example.batch.low.writer;

import com.example.batch.low.model.BatchData;
import com.example.batch.low.model.BatchResult;
import com.example.batch.low.repository.BatchDataRepository;
import com.example.batch.low.repository.BatchResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BatchItemWriter implements ItemWriter<BatchResult> {

    private static final Logger logger = LoggerFactory.getLogger(BatchItemWriter.class);
    
    @Autowired
    private BatchResultRepository batchResultRepository;
    
    @Autowired
    private BatchDataRepository batchDataRepository;
    
    private JobParameters jobParameters;
    
    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobParameters = stepExecution.getJobParameters();
    }
    
    @Override
    @Transactional
    public void write(Chunk<? extends BatchResult> results) throws Exception {
        logger.debug("Writing {} results", results.size());
        
        boolean updateSource = jobParameters.getString("updateSource", "true").equalsIgnoreCase("true");
        
        for (BatchResult result : results) {
            // Save the result
            batchResultRepository.save(result);
            
            // Update the source data status if configured
            if (updateSource) {
                BatchData sourceData = batchDataRepository.findById(result.getDataId()).orElse(null);
                if (sourceData != null) {
                    sourceData.setStatus("PROCESSED");
                    batchDataRepository.save(sourceData);
                }
            }
        }
        
        logger.debug("Finished writing results");
    }
}