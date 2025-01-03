-- Create the database
CREATE DATABASE ocr_p3;

-- Create the USERS table with id, email, name, password, and timestamps
CREATE TABLE `USERS` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `email` varchar(255),
  `name` varchar(255),
  `password` varchar(255),
  `created_at` timestamp,
  `updated_at` timestamp
);

-- Create the RENTALS table with fields for id, name, surface, price, picture, description, owner_id, and timestamps
CREATE TABLE `RENTALS` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255),
  `surface` numeric,
  `price` numeric,
  `picture` varchar(255),
  `description` varchar(2000),
  `owner_id` integer NOT NULL,
  `created_at` timestamp,
  `updated_at` timestamp
);

-- Create the MESSAGES table with fields for id, rental_id, user_id, message, and timestamps
CREATE TABLE `MESSAGES` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `rental_id` integer,
  `user_id` integer,
  `message` varchar(2000),
  `created_at` timestamp,
  `updated_at` timestamp
);

-- Ensure email in USERS table is unique
CREATE UNIQUE INDEX `USERS_index` ON `USERS` (`email`);

-- Add foreign key constraint to RENTALS table linking owner_id to USERS id
ALTER TABLE `RENTALS` ADD FOREIGN KEY (`owner_id`) REFERENCES `USERS` (`id`);

-- Add foreign key constraint to MESSAGES table linking user_id to USERS id
ALTER TABLE `MESSAGES` ADD FOREIGN KEY (`user_id`) REFERENCES `USERS` (`id`);

-- Add foreign key constraint to MESSAGES table linking rental_id to RENTALS id
ALTER TABLE `MESSAGES` ADD FOREIGN KEY (`rental_id`) REFERENCES `RENTALS` (`id`);

-- Optional : Create a user with login "user1@ocrp3.com" and password "ocrpassword1"
INSERT INTO user (email, name, password, created_at, updated_at)
VALUES
('user1@ocrp3.com', 'user1', '$2y$10$/KB5HQgJ./UC9.dPCk.3Ou2MQ7qbxfOR.sprJ0HRTkd7Um2mZiHQG', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Optional : Create some rentals entries
INSERT INTO rental (name, surface, price, picture, description, owner_id, created_at, updated_at)
VALUES
('Cozy Apartment', 50, 1500, 'apartment.jpg', 'A cozy 2-bedroom apartment in the city center.', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Luxury Villa', 250, 5000, 'villa.jpg', 'A luxury villa with a private pool and garden.', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Modern Studio', 35, 1200, 'studio.jpg', 'A modern studio apartment, perfect for singles.', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Country House', 180, 3500, 'country_house.jpg', 'A spacious country house with a beautiful landscape.', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Beachside Bungalow', 80, 3000, 'bungalow.jpg', 'A lovely bungalow right on the beach.', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Beachside House', 90, 2000, 'beachside_house.jpg', 'A nice house right on the beach.', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
