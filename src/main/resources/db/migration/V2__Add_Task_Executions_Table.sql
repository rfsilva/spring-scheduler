-- Create task_executions table to store execution history
CREATE TABLE task_executions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    scheduled_task_id BIGINT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME,
    status VARCHAR(50) NOT NULL,
    execution_message TEXT,
    execution_details TEXT,
    retry_count INT DEFAULT 0,
    duration_ms BIGINT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    FOREIGN KEY (scheduled_task_id) REFERENCES scheduled_tasks(id) ON DELETE CASCADE
);

-- Add indexes for better query performance
CREATE INDEX idx_task_executions_task_id ON task_executions(scheduled_task_id);
CREATE INDEX idx_task_executions_status ON task_executions(status);
CREATE INDEX idx_task_executions_start_time ON task_executions(start_time);