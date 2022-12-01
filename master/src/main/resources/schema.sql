DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS compilations_events CASCADE;
DROP TABLE IF EXISTS requests CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name varchar(255) NOT NULL UNIQUE,
    email varchar(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS categories
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name varchar NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS compilations
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    title varchar NOT NULL UNIQUE,
    pinned boolean NOT NULL
);

CREATE TABLE IF NOT EXISTS events
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    annotation varchar(2000) NOT NULL,
    category_id BIGINT NOT NULL,
    created_on timestamp without time zone NOT NULL,
    description varchar(7000) NOT NULL,
    event_date timestamp without time zone NOT NULL,
    user_id BIGINT NOT NULL,
    location_lat FLOAT NOT NULL,
    location_lon FLOAT NOT NULL,
    paid boolean NOT NULL,
    participant_limit int NOT NULL,
    published_on timestamp without time zone,
    request_moderation boolean NOT NULL,
    state varchar(20) NOT NULL,
    title varchar(120) NOT NULL,
    CONSTRAINT fk_events_to_categories FOREIGN KEY(category_id) REFERENCES categories(id),
    CONSTRAINT fk_events_to_users FOREIGN KEY(user_id) REFERENCES users(id)
);
CREATE TABLE IF NOT EXISTS compilations_events
(
    events_id BIGINT NOT NULL,
    compilation_id BIGINT NOT NULL,
    CONSTRAINT fk_compilations_events_to_events FOREIGN KEY(events_id) REFERENCES events(id),
    CONSTRAINT fk_compilations_events_to_compilations FOREIGN KEY(compilation_id) REFERENCES compilations(id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    created timestamp without time zone NOT NULL,
    event_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    status varchar(20) NOT NULL,
    CONSTRAINT fk_requests_to_users FOREIGN KEY(requester_id) REFERENCES users(id),
    CONSTRAINT fk_requests_to_events FOREIGN KEY(event_id) REFERENCES events(id)
);
