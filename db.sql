-- ============================================================
-- ETHIOPIAN MUSEUM MANAGEMENT SYSTEM - COMPLETE DATABASE
-- ============================================================
-- This script creates the entire database from scratch.
-- Run this in your MySQL client (Workbench, command line, etc.)
-- ============================================================

-- Drop existing database if it exists (WARNING: This deletes all data!)
DROP DATABASE IF EXISTS museum_db;

-- Create fresh database
CREATE DATABASE museum_db;
USE museum_db;

-- ============================================================
-- TABLE 1: Cities
-- ============================================================
CREATE TABLE cities (
    city_id INT PRIMARY KEY AUTO_INCREMENT,
    city_name VARCHAR(50) UNIQUE NOT NULL
);

-- ============================================================
-- TABLE 2: Museums (NO student_price)
-- ============================================================
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

-- ============================================================
-- TABLE 3: Users (for Admin Login)
-- ============================================================
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'ADMIN'
);

-- ============================================================
-- TABLE 4: Transactions (for Ticket Purchases - Optional)
-- ============================================================
CREATE TABLE transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    museum_id INT NOT NULL,
    visitor_name VARCHAR(100) NOT NULL,
    visitor_email VARCHAR(100) NOT NULL,
    visitor_phone VARCHAR(20) NOT NULL,
    visitor_type VARCHAR(20) NOT NULL,
    quantity INT NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    purchase_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (museum_id) REFERENCES museums(museum_id) ON DELETE CASCADE
);

-- ============================================================
-- INSERT DATA: Cities
-- ============================================================
INSERT INTO cities (city_name) VALUES 
('Addis Ababa'),
('Lalibela'),
('Axum'),
('Gondar'),
('Bahir Dar'),
('Mekele'),
('Jimma'),
('Konso'),
('Wollega');

-- ============================================================
-- INSERT DATA: Admin User
-- ============================================================
INSERT INTO users (username, password, role) VALUES 
('admin', 'admin123', 'ADMIN');

-- ============================================================
-- INSERT DATA: All 22 Museums
-- ============================================================

-- Addis Ababa Museums (city_id = 1)
INSERT INTO museums (name, description, city_id, opening_time, closing_time, local_price, foreign_price, cultural_info, image_path) VALUES
('National Museum of Ethiopia', 'Home to the famous fossil "Lucy". It houses archaeological, paleontological, and historical artifacts from Ethiopia''s rich history.', 1, '09:00:00', '17:00:00', 30, 150, 'Founded in 1956, the museum hosts exhibits from the Axumite empire and contains the iconic Lucy skeleton, one of the oldest hominid fossils ever discovered.', 'resources/images/national_museum.jpg'),

('Ethnological Museum', 'Located on Addis Ababa University main campus, inside the former Guenete Leul Palace of Emperor Haile Selassie.', 1, '09:00:00', '16:30:00', 20, 100, 'Exhibits a wide range of ethnographic objects, traditional costumes, musical instruments, and religious art from various Ethiopian cultures.', 'resources/images/ethnological.jpg'),

('Red Terror Martyrs Memorial Museum', 'A museum dedicated to the victims of the Red Terror (1974-1991) under the Derg regime.', 1, '08:30:00', '18:00:00', 25, 120, 'It serves as a memorial and educational center about Ethiopia''s political history, documenting the atrocities committed during the Derg period.', 'resources/images/red_terror.jpg'),

('Addis Ababa Museum', 'Housed in the former palace of Ras Biru, this museum showcases the history of Addis Ababa.', 1, '09:00:00', '17:00:00', 20, 100, 'The palace was built at the turn of the 19th century and has exhibits on the city''s founding, development, and cultural evolution.', 'resources/images/addis_museum.jpg'),

('Holy Trinity Cathedral Museum', 'Located within the grounds of the Holy Trinity Cathedral, this museum displays religious artifacts.', 1, '09:00:00', '17:00:00', 20, 100, 'The cathedral is the final resting place of many Ethiopian emperors and patriots, including Emperor Haile Selassie.', 'resources/images/holy_trinity.jpg'),

('St. George''s Cathedral Museum', 'Within the cathedral grounds, it holds many religious treasures and historical items.', 1, '09:00:00', '17:00:00', 20, 100, 'St. George''s Cathedral is an important landmark in Addis Ababa, built to commemorate Ethiopia''s victory over Italian forces.', 'resources/images/st_george.jpg'),

('Ethiopian Postal Museum', 'Displays the history of postal services in Ethiopia, including old stamps and postal equipment.', 1, '09:00:00', '16:00:00', 15, 80, 'The museum offers a unique perspective on Ethiopia''s communication history, with rare stamps and postal artifacts.', 'resources/images/postal_museum.jpg'),

('Zoma Museum', 'A contemporary art and cultural centre that also focuses on environmental sustainability.', 1, '10:00:00', '18:00:00', 30, 150, 'Zoma is known for its beautiful green architecture and modern art exhibitions, promoting sustainable living.', 'resources/images/zoma_museum.jpg'),

('Zoological Natural History Museum', 'Displays Ethiopia''s fauna, including taxidermy specimens of endemic animals.', 1, '09:00:00', '17:00:00', 15, 80, 'Part of the Ministry of Culture and Tourism, it educates on Ethiopia''s biodiversity and conservation efforts.', 'resources/images/zoological.jpg'),

