INSERT INTO user_accounts
(id, creation_time, email_address, is_enabled, modification_time, name, password, role, version)
VALUES
  (1, now(), 'john.doe@gmail.com', true, now(), 'John Doe', 'user', 'ROLE_USER', 0);
INSERT INTO user_accounts
(id, creation_time, email_address, is_enabled, modification_time, name, password, role, version)
VALUES
  (2, now(), 'anne.admin@gmail.com"', true, now(), 'Anne Admin', 'admin', 'ROLE_ADMIN', 0);
