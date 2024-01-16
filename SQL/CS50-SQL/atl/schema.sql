CREATE TABLE passengers (
    id INTEGER,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    age INTEGER NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE airlines (
    id INTEGER,
    name TEXT NOT NULL,
    concourse TEXT NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE flights (
    id INTEGER,
    number INTEGER NOT NULL,
    airline_id INTEGER,
    from_airport TEXT NOT NULL,
    to_airport TEXT NOT NULL,
    departure NUMERIC NOT NULL,
    arrival NUMERIC NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(airline_id) REFERENCES airlines(id)

);

CREATE TABLE check_ins (
    id INTEGER,
    datetime NUMERIC NOT NULL DEFAULT CURRENT_TIMESTAMP,
    flight_id INTEGER,
    PRIMARY KEY(id),
    FOREIGN KEY(flight_id) REFERENCES flights(id)
);
