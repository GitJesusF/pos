CREATE TABLE sys_user (
id serial NOT NULL,
first_name  character varying(255) NOT NULL,
last_name  character varying(255) NOT NULL,
username  character varying(255) NOT NULL,
password  character varying(255) NOT NULL,
active boolean,
CONSTRAINT pk_sys_user PRIMARY KEY(id),
CONSTRAINT uc_sys_user_01 UNIQUE (username)
);