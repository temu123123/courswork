-- init_data_hotels.sql

-- Insert countries
INSERT INTO country (name, description) VALUES
('United States', 'A country primarily located in North America.'),
('Canada', 'A country in North America, known for its vast wilderness.'),
('Australia', 'A country and continent surrounded by the Indian and Pacific oceans.'),
('Germany', 'A country in Central Europe known for its history and culture.'),
('Japan', 'An island country in East Asia, known for its technology and tradition.');

-- Insert cities
INSERT INTO city (name, description, country_id) VALUES
('New York', 'The largest city in the United States.', 1),
('Los Angeles', 'Known for its Mediterranean climate and entertainment industry.', 1),
('Toronto', 'The largest city in Canada, known for its diversity.', 2),
('Vancouver', 'A major city in Canada known for its scenic beauty.', 2),
('Sydney', 'Famous for its Sydney Opera House and Harbour.', 3),
('Melbourne', 'Known for its arts, culture, and coffee scene.', 3),
('Berlin', 'The capital city of Germany, known for its history.', 4),
('Munich', 'Famous for its Oktoberfest and beer culture.', 4),
('Tokyo', 'The capital city of Japan, known for its modernity and tradition.', 5),
('Kyoto', 'Famous for its classical Buddhist temples and gardens.', 5);

-- Insert hotels
INSERT INTO hotel (name, city_id) VALUES
('The Ritz-Carlton', 1),
('The Four Seasons', 1),
('Waldorf Astoria', 2),
('The Beverly Hills Hotel', 2),
('Fairmont Royal York', 3),
('Shangri-La Hotel', 3),
('Sydney Harbour Marriott', 5),
('Park Hyatt Sydney', 5),
('Hotel Adlon', 7),
('Bayerischer Hof', 8);

-- Insert rooms
INSERT INTO room (hotel_id, type, price, is_available) VALUES
(1, 'Single', 100, true),
(1, 'Double', 150, true),
(1, 'Suite', 300, true),
(2, 'Single', 110, true),
(2, 'Double', 160, true),
(2, 'Suite', 310, true),
(3, 'Single', 120, true),
(3, 'Double', 170, true),
(3, 'Suite', 320, true),
(4, 'Single', 130, true),
(4, 'Double', 180, true),
(4, 'Suite', 330, true),
(5, 'Single', 140, true),
(5, 'Double', 190, true),
(5, 'Suite', 340, true),
(6, 'Single', 150, true),
(6, 'Double', 200, true),
(6, 'Suite', 350, true),
(7, 'Single', 160, true),
(7, 'Double', 210, true),
(7, 'Suite', 360, true),
(8, 'Single', 170, true),
(8, 'Double', 220, true),
(8, 'Suite', 370, true),
(9, 'Single', 180, true),
(9, 'Double', 230, true),
(9, 'Suite', 380, true),
(10, 'Single', 190, true),
(10, 'Double', 240, true),
(10, 'Suite', 390, true),
(1, 'Single', 200, true),
(2, 'Double', 250, true),
(3, 'Suite', 400, true),
(4, 'Single', 210, true),
(5, 'Double', 260, true),
(6, 'Suite', 410, true),
(7, 'Single', 220, true),
(8, 'Double', 270, true),
(9, 'Suite', 420, true),
(10, 'Single', 230, true);
