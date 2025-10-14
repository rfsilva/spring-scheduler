package com.example.batch.low.util;

import com.example.batch.low.model.BatchData;
import com.example.batch.low.repository.BatchDataRepository;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dataloader")
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    @Autowired
    private BatchDataRepository batchDataRepository;

    @Override
    public void run(String... args) throws Exception {
        // Define command line options
        Options options = new Options();
        options.addOption(Option.builder("c")
                .longOpt("count")
                .desc("Number of records to generate")
                .hasArg()
                .required(false)
                .build());
        
        options.addOption(Option.builder("p")
                .longOpt("prefix")
                .desc("Prefix for generated data")
                .hasArg()
                .required(false)
                .build());
        
        options.addOption(Option.builder("s")
                .longOpt("status")
                .desc("Status for generated records")
                .hasArg()
                .required(false)
                .build());
        
        options.addOption(Option.builder("h")
                .longOpt("help")
                .desc("Show help")
                .build());

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("help")) {
                formatter.printHelp("dataloader", options);
                return;
            }

            int count = Integer.parseInt(cmd.getOptionValue("count", "10"));
            String prefix = cmd.getOptionValue("prefix", "test-data-");
            String status = cmd.getOptionValue("status", "PENDING");
            
            logger.info("Generating {} records with prefix '{}' and status '{}'", count, prefix, status);
            
            List<BatchData> dataList = new ArrayList<>();
            for (int i = 1; i <= count; i++) {
                BatchData data = new BatchData();
                data.setInputData(prefix + i);
                data.setStatus(status);
                data.setCreatedAt(LocalDateTime.now());
                dataList.add(data);
            }
            
            batchDataRepository.saveAll(dataList);
            logger.info("Successfully generated {} records", count);
            
        } catch (ParseException e) {
            logger.error("Error parsing command line arguments", e);
            formatter.printHelp("dataloader", options);
            System.exit(1);
        } catch (Exception e) {
            logger.error("Error generating data", e);
            System.exit(1);
        }
    }
}