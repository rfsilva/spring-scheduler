package com.example.batch.low.reader;

import com.example.batch.low.model.BatchData;
import com.example.batch.low.repository.BatchDataRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class BatchItemReader implements ItemReader<BatchData> {

    @Autowired
    private BatchDataRepository batchDataRepository;
    
    private Iterator<BatchData> batchDataIterator;
    
    private boolean initialized = false;
    
    @Override
    public BatchData read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (!initialized) {
            List<BatchData> pendingItems = batchDataRepository.findByStatus("PENDING");
            batchDataIterator = pendingItems.iterator();
            initialized = true;
        }
        
        if (batchDataIterator != null && batchDataIterator.hasNext()) {
            return batchDataIterator.next();
        }
        
        // Reset for next job execution
        initialized = false;
        return null;
    }
}