-- ─────────────────────────────────────────────
--  0. Extensions & ENUM types
-- ─────────────────────────────────────────────
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";      -- for uuid_generate_v4()

CREATE TYPE role_type            AS ENUM ('student','manager','admin');
CREATE TYPE payment_method_type  AS ENUM ('bkash','rocket','card');
CREATE TYPE application_status   AS ENUM ('pending','approved','rejected');
CREATE TYPE notification_target  AS ENUM ('student','manager','all');
CREATE TYPE manager_status       AS ENUM ('running','past');


CREATE TABLE student (
    id              UUID PRIMARY KEY          DEFAULT uuid_generate_v4(),
    username        VARCHAR(50)  NOT NULL     UNIQUE,
    email           VARCHAR(120) NOT NULL     UNIQUE,
    password_hash   TEXT        NOT NULL,
    std_id          VARCHAR(10) NOT NULL   UNIQUE,
    first_name      VARCHAR(50)  NOT NULL,
    last_name       VARCHAR(50),
    phone_number    VARCHAR(15),
    imageUrl        TEXT,
    present_address  TEXT,
    permanent_address TEXT,
    created_at      TIMESTAMPTZ NOT NULL      DEFAULT NOW()
);

CREATE TABLE admin (
    id              UUID PRIMARY KEY          DEFAULT uuid_generate_v4(),
    username        VARCHAR(50)  NOT NULL     UNIQUE,
    email           VARCHAR(120) NOT NULL     UNIQUE,
    password_hash   TEXT        NOT NULL,
    first_name      VARCHAR(50)  NOT NULL,
    last_name       VARCHAR(50),
    phone_number    VARCHAR(15)
);

CREATE INDEX idx_student_id ON student(std_id);

CREATE TABLE user_request (
    id              UUID PRIMARY KEY          DEFAULT uuid_generate_v4(),
    username        VARCHAR(50)  NOT NULL     UNIQUE,
    email           VARCHAR(120) NOT NULL     UNIQUE,
    password_hash   TEXT        NOT NULL,
    std_id          VARCHAR(10),
    is_verified     BOOLEAN     NOT NULL      DEFAULT FALSE,
    otp             VARCHAR(10),
    otp_expiry      TIMESTAMPTZ,
    first_name      VARCHAR(50)  NOT NULL,
    last_name       VARCHAR(50),
    phone_number    VARCHAR(15),
    imageUrl        TEXT,
    present_address  TEXT,
    permanent_address TEXT,
    requested_at      TIMESTAMPTZ NOT NULL      DEFAULT NOW()
);


CREATE TABLE mess_manager (
    id             UUID PRIMARY KEY          DEFAULT uuid_generate_v4(),
    std_id        VARCHAR(10) NOT NULL REFERENCES student(std_id) ON DELETE CASCADE,
    status         manager_status NOT NULL,
    start_date     DATE NOT NULL,
    end_date       DATE NOT NULL,
    avg_rating    NUMERIC(2,1) DEFAULT 0  
);

CREATE TABLE manager_applications (
    id            BIGSERIAL PRIMARY KEY,
    std_id  VARCHAR(10)   NOT NULL REFERENCES student(std_id) ON DELETE CASCADE,
    applied_month DATE   NOT NULL,                            -- YYYY-MM-01
    status        application_status NOT NULL DEFAULT 'pending',
    reviewed_at   TIMESTAMPTZ
);
CREATE INDEX idx_applications_status ON manager_applications(status);

CREATE TABLE meal_confirmations (
    id           BIGSERIAL PRIMARY KEY,
    std_id      VARCHAR(10)        NOT NULL REFERENCES student(std_id)       ON DELETE CASCADE,
    meal_date    DATE        NOT NULL,
    will_lunch   BOOLEAN     NOT NULL DEFAULT FALSE,
    will_dinner  BOOLEAN     NOT NULL DEFAULT FALSE,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_user_date UNIQUE (std_id, meal_date)
);

CREATE INDEX idx_meal_conf_date ON meal_confirmations (meal_date);


