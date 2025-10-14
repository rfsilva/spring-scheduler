-- Insert test tasks
INSERT INTO unsecure_tasks (title, description, status, created_at, updated_at, priority)
VALUES
('Complete project documentation', 'Write comprehensive documentation for the API', 'PENDING', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 2),
('Fix security vulnerabilities', 'Address the security issues identified in the last audit', 'IN_PROGRESS', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1),
('Implement new features', 'Add the features requested by the client', 'PENDING', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 3),
('Test API endpoints', 'Perform integration tests on all endpoints', 'TODO', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 2),
('Deploy to production', 'Release the new version to production environment', 'BLOCKED', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1);