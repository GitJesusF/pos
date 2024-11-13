ALTER TABLE sys_user
ADD COLUMN role_id INT;

ALTER TABLE sys_user
ADD CONSTRAINT fk_role
FOREIGN KEY (role_id) REFERENCES sys_role(id);
