package com.example.batch.low.writer;

import com.example.batch.low.model.BatchData;
import com.example.batch.low.model.BatchResult;
import com.example.batch.low.repository.BatchDataRepository;
import com.example.batch.low.repository.BatchResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class BatchItemWriter implements ItemWriter<BatchResult> {

    private static final Logger log = LoggerFactory.getLogger(BatchItemWriter.class);
    
    @Autowired
    private BatchResultRepository batchResultRepository;
    
    @Autowired
    private BatchDataRepository batchDataRepository;

    @Override
    @Transactional
    public void write(Chunk<? extends BatchResult> chunk) throws Exception {
        List<BatchResult> results = new ArrayList<>();
        List<BatchData> dataToUpdate = new ArrayList<>();
        
        for (BatchResult result : chunk) {
            log.info("Writing result: {}", result);
            results.add(result);
            
            // Update the original batch data
            BatchData batchData = batchDataRepository.findById(result.getBatchDataId())
                    .orElseThrow(() -> new RuntimeException("Batch data not found: " + result.getBatchDataId()));
            
            batchData.setStatus("PROCESSED");
            batchData.setProcessedDate(LocalDateTime.now());
            dataToUpdate.add(batchData);
        }
        
        // Save results and update batch data
        batchResultRepository.saveAll(results);
        batchDataRepository.saveAll(dataToUpdate);
        
        log.info("Saved {} results and updated {} batch data items", results.size(), dataToUpdate.size());
    }
}