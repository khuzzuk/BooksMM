CREATE USER robot IDENTIFIED BY 'password';
GRANT INSERT, DELETE, REFERENCES, SELECT, TRIGGER, UPDATE, EXECUTE, CREATE ON Library.* TO robot with GRANT OPTION;