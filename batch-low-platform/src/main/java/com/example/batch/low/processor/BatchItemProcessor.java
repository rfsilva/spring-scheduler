package com.example.batch.low.processor;

import com.example.batch.low.model.BatchData;
import com.example.batch.low.model.BatchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BatchItemProcessor implements ItemProcessor<BatchData, BatchResult> {

    private static final Logger log = LoggerFactory.getLogger(BatchItemProcessor.class);

    @Override
    public BatchResult process(BatchData batchData) throws Exception {
        log.info("Processing batch data: {}", batchData);
        
        // Simulate processing
        double resultValue = batchData.getValue() * 1.5;
        
        // Create result
        BatchResult result = new BatchResult();
        result.setBatchDataId(batchData.getId());
        result.setResultValue(resultValue);
        result.setStatus("PROCESSED");
        result.setProcessedDate(LocalDateTime.now());
        
        log.info("Processed result: {}", result);
        return result;
    }
}