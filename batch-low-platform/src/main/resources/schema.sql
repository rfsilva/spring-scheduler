-- Create batch_data table if not exists
CREATE TABLE IF NOT EXISTS batch_data (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    value INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_date TIMESTAMP,
    processed_date TIMESTAMP
);

-- Create batch_result table if not exists
CREATE TABLE IF NOT EXISTS batch_result (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    batch_data_id BIGINT,
    result_value DOUBLE,
    status VARCHAR(50) NOT NULL,
    processed_date TIMESTAMP,
    FOREIGN KEY (batch_data_id) REFERENCES batch_data(id)
);