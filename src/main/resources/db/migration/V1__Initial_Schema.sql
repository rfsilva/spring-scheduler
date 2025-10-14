-- Create scheduled_tasks table
CREATE TABLE scheduled_tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    task_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    cron_expression VARCHAR(100) NOT NULL,
    next_execution DATETIME,
    last_execution DATETIME,
    last_execution_status VARCHAR(50),
    last_execution_message TEXT,
    execution_count INT DEFAULT 0,
    max_retries INT,
    retry_delay_seconds INT,
    task_data TEXT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

-- Create rest_task_configs table
CREATE TABLE rest_task_configs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    scheduled_task_id BIGINT NOT NULL,
    url VARCHAR(1024) NOT NULL,
    method VARCHAR(10) NOT NULL,
    headers TEXT NOT NULL,
    body TEXT,
    timeout INT NOT NULL,
    auth_type VARCHAR(20) NOT NULL,
    retry_policy TEXT,
    token_endpoint VARCHAR(1024),
    client_id VARCHAR(255),
    client_secret VARCHAR(255),
    certificate_path VARCHAR(1024),
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    FOREIGN KEY (scheduled_task_id) REFERENCES scheduled_tasks(id) ON DELETE CASCADE
);

-- Create soap_task_configs table
CREATE TABLE soap_task_configs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    scheduled_task_id BIGINT NOT NULL,
    wsdl_url VARCHAR(1024) NOT NULL,
    operation VARCHAR(255) NOT NULL,
    namespace VARCHAR(1024) NOT NULL,
    soap_action VARCHAR(1024),
    request_xml TEXT NOT NULL,
    auth_type VARCHAR(20) NOT NULL,
    timeout INT NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    custom_headers TEXT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    FOREIGN KEY (scheduled_task_id) REFERENCES scheduled_tasks(id) ON DELETE CASCADE
);

-- Create low_platform_batch_configs table
CREATE TABLE low_platform_batch_configs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    scheduled_task_id BIGINT NOT NULL,
    job_name VARCHAR(255) NOT NULL,
    command VARCHAR(1024) NOT NULL,
    parameters TEXT,
    working_directory VARCHAR(1024) NOT NULL,
    timeout INT NOT NULL,
    run_as_user VARCHAR(255),
    on_success TEXT,
    on_failure TEXT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    FOREIGN KEY (scheduled_task_id) REFERENCES scheduled_tasks(id) ON DELETE CASCADE
);

-- Create high_platform_batch_configs table
CREATE TABLE high_platform_batch_configs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    scheduled_task_id BIGINT NOT NULL,
    endpoint_type VARCHAR(20) NOT NULL,
    transaction_id VARCHAR(255) NOT NULL,
    payload TEXT NOT NULL,
    credentials TEXT NOT NULL,
    timeout INT NOT NULL,
    channel VARCHAR(255),
    queue VARCHAR(255),
    host VARCHAR(255),
    port VARCHAR(10),
    ssl_cert_path VARCHAR(1024),
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    FOREIGN KEY (scheduled_task_id) REFERENCES scheduled_tasks(id) ON DELETE CASCADE
);

-- Create messaging_task_configs table
CREATE TABLE messaging_task_configs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    scheduled_task_id BIGINT NOT NULL,
    broker_type VARCHAR(20) NOT NULL,
    destination_name VARCHAR(255) NOT NULL,
    message_payload TEXT NOT NULL,
    connection_url VARCHAR(1024) NOT NULL,
    auth_type VARCHAR(20) NOT NULL,
    headers TEXT NOT NULL,
    delivery_mode VARCHAR(20) NOT NULL,
    retry_policy TEXT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    FOREIGN KEY (scheduled_task_id) REFERENCES scheduled_tasks(id) ON DELETE CASCADE
);
