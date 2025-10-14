-- Insert sample data for batch processing
INSERT INTO batch_data (input_data, status, created_at) VALUES 
('sample data 1', 'PENDING', CURRENT_TIMESTAMP()),
('sample data 2', 'PENDING', CURRENT_TIMESTAMP()),
('sample data 3', 'PENDING', CURRENT_TIMESTAMP()),
('sample data 4', 'PENDING', CURRENT_TIMESTAMP()),
('sample data 5', 'PENDING', CURRENT_TIMESTAMP()),
('123.45', 'PENDING', CURRENT_TIMESTAMP()),
('678.90', 'PENDING', CURRENT_TIMESTAMP()),
('', 'PENDING', CURRENT_TIMESTAMP()),
('test validation', 'PENDING', CURRENT_TIMESTAMP()),
('another test', 'PENDING', CURRENT_TIMESTAMP());