('Oromo Museum', 'Focuses on the culture, history, and art of the Oromo people.', 1, '09:00:00', '17:00:00', 20, 100, 'Includes ethnography, natural history, and archaeological artifacts celebrating Oromo heritage.', 'resources/images/oromo_museum.jpg'),

('Rimbaud Museum (Casa Museo di Rimbaud)', 'A small museum dedicated to the French poet Arthur Rimbaud, who lived in Ethiopia.', 1, '09:00:00', '17:00:00', 20, 100, 'The museum displays letters, photographs, and personal items of Rimbaud during his time in Ethiopia.', 'resources/images/rimbaud.jpg'),

('Pan African Museum', 'Celebrates African unity, Pan-Africanism, and Ethiopian history.', 1, '09:00:00', '17:00:00', 20, 100, 'Located in the heart of Addis Ababa, it highlights Ethiopia''s role in Africa''s liberation movements.', 'resources/images/pan_african.jpg'),

('Mary Museum', 'Known for its wall paintings by artist Aleka Mezgebu.', 1, '09:00:00', '17:00:00', 15, 80, 'The museum features beautiful religious and cultural paintings by renowned Ethiopian artists.', 'resources/images/mary_museum.jpg'),

-- Lalibela Museums (city_id = 2)
('Lalibela Cultural Center', 'Provides an overview of the history and significance of Lalibela''s rock-hewn churches.', 2, '08:30:00', '18:00:00', 15, 80, 'The center is an excellent introduction before visiting the churches, explaining their construction and religious importance.', 'resources/images/lalibela_center.jpg'),

('Rock-Hewn Churches Museum', 'Located within the UNESCO World Heritage site, displaying artifacts from the churches.', 2, '08:30:00', '18:00:00', 15, 80, 'Exhibits include ancient manuscripts, crosses, and liturgical items from the 11 monolithic churches.', 'resources/images/lalibela_churches.jpg'),

-- Axum Museums (city_id = 3)
('Archaeological Museum of Axum', 'Houses stelae, coins, pottery, and inscriptions from the ancient Axumite kingdom.', 3, '09:00:00', '17:30:00', 15, 80, 'Axum was a major trading empire; the museum displays artifacts spanning over 2,000 years of history.', 'resources/images/axum_museum.jpg'),

-- Gondar Museums (city_id = 4)
('Gondar Castle Complex Museums', 'Located in the Royal Enclosure, this museum exhibits items from the Gondarine period.', 4, '09:00:00', '17:00:00', 15, 80, 'Includes royal crowns, thrones, and relics from the emperors who ruled from Gondar.', 'resources/images/gondar_castle.jpg'),

-- Bahir Dar Museums (city_id = 5)
('Bahir Dar Blue Nile Cultural Center', 'Showcases the culture of the Amhara region and the Blue Nile Falls.', 5, '09:00:00', '17:00:00', 15, 80, 'The center offers insights into local traditions, crafts, and the history of the Blue Nile.', 'resources/images/bahir_dar.jpg'),

-- Mekele Museums (city_id = 6)
('Museum Yohannes IV', 'Dedicated to Emperor Yohannes IV, featuring his personal belongings and historical items.', 6, '09:00:00', '17:00:00', 15, 80, 'The museum is located in Mekele, the capital of the Tigray region, honoring one of Ethiopia''s greatest emperors.', 'resources/images/yohannes_museum.jpg'),

-- Jimma Museums (city_id = 7)
('Jimma Museum', 'Displays artifacts of the Oromo culture and history of the Jimma region.', 7, '09:00:00', '17:00:00', 15, 80, 'It includes traditional clothing, household items, and historical photographs from the Jimma kingdom.', 'resources/images/jimma_museum.jpg'),

-- Konso Museums (city_id = 8)
('Waga of Konso Museum', 'Known for the wooden totem poles (Waga) that honor the Konso heroes.', 8, '09:00:00', '17:00:00', 15, 80, 'The museum is in Dokato and explains the unique Konso culture, which is a UNESCO intangible heritage.', 'resources/images/konso_museum.jpg'),

-- Wollega Museums (city_id = 9)
('Wollega Museum', 'Showcases the history and culture of the Wollega Oromo people.', 9, '09:00:00', '17:00:00', 15, 80, 'Features traditional artifacts, musical instruments, and historical documents from the Wollega region.', 'resources/images/wollega_museum.jpg');

-- ============================================================
-- VERIFICATION QUERIES (Run these to check your data)
-- ============================================================

-- Check how many cities
SELECT 'Cities Count:' as 'Check', COUNT(*) as 'Count' FROM cities;

-- Check how many museums
SELECT 'Museums Count:' as 'Check', COUNT(*) as 'Count' FROM museums;

-- Check museums by city
SELECT 'Museums by City:' as 'Check', c.city_name, COUNT(m.museum_id) as 'Count'
FROM cities c
LEFT JOIN museums m ON c.city_id = m.city_id
GROUP BY c.city_name
ORDER BY Count DESC;

-- Check admin user
SELECT 'Admin User:' as 'Check', username, role FROM users;

-- ============================================================
-- DONE! Your database is ready.
-- ============================================================