# Low Platform Batch Processing Application

This is a simple batch processing application built with Spring Boot 3, Spring Batch, and Java 21 for testing purposes.

## Features

- Spring Batch job for processing data
- H2 in-memory database with pre-loaded test data
- REST API to trigger batch jobs
- Health check endpoint

## Endpoints

### Batch Operations

- `POST /api/batch/start` - Start the batch job
- `GET /api/batch/status` - Get batch service status
- `GET /api/batch/health` - Health check endpoint

## H2 Console

The H2 console is available at `/h2-console` with the following credentials:
- JDBC URL: jdbc:h2:mem:batchdb
- Username: sa
- Password: password

## Database Schema

The application uses two tables:

1. `batch_data` - Contains the input data for batch processing
   - id: Primary key
   - name: Item name
   - value: Numeric value
   - status: Processing status (PENDING, PROCESSED)
   - created_date: Creation timestamp
   - processed_date: Processing timestamp

2. `batch_result` - Contains the results of batch processing
   - id: Primary key
   - batch_data_id: Foreign key to batch_data
   - result_value: Calculated result
   - status: Processing status
   - processed_date: Processing timestamp

## Batch Process Flow

1. The batch job reads all items with status "PENDING" from the batch_data table
2. Each item is processed (in this example, the value is multiplied by 1.5)
3. The results are written to the batch_result table
4. The original items in batch_data are updated with status "PROCESSED"