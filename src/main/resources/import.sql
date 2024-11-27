--INSERT INTO user (email, name, password, created_at, updated_at)
--VALUES
--('user1@ocrp3.com', 'user1', 'password1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
--('user2@ocrp3.com', 'user2', 'password2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
--('user3@ocrp3.com', 'user3', 'password3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
--('user4@ocrp3.com', 'user4', 'password4', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
--('user5@ocrp3.com', 'user5', 'password5', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
--
--INSERT INTO rental (name, surface, price, picture, description, owner_id, created_at, updated_at)
--VALUES
--('Cozy Apartment', 50, 1500, 'apartment.jpg', 'A cozy 2-bedroom apartment in the city center.', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
--('Luxury Villa', 250, 5000, 'villa.jpg', 'A luxury villa with a private pool and garden.', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
--('Modern Studio', 35, 1200, 'studio.jpg', 'A modern studio apartment, perfect for singles.', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
--('Country House', 180, 3500, 'country_house.jpg', 'A spacious country house with a beautiful landscape.', 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
--('Beachside Bungalow', 80, 3000, 'bungalow.jpg', 'A lovely bungalow right on the beach.', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
--('Beachside House', 90, 2000, 'beachside_house.jpg', 'A nice house right on the beach.', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
--
--INSERT INTO message (rental_id, user_id, message, created_at, updated_at)
--VALUES
--(1, 1, 'I am interested in this rental. Is it still available?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
--(2, 3, 'Can I schedule a viewing for this weekend?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
--(3, 2, 'What is the earliest move-in date?', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);