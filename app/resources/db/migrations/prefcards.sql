CREATE TABLE prefcards (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(255),
                           operation VARCHAR(255),
                           tools VARCHAR(255),
                           duration VARCHAR(255)
);

DROP TABLE IF EXISTS prefcards;