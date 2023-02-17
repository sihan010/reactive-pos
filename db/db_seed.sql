CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE SCHEMA IF NOT EXISTS pos_schema;

CREATE TABLE IF NOT EXISTS pos_schema.payment_method
(
  payment_method_id uuid DEFAULT uuid_generate_v4(),
  payment_method varchar(128) DEFAULT '' NOT NULL,
  price_modifier_from real NOT NULL,
  price_modifier_to real NOT NULL,
  points real NOT NULL,
  created_at timestamp with time zone NOT NULL,
  updated_at timestamp with time zone NOT NULL
);

ALTER TABLE pos_schema.payment_method ADD CONSTRAINT payment_method_id
  PRIMARY KEY (payment_method_id);

CREATE TABLE IF NOT EXISTS pos_schema.sales
(
  sales_id uuid DEFAULT uuid_generate_v4(),
  sales real NOT NULL,
  points real NOT NULL,
  created_at timestamp with time zone NOT NULL,
  updated_at timestamp with time zone NOT NULL
);

ALTER TABLE pos_schema.sales ADD CONSTRAINT sales_id
  PRIMARY KEY (sales_id);

INSERT INTO pos_schema.payment_method (payment_method, price_modifier_from, price_modifier_to, points, created_at, updated_at)
    VALUES ('CASH', 0.90, 1.00, 0.05, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO pos_schema.payment_method (payment_method, price_modifier_from, price_modifier_to, points, created_at, updated_at)
    VALUES ('CASH_ON_DELIVERY', 1.00, 1.02, 0.05, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO pos_schema.payment_method (payment_method, price_modifier_from, price_modifier_to, points, created_at, updated_at)
    VALUES ('VISA', 0.95, 1.00, 0.03, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO pos_schema.payment_method (payment_method, price_modifier_from, price_modifier_to, points, created_at, updated_at)
    VALUES ('MASTERCARD', 0.95, 1.00, 0.03, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO pos_schema.payment_method (payment_method, price_modifier_from, price_modifier_to, points, created_at, updated_at)
    VALUES ('AMEX', 0.98, 1.01, 0.02, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO pos_schema.payment_method (payment_method, price_modifier_from, price_modifier_to, points, created_at, updated_at)
    VALUES ('JCB', 0.95, 1.00, 0.05, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
