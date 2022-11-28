DROP TABLE IF EXISTS endpoint_hits CASCADE;

CREATE TABLE IF NOT EXISTS endpoint_hits
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    app varchar(255) NOT NULL,
    uri varchar(1000) NOT NULL,
    ip varchar(255) NOT NULL,
    timestamp timestamp without time zone
);