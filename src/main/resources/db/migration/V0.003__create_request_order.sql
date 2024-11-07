CREATE TABLE item(
 id serial NOT NULL,
 code character varying(20) NOT NULL,
 name character varying(255) NOT NULL,
 price numeric(15, 2) NOT NULL,
 CONSTRAINT pk_item PRIMARY KEY (id),
 CONSTRAINT uc_item_01 UNIQUE (code)
);

CREATE TABLE customer(
 id serial NOT NULL,
 first_name character varying(255) NOT NULL,
 last_name character varying(255) NOT NULL,
 phone character varying(255),
 CONSTRAINT pk_customer PRIMARY KEY (id)
);

CREATE TABLE request_order(
 id serial NOT NULL,
 customer_id integer NOT NULL,
 order_date date NOT NULL,
 total numeric(15, 2) NOT NULL,
 CONSTRAINT pk_request_order PRIMARY KEY (id)
);

CREATE TABLE request_order_item(
 id serial NOT NULL,
 request_order_id integer NOT NULL,
 item_id integer NOT NULL,
 price numeric(15, 2) NOT NULL,
 quantity integer NOT NULL,
 subtotal numeric(15, 2) NOT NULL,
 CONSTRAINT pk_request_order_item PRIMARY KEY (id)
);