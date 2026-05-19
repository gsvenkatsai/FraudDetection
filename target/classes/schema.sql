-- Schema for Fraud Detection System (Realistic Hand-Crafted Data)

DROP TABLE IF EXISTS Alerts CASCADE;
DROP TABLE IF EXISTS Transactions CASCADE;
DROP TABLE IF EXISTS Logins CASCADE;
DROP TABLE IF EXISTS Users CASCADE;
DROP TABLE IF EXISTS Devices CASCADE;

-- 1. Devices Table
CREATE TABLE Devices (
    device_id VARCHAR(50) PRIMARY KEY,
    device_type VARCHAR(20),
    os VARCHAR(20),
    first_seen TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Users Table
CREATE TABLE Users (
    user_id SERIAL PRIMARY KEY,
    signup_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    country VARCHAR(50),
    status VARCHAR(20),
    full_name VARCHAR(100) -- Added for realism
);

-- 3. Logins Table
CREATE TABLE Logins (
    login_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES Users(user_id),
    login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    country VARCHAR(50),
    device_id VARCHAR(50) REFERENCES Devices(device_id),
    status VARCHAR(20) -- 'success', 'failed'
);

-- 4. Transactions Table
CREATE TABLE Transactions (
    transaction_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES Users(user_id),
    amount DECIMAL(15, 2),
    transaction_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    merchant VARCHAR(100),
    status VARCHAR(20)
);

-- 5. Alerts Table
CREATE TABLE Alerts (
    alert_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES Users(user_id),
    alert_type VARCHAR(50),
    alert_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    severity VARCHAR(10)
);

-- Indexes
CREATE INDEX idx_users_signup_date ON Users(signup_date);
CREATE INDEX idx_logins_user_id ON Logins(user_id);
CREATE INDEX idx_logins_login_time ON Logins(login_time);
CREATE INDEX idx_transactions_user_id ON Transactions(user_id);
CREATE INDEX idx_transactions_time ON Transactions(transaction_time);
CREATE INDEX idx_alerts_user_id ON Alerts(user_id);
CREATE INDEX idx_alerts_time ON Alerts(alert_time);

-- --- REALISTIC DUMMY DATA ---

-- 1. Devices (10 realistic devices)
INSERT INTO Devices (device_id, device_type, os, first_seen) VALUES
('IPHONE-13-JS', 'Mobile', 'iOS', '2023-01-15 08:30:00'),
('MACBOOK-PRO-MG', 'Desktop', 'macOS', '2023-02-10 14:20:00'),
('SAMSUNG-S22-RK', 'Mobile', 'Android', '2023-03-05 09:45:00'),
('PIXEL-6-SJ', 'Mobile', 'Android', '2023-04-12 11:10:00'),
('DELL-XPS-HM', 'Desktop', 'Windows', '2023-05-20 16:30:00'),
('IPAD-AIR-YT', 'Tablet', 'iOS', '2023-06-01 10:00:00'),
('THINKPAD-AA', 'Desktop', 'Linux', '2023-07-15 13:00:00'),
('HP-LAPTOP-EP', 'Desktop', 'Windows', '2023-08-22 15:45:00'),
('ONEPLUS-9-CW', 'Mobile', 'Android', '2023-09-10 12:20:00'),
('SURFACE-PRO-CD', 'Tablet', 'Windows', '2023-10-05 17:15:00');

-- 2. Users (10 realistic users)
INSERT INTO Users (full_name, country, status, signup_date) VALUES
('John Smith', 'USA', 'active', '2023-01-01 10:00:00'), -- User 1
('Maria Garcia', 'Brazil', 'active', '2023-02-01 11:00:00'), -- User 2
('Rajesh Kumar', 'India', 'active', '2023-03-01 12:00:00'), -- User 3
('Sarah Jenkins', 'UK', 'active', '2023-04-01 13:00:00'), -- User 4
('Hans Müller', 'Germany', 'active', '2023-05-01 14:00:00'), -- User 5
('Yuki Tanaka', 'Japan', 'active', '2023-06-01 15:00:00'),
('Ahmed Al-Farsi', 'UAE', 'active', '2023-07-01 16:00:00'),
('Elena Petrova', 'Russia', 'active', '2023-08-01 17:00:00'),
('Chen Wei', 'China', 'active', '2023-09-01 18:00:00'),
('Chloe Dubois', 'France', 'active', '2023-10-01 19:00:00');

-- 3. Logins (15 realistic logins)
INSERT INTO Logins (user_id, login_time, ip_address, country, device_id, status) VALUES
(1, NOW() - INTERVAL '1 hour', '192.168.1.10', 'USA', 'IPHONE-13-JS', 'success'),
(2, NOW() - INTERVAL '2 hours', '177.126.34.5', 'Brazil', 'MACBOOK-PRO-MG', 'success'),
-- Rule 3: Impossible Travel (User 3 - India then UK in 30 mins)
(3, CURRENT_TIMESTAMP - INTERVAL '40 minutes', '103.21.159.1', 'India', 'SAMSUNG-S22-RK', 'success'),
(3, CURRENT_TIMESTAMP - INTERVAL '10 minutes', '62.253.221.4', 'UK', 'SAMSUNG-S22-RK', 'success'),
-- Rule 4: Failed Login Cluster (User 4 - 4 failures in 3 mins)
(4, NOW() - INTERVAL '5 minutes', '81.149.12.3', 'UK', 'PIXEL-6-SJ', 'failed'),
(4, NOW() - INTERVAL '4 minutes', '81.149.12.3', 'UK', 'PIXEL-6-SJ', 'failed'),
(4, NOW() - INTERVAL '3 minutes', '81.149.12.3', 'UK', 'PIXEL-6-SJ', 'failed'),
(4, NOW() - INTERVAL '2 minutes', '81.149.12.3', 'UK', 'PIXEL-6-SJ', 'failed'),
(4, NOW() - INTERVAL '1 minute', '81.149.12.3', 'UK', 'PIXEL-6-SJ', 'success'), -- Finally succeeded
(5, NOW() - INTERVAL '3 hours', '93.184.216.34', 'Germany', 'DELL-XPS-HM', 'success'),
(6, NOW() - INTERVAL '4 hours', '210.140.134.1', 'Japan', 'IPAD-AIR-YT', 'success'),
(7, NOW() - INTERVAL '5 hours', '94.200.10.5', 'UAE', 'THINKPAD-AA', 'success'),
(8, NOW() - INTERVAL '6 hours', '95.161.22.1', 'Russia', 'HP-LAPTOP-EP', 'success'),
(9, NOW() - INTERVAL '7 hours', '101.226.103.12', 'China', 'ONEPLUS-9-CW', 'success'),
(10, NOW() - INTERVAL '8 hours', '176.31.224.1', 'France', 'SURFACE-PRO-CD', 'success');

-- 4. Transactions (20 realistic transactions)
INSERT INTO Transactions (user_id, amount, transaction_time, merchant, status) VALUES
-- Rule 1: High Frequency (User 1 - 5 txns in 10 mins to match code txn_count >= 5)
(1, 12.50, NOW() - INTERVAL '9 minutes', 'Starbucks', 'completed'),
(1, 45.00, NOW() - INTERVAL '7 minutes', 'Amazon', 'completed'),
(1, 15.99, NOW() - INTERVAL '5 minutes', 'Netflix', 'completed'),
(1, 8.50, NOW() - INTERVAL '3 minutes', 'App Store', 'completed'),
(1, 120.00, NOW() - INTERVAL '1 minute', 'Best Buy', 'completed'),
-- Rule 2: Night Activity (User 2 - 2AM transaction)
(2, 85.00, CURRENT_DATE + TIME '02:15:00', 'Uber', 'completed'),
(2, 250.00, NOW() - INTERVAL '1 day', 'Walmart', 'completed'),
(3, 45.00, NOW() - INTERVAL '2 days', 'Zomato', 'completed'),
(4, 15.00, NOW() - INTERVAL '1 hour', 'Spotify', 'completed'),
(5, 1200.00, NOW() - INTERVAL '3 days', 'Apple Store', 'completed'),
(6, 60.00, NOW() - INTERVAL '4 days', 'Rakuten', 'completed'),
(7, 300.00, NOW() - INTERVAL '5 days', 'Etihad Airways', 'completed'),
(8, 22.50, NOW() - INTERVAL '6 days', 'Yandex Market', 'completed'),
(9, 10.00, NOW() - INTERVAL '7 days', 'Taobao', 'completed'),
(10, 55.00, NOW() - INTERVAL '8 days', 'Carrefour', 'completed'),
(1, 200.00, NOW() - INTERVAL '10 days', 'Target', 'completed'),
(3, 150.00, NOW() - INTERVAL '11 days', 'Amazon India', 'completed'),
(5, 35.00, NOW() - INTERVAL '12 days', 'Lidl', 'completed'),
(2, 12.00, NOW() - INTERVAL '13 days', 'iFood', 'completed'),
(4, 75.00, NOW() - INTERVAL '14 days', 'Tesco', 'completed');

-- Alerts (0 hardcoded alerts)
-- Alerts will be generated by the FraudEngine.
-- SPENDING_SPIKE: User 1 avg is ~$50, spike to $500
INSERT INTO Transactions (user_id, amount, transaction_time, merchant, status)
VALUES (1, 500.00, NOW(), 'Electronics Store', 'completed');

-- NEW_DEVICE: Device seen just now
INSERT INTO Devices (device_id, device_type, os, first_seen)
VALUES ('DEV_NEW_99', 'Mobile', 'iOS', NOW());

INSERT INTO Logins (user_id, login_time, ip_address, country, device_id, status)
VALUES (1, NOW(), '9.9.9.9', 'USA', 'DEV_NEW_99', 'success');

INSERT INTO Transactions (user_id, amount, transaction_time, merchant, status)
VALUES (1, 100.00, NOW(), 'Apple Store', 'completed');