CREATE TABLE booking (
    id SERIAL PRIMARY KEY,
    hotel_id BIGINT NOT NULL,
    start_date VARCHAR(15) NOT NULL,
    end_date VARCHAR(15) NOT NULL,
    username VARCHAR(15) NOT NULL,
    room_id BIGINT NOT NULL
);
