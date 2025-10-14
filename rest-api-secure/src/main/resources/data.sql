-- Insert test users if they don't exist
INSERT INTO secure_users (username, email, password)
SELECT 'user', 'user@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a'
WHERE NOT EXISTS (SELECT 1 FROM secure_users WHERE username = 'user');

INSERT INTO secure_users (username, email, password)
SELECT 'admin', 'admin@example.com', '$2a$10$WvtSK4JQ.hOUQz1nHLj/5.21cv.D9yXpZTRRvnHPrWP12RQkRjQSG'
WHERE NOT EXISTS (SELECT 1 FROM secure_users WHERE username = 'admin');

-- Insert user roles if they don't exist
INSERT INTO secure_user_roles (user_id, role)
SELECT id, 'USER'
FROM secure_users
WHERE username = 'user'
AND NOT EXISTS (SELECT 1 FROM secure_user_roles WHERE user_id = (SELECT id FROM secure_users WHERE username = 'user') AND role = 'USER');

INSERT INTO secure_user_roles (user_id, role)
SELECT id, 'USER'
FROM secure_users
WHERE username = 'admin'
AND NOT EXISTS (SELECT 1 FROM secure_user_roles WHERE user_id = (SELECT id FROM secure_users WHERE username = 'admin') AND role = 'USER');

INSERT INTO secure_user_roles (user_id, role)
SELECT id, 'ADMIN'
FROM secure_users
WHERE username = 'admin'
AND NOT EXISTS (SELECT 1 FROM secure_user_roles WHERE user_id = (SELECT id FROM secure_users WHERE username = 'admin') AND role = 'ADMIN');

-- Insert test tasks
INSERT INTO secure_tasks (title, description, completed, created_at, updated_at)
VALUES
('Implement authentication', 'Add JWT authentication to the API', true, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Create task endpoints', 'Implement CRUD operations for tasks', true, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Add role-based authorization', 'Restrict access based on user roles', true, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Write documentation', 'Document API endpoints and authentication flow', false, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('Add unit tests', 'Write tests for controllers and services', false, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());