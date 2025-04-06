-- Create or replace the timestamp update function
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    IF (NEW.* IS DISTINCT FROM OLD.*) THEN
        NEW.updated_at = NOW();
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create trigger for users table
CREATE OR REPLACE TRIGGER update_users_timestamp
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- Create trigger for todos table
CREATE OR REPLACE TRIGGER update_todos_timestamp
BEFORE UPDATE ON todos
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

COMMENT ON FUNCTION update_timestamp() IS 'Updates updated_at timestamp on row updates';
COMMENT ON TRIGGER update_users_timestamp ON users IS 'Maintains updated_at timestamp on user updates';
COMMENT ON TRIGGER update_todos_timestamp ON todos IS 'Maintains updated_at timestamp on todo updates';
