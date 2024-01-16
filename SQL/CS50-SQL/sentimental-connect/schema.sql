CREATE TABLE users (
    id INT UNSIGNED AUTO_INCREMENT,
    full_name VARCHAR(70) NOT NULL,
    username VARCHAR(70) NOT NULL UNIQUE,
    password CHAR(64) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE schools (
    id INT UNSIGNED AUTO_INCREMENT,
    name VARCHAR(70) NOT NULL,
    type ENUM('Primary', 'Secondary', 'Higher Education') NOT NULL,
    location VARCHAR(70) NOT NULL,
    year SMALLINT UNSIGNED,
    PRIMARY KEY(id)
);

CREATE TABLE companies (
    id INT UNSIGNED AUTO_INCREMENT,
    name VARCHAR(70) NOT NULL,
    industry ENUM('Technology', 'Education', 'Business') NOT NULL,
    location VARCHAR(70),
    PRIMARY KEY(id)
);

CREATE TABLE user_connections (
    id INT UNSIGNED AUTO_INCREMENT,
    user1_id INT UNSIGNED NOT NULL,
    user2_id INT UNSIGNED NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(user1_id) REFERENCES users(id),
    FOREIGN KEY(user2_id) REFERENCES users(id)
);

CREATE TABLE school_connections (
    id INT UNSIGNED AUTO_INCREMENT,
    school_id INT UNSIGNED NOT NULL,
    user_id INT UNSIGNED NOT NULL,
    start_date DATE,
    end_date DATE,
    degree_type ENUM ('BA', 'MA', 'PhD', 'BS'),
    PRIMARY KEY(id),
    FOREIGN KEY(school_id) REFERENCES schools(id),
    FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE company_connections (
    id INT UNSIGNED AUTO_INCREMENT,
    company_id INT UNSIGNED NOT NULL,
    user_id INT UNSIGNED NOT NULL,
    start_date DATE,
    end_date DATE,
    PRIMARY KEY(id),
    FOREIGN KEY(company_id) REFERENCES companies(id),
    FOREIGN KEY(user_id) REFERENCES users(id)
);