CREATE TABLE dues (
    id         BIGSERIAL PRIMARY KEY,
    std_id    VARCHAR(10)   NOT NULL REFERENCES student(std_id) ON DELETE CASCADE,
    last_paid_date  DATE,                     -- store first day of month (YYYY-MM-01)
    total_due  NUMERIC(10,2) NOT NULL DEFAULT 0
);


CREATE TABLE payments (
    id             BIGSERIAL PRIMARY KEY,
    std_id        VARCHAR(10)    NOT NULL REFERENCES student(std_id) ON DELETE CASCADE,
    method         payment_method_type NOT NULL,
    amount         NUMERIC(10,2)       NOT NULL CHECK (amount > 0),
    transaction_id VARCHAR(120)        NOT NULL UNIQUE,
    paid_at        TIMESTAMPTZ         NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_payments_student ON payments(std_id);


CREATE TABLE menus (
    id              BIGSERIAL PRIMARY KEY,
    menu_date       DATE        NOT NULL UNIQUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    lunch_items      JSONB       NOT NULL DEFAULT '[]',  -- array of item names
    dinner_items     JSONB       NOT NULL DEFAULT '[]'  -- array of item names
);
CREATE INDEX idx_menus_date ON menus(menu_date);


CREATE TABLE stocks (
    id            BIGSERIAL PRIMARY KEY,
    item_name     TEXT        NOT NULL UNIQUE,
    unit          TEXT        NOT NULL,                 -- kg, litre, pcs …
    quantity      NUMERIC(12,3) NOT NULL DEFAULT 0,
    per_unit_price NUMERIC(10,2) NOT NULL DEFAULT 0,
    last_updated  TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);


CREATE TABLE expenses (
    id          BIGSERIAL PRIMARY KEY,
    manager_id  UUID   NOT NULL REFERENCES mess_manager(id) ON DELETE SET NULL,
    description TEXT,
    total_amount      NUMERIC(10,2) NOT NULL CHECK (total_amount > 0),
    purchase_date      DATE   NOT NULL
);
CREATE INDEX idx_expenses_purchase_date ON expenses(purchase_date);


CREATE TABLE expense_items (
    id          BIGSERIAL PRIMARY KEY,
    expense_id  BIGINT  NOT NULL REFERENCES expenses(id) ON DELETE CASCADE,
    item_name   TEXT    NOT NULL,
    quantity    NUMERIC(12,3) NOT NULL DEFAULT 0,
    unit        TEXT    NOT NULL,                 -- kg, litre, pcs …
    per_unit_price NUMERIC(10,2) NOT NULL DEFAULT 0
);


CREATE TABLE feedback (
    id          BIGSERIAL PRIMARY KEY,
    std_id     VARCHAR(10)  NOT NULL REFERENCES student(std_id) ON DELETE CASCADE,
    meal_date   DATE  NOT NULL,
    is_lunch   BOOLEAN NOT NULL,
    is_dinner  BOOLEAN NOT NULL,
    comment     TEXT,
    anonymous   BOOLEAN NOT NULL DEFAULT FALSE,
    rating      SMALLINT CHECK (rating BETWEEN 1 AND 5),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_feedback_date ON feedback(meal_date);



CREATE TABLE sent_notifications (
    id             BIGSERIAL PRIMARY KEY,
    sender_id      UUID,
    sender_role    role_type,                     
    target_role    notification_target,
    title          TEXT   NOT NULL,
    message        TEXT   NOT NULL,
    sent_at        TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_notifications_role      ON sent_notifications(target_role);


CREATE TABLE received_notifications (
    id             BIGSERIAL PRIMARY KEY,
    receiver_id    VARCHAR(10)   NOT NULL REFERENCES student(std_id) ON DELETE CASCADE,
    notification_id BIGINT NOT NULL REFERENCES sent_notifications(id) ON DELETE CASCADE,
    is_read        BOOLEAN NOT NULL DEFAULT FALSE,
    read_at        TIMESTAMPTZ
);


CREATE TABLE ai_menu_suggestions (
    id        BIGSERIAL PRIMARY KEY,
    menu_date DATE        NOT NULL UNIQUE,
    payload   JSONB       NOT NULL,   -- full suggestion blob
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE chatbot_conversations (
    id        BIGSERIAL PRIMARY KEY,
    manager_id   UUID        NOT NULL REFERENCES mess_manager(id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    payload   JSONB       NOT NULL
);

CREATE TABLE feedback_analysis (
    id         BIGSERIAL PRIMARY KEY,
    analyzed_on DATE      NOT NULL UNIQUE,
    result     JSONB      NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);



CREATE TABLE market_expenses (
    id              BIGSERIAL PRIMARY KEY,
    stock_id        BIGINT NOT NULL REFERENCES stocks(id) ON DELETE CASCADE,
    quantity_added  NUMERIC(12,3) NOT NULL,
    total_cost      NUMERIC(12,2) NOT NULL,
    added_by        TEXT NOT NULL,              -- could be manager std_id or name
    added_at        TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE daily_expenses (
    id                 BIGSERIAL PRIMARY KEY,
    stock_id           BIGINT NOT NULL REFERENCES stocks(id) ON DELETE CASCADE,
    quantity_used      NUMERIC(12,3) NOT NULL,
    used_for           TEXT,                      -- Optional: lunch, dinner, cleaning, etc.
    used_by            TEXT NOT NULL,             -- could be manager std_id or name
    used_at            TIMESTAMPTZ NOT NULL DEFAULT NOW()
);


-- 1. Threads (one thread per menu date)
CREATE TABLE comment_thread (
  id          BIGSERIAL PRIMARY KEY,
  menu_date   DATE NOT NULL,
  created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE UNIQUE INDEX idx_thread_menu_date ON comment_thread(menu_date);

-- 2. Comments
CREATE TABLE comment (
  id           BIGSERIAL PRIMARY KEY,
  thread_id    BIGINT NOT NULL REFERENCES comment_thread(id) ON DELETE CASCADE,
  parent_id    BIGINT REFERENCES comment(id) ON DELETE CASCADE, -- for replies
  author_id    VARCHAR(50) NOT NULL,  -- can refer to student std_id or manager id
  author_role  VARCHAR(20) NOT NULL,  -- 'STUDENT' or 'MANAGER'
  anonymous    BOOLEAN NOT NULL DEFAULT FALSE,
  content      TEXT NOT NULL,
  created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_comment_thread ON comment(thread_id);

ALTER TABLE menus
    ALTER COLUMN lunch_items TYPE text[] USING lunch_items::text[],
    ALTER COLUMN dinner_items TYPE text[] USING dinner_items::text[];

    CREATE TABLE admin (
    id              UUID PRIMARY KEY          DEFAULT uuid_generate_v4(),
    username        VARCHAR(50)  NOT NULL     UNIQUE,
    email           VARCHAR(120) NOT NULL     UNIQUE,
    password_hash   TEXT        NOT NULL,
    first_name      VARCHAR(50)  NOT NULL,
    last_name       VARCHAR(50),
    phone_number    VARCHAR(15)
);

INSERT INTO admin (username, email, password_hash, first_name, last_name, phone_number)
VALUES ('admin1', 'admin1@example.com', '$2a$10$DAELXEPkSsmtyQZemF07Suk9zx/0n8DajRq32vot7nGw6dly1QhNe', 'Alice', 'Johnson', '+8801234567890');

INSERT INTO admin (username, email, password_hash, first_name, last_name, phone_number)
VALUES ('admin2', 'admin2@example.com', '$2a$10$DAELXEPkSsmtyQZemF07Suk9zx/0n8DajRq32vot7nGw6dly1QhNe', 'Bob', NULL, '+8801987654321');

INSERT INTO admin (username, email, password_hash, first_name, last_name, phone_number)
VALUES ('admin3', 'admin3@example.com', '$2a$10$DAELXEPkSsmtyQZemF07Suk9zx/0n8DajRq32vot7nGw6dly1QhNe', 'Charlie', 'Lee', NULL);
