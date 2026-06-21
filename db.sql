-- Create database
CREATE DATABASE IF NOT EXISTS museum_db;
USE museum_db;

-- Cities table
CREATE TABLE cities (
    city_id INT PRIMARY KEY AUTO_INCREMENT,
    city_name VARCHAR(50) UNIQUE NOT NULL
);

-- Museums table (NO student_price)
CREATE TABLE museums (
    museum_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    city_id INT NOT NULL,
    opening_time TIME,
    closing_time TIME,
    local_price DECIMAL(8,2),
    foreign_price DECIMAL(8,2),
    cultural_info TEXT,
    image_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (city_id) REFERENCES cities(city_id) ON DELETE CASCADE
);

-- Users table for admin login
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'ADMIN'
);

-- Transactions table (for ticket purchases)
CREATE TABLE transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    museum_id INT NOT NULL,
    visitor_type VARCHAR(20) NOT NULL,   -- 'Local' or 'Foreign'
    quantity INT NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    purchase_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (museum_id) REFERENCES museums(museum_id) ON DELETE CASCADE
);

-- Insert cities
INSERT INTO cities (city_name) VALUES 
('Addis Ababa'), ('Lalibela'), ('Axum'), ('Gondar'), ('Bahir Dar'), ('Mekele'), ('Jimma'), ('Konso');

-- Insert a default admin (username: admin, password: admin123)
INSERT INTO users (username, password) VALUES ('admin', 'admin123');

-- Insert sample museums (without student_price)
INSERT INTO museums (name, description, city_id, opening_time, closing_time, local_price, foreign_price, cultural_info, image_path) VALUES
('National Museum of Ethiopia', 'Home to the famous fossil "Lucy".', 1, '09:00:00', '17:00:00', 30, 150, 'Founded in 1956, the museum hosts archaeological and historical artifacts.', 'resources/images/national_museum.jpg'),
('Ethnological Museum', 'Located in Addis Ababa University, former palace of Haile Selassie.', 1, '09:00:00', '16:30:00', 20, 100, 'Exhibits ethnographic objects, traditional costumes, and musical instruments.', 'resources/images/ethnological.jpg'),
('Lalibela Cultural Center', 'Showcases the history of the rock-hewn churches.', 2, '08:30:00', '18:00:00', 15, 80, 'The center provides context about the 11 monolithic churches.', 'resources/images/lalibela.jpg'),
('Archaeological Museum of Axum', 'Houses stelae, coins, and inscriptions from the Axumite kingdom.', 3, '09:00:00', '17:30:00', 15, 80, 'Axum was a major trading empire in ancient times.', 'resources/images/axum.jpg'); 