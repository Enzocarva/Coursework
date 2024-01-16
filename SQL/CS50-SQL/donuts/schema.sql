CREATE TABLE ingredients (
    id INTEGER UNIQUE NOT NULL,
    name TEXT NOT NULL,
    price_per_unit TEXT
);

CREATE TABLE donuts (
    id INTEGER UNIQUE NOT NULL,
    name TEXT NOT NULL,
    gluten_free TEXT NOT NULL DEFAULT 'NO',
    price TEXT NOT NULL,
    ingredients TEXT NOT NULL
);

CREATE TABLE orders (
    number INTEGER UNIQUE NOT NULL,
    donuts TEXT NOT NULL,
    customer_id INTEGER NOT NULL,
    FOREIGN KEY(customer_id) REFERENCES customers(id)
);

CREATE TABLE customers (
    id INTEGER,
    name TEXT NOT NULL,
    history TEXT NOT NULL,
    PRIMARY KEY(id)
);
