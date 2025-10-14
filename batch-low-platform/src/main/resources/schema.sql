-- Schema for batch-low-platform

-- Drop tables if they exist
DROP TABLE IF EXISTS batch_results;
DROP TABLE IF EXISTS batch_data;

-- Create batch_data table
CREATE TABLE IF NOT EXISTS batch_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    input_data VARCHAR(4000),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

-- Create batch_results table
CREATE TABLE IF NOT EXISTS batch_results (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_id BIGINT NOT NULL,
    output_data VARCHAR(4000),
    status VARCHAR(50) NOT NULL,
    processed_at TIMESTAMP NOT NULL
);