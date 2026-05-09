CREATE TABLE IF NOT EXISTS availability_slots (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    provider_id INTEGER NOT NULL,
    subject     TEXT    NOT NULL DEFAULT '',
    start_time  TEXT    NOT NULL,
    end_time    TEXT    NOT NULL,
    status      TEXT    NOT NULL DEFAULT 'OPEN'
                        CHECK (status IN ('OPEN', 'BOOKED', 'CANCELLED'))
);

CREATE TABLE IF NOT EXISTS appointments (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    slot_id     INTEGER NOT NULL UNIQUE,
    student_id  INTEGER NOT NULL,
    provider_id INTEGER NOT NULL,
    service_id  INTEGER NOT NULL,
    subject     TEXT    NOT NULL DEFAULT '',
    created_at  TEXT    NOT NULL DEFAULT (datetime('now')),
    FOREIGN KEY (slot_id) REFERENCES availability_slots(id)
);
