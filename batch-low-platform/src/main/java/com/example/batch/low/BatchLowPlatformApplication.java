package com.example.batch.low;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Date;
import java.util.Properties;

@SpringBootApplication
public class BatchLowPlatformApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(BatchLowPlatformApplication.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private ApplicationContext context;

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(BatchLowPlatformApplication.class, args)));
    }

    @Override
    public void run(String... args) throws Exception {
        // Define command line options
        Options options = new Options();
        options.addOption(Option.builder("j")
                .longOpt("job")
                .desc("Job name to execute")
                .hasArg()
                .required()
                .build());
        
        options.addOption(Option.builder("p")
                .longOpt("param")
                .desc("Job parameters in format key=value (can be used multiple times)")
                .hasArg()
                .numberOfArgs(2)
                .valueSeparator('=')
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
                formatter.printHelp("batch-low-platform", options);
                return;
            }

            String jobName = cmd.getOptionValue("job");
            logger.info("Starting job: {}", jobName);

            // Get the job bean by name
            Job job = context.getBean(jobName, Job.class);

            // Build job parameters
            JobParametersBuilder parametersBuilder = new JobParametersBuilder();
            parametersBuilder.addLong("time", System.currentTimeMillis());
            
            // Add custom parameters if provided
            if (cmd.hasOption("param")) {
                Properties props = cmd.getOptionProperties("param");
                props.forEach((key, value) -> {
                    String paramKey = key.toString();
                    String paramValue = value.toString();
                    
                    // Try to determine parameter type
                    try {
                        Long longValue = Long.parseLong(paramValue);
                        parametersBuilder.addLong(paramKey, longValue);
                    } catch (NumberFormatException e1) {
                        try {
                            Double doubleValue = Double.parseDouble(paramValue);
                            parametersBuilder.addDouble(paramKey, doubleValue);
                        } catch (NumberFormatException e2) {
                            if ("true".equalsIgnoreCase(paramValue) || "false".equalsIgnoreCase(paramValue)) {
                                parametersBuilder.addString(paramKey, paramValue);
                            } else if (paramValue.matches("\\d{4}-\\d{2}-\\d{2}.*")) {
                                try {
                                    Date dateValue = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(paramValue);
                                    parametersBuilder.addDate(paramKey, dateValue);
                                } catch (Exception e3) {
                                    parametersBuilder.addString(paramKey, paramValue);
                                }
                            } else {
                                parametersBuilder.addString(paramKey, paramValue);
                            }
                        }
                    }
                });
            }

            JobParameters jobParameters = parametersBuilder.toJobParameters();
            
            // Execute the job
            JobExecution execution = jobLauncher.run(job, jobParameters);
            
            logger.info("Job execution status: {}", execution.getStatus());
            
            // Set exit code based on job status
            if (execution.getStatus().isUnsuccessful()) {
                System.exit(1);
            }
            
        } catch (ParseException e) {
            logger.error("Error parsing command line arguments", e);
            formatter.printHelp("batch-low-platform", options);
            System.exit(1);
        } catch (Exception e) {
            logger.error("Error executing batch job", e);
            System.exit(1);
        }
    }
}