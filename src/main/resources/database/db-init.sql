CREATE TABLE IF NOT EXISTS events
(
    event_time TIMESTAMP NOT NULL,
    event_type TEXT      NOT NULL,
    event      TEXT      NOT NULL
);

CREATE TABLE IF NOT EXISTS inventory
(
    product  TEXT    NOT NULL,
    quantity INTEGER NOT NULL,
    unit     TEXT    NOT NULL
);

