CREATE TABLE todos (
    id BIGSERIAL PRIMARY KEY,
    version INTEGER NOT NULL DEFAULT 0,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    priority VARCHAR(10) NOT NULL,
    status VARCHAR(20) NOT NULL,
    due_date TIMESTAMP,
    completed_at TIMESTAMP,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255),
    CONSTRAINT fk_todos_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX idx_todos_user_id ON todos (user_id);
CREATE INDEX idx_todos_status ON todos (status);
CREATE INDEX idx_todos_priority ON todos (priority);
CREATE INDEX idx_todos_due_date ON todos (due_date);
