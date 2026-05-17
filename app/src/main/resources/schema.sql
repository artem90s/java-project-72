DROP TABLE IF EXISTS urls;
DROP TABLE IF EXISTS url_checks;

CREATE TABLE urls
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP
);

CREATE TABLE url_checks
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    url_id      BIGINT NOT NULL,
    status_code BIGINT,
    h1          VARCHAR,
    title       VARCHAR,
    description TEXT,
    created_at  TIMESTAMP
);