CREATE TABLE menstrual_records (
    id BIGSERIAL PRIMARY KEY,
    start_date DATE NOT NULL,
    end_date DATE NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_menstrual_records_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_menstrual_records_date
        CHECK ( end_date IS NULL OR end_date >= start_date )
);

CREATE INDEX idx_menstrual_records_user_start_date
    ON menstrual_records (user_id, start_date DESC)