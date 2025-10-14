package com.example.batch.low.processor;

import com.example.batch.low.model.BatchData;
import com.example.batch.low.model.BatchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class BatchItemProcessor implements ItemProcessor<BatchData, BatchResult> {

    private static final Logger logger = LoggerFactory.getLogger(BatchItemProcessor.class);
    
    private JobParameters jobParameters;
    
    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobParameters = stepExecution.getJobParameters();
    }
    
    @Override
    public BatchResult process(BatchData data) throws Exception {
        logger.debug("Processing item: {}", data.getId());
        
        // Apply processing logic based on job parameters
        String operation = jobParameters.getString("operation", "transform");
        
        BatchResult result = new BatchResult();
        result.setDataId(data.getId());
        
        switch (operation.toLowerCase()) {
            case "transform":
                result.setOutputData("Transformed: " + data.getInputData().toUpperCase());
                break;
            case "calculate":
                try {
                    double value = Double.parseDouble(data.getInputData());
                    double factor = jobParameters.getDouble("factor", 1.0);
                    result.setOutputData(String.valueOf(value * factor));
                } catch (NumberFormatException e) {
                    result.setOutputData("Error: Not a number");
                    result.setStatus("ERROR");
                    return result;
                }
                break;
            case "validate":
                if (data.getInputData() != null && !data.getInputData().isEmpty()) {
                    result.setOutputData("Valid: " + data.getInputData());
                } else {
                    result.setOutputData("Invalid: Empty data");
                    result.setStatus("ERROR");
                    return result;
                }
                break;
            default:
                result.setOutputData("Unknown operation: " + operation);
                break;
        }
        
        result.setStatus("PROCESSED");
        return result;
    }
}