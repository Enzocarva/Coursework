CREATE TABLE users (
    id INTEGER,
    full_name TEXT NOT NULL,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE schools (
    id INTEGER,
    name TEXT NOT NULL,
    type TEXT NOT NULL,
    location TEXT NOT NULL,
    year_founded INTEGER,
    PRIMARY KEY(id)
);

CREATE TABLE companies (
    id INTEGER,
    name TEXT NOT NULL,
    industry TEXT NOT NULL,
    location TEXT NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE user_connections (
    id INTEGER,
    user_id1 INTEGER NOT NULL,
    user_id2 INTEGER NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(user_id1) REFERENCES users(id),
    FOREIGN KEY(user_id2) REFERENCES users(id)
);

CREATE TABLE school_connections (
    id INTEGER,
    school_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    start_date NUMERIC,
    end_date NUMERIC,
    degree_type TEXT,
    PRIMARY KEY(id),
    FOREIGN KEY(user_id) REFERENCES users(id),
    FOREIGN KEY(school_id) REFERENCES schools(id)
);

CREATE TABLE company_connections (
    id INTEGER,
    company_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    start_date NUMERIC,
    end_date NUMERIC,
    title TEXT,
    PRIMARY KEY(id),
    FOREIGN KEY(user_id) REFERENCES users(id),
    FOREIGN KEY(company_id) REFERENCES companies(id)
);
