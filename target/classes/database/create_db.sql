CREATE TABLE address
(
  id                        INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  country                   VARCHAR(255)        NOT NULL,
  city                      DOUBLE              NOT NULL,
  street                    VARCHAR(50),
  building_number           INT(11),
  apartment_number          INT(11),
  deleted                   TINYINT(1)          DEFAULT 0
);

CREATE TABLE gender
(
  id                        INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name_ru                   VARCHAR(20)        NOT NULL,
  name_en                   VARCHAR(20)        NOT NULL,
);

CREATE TABLE image
(
  id                        INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name                      VARCHAR(20)         NOT NULL,
  product_id                INT(11)             NOT NULL,
  content                   LONGBLOB            NOT NULL,
  date_modified             TIMESTAMP           NOT NULL,
  deleted                   TINYINT(1)          DEFAULT 0
);

CREATE TABLE order
(
  id                        INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  user_id                   INT(11)        NOT NULL,
  created                   TIMESTAMP      NOT NULL,
  description               VARCHAR(200),
  status_id                 INT(11)        NOT NULL,
  deleted                   TINYINT(1)     DEFAULT 0
);

CREATE TABLE order_status
(
  id                        INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name_ru                   VARCHAR(30)        NOT NULL,
  name_en                   VARCHAR(30)        NOT NULL,
  deleted                   TINYINT(1)         DEFAULT 0
);

CREATE TABLE ordering_item
(
  id                        INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  order_id                  INT(11)        NOT NULL,
  product_id                INT(11)        NOT NULL,
  amount                    INT(11)        NOT NULL,
  deleted                   TINYINT(1)     DEFAULT 0
);

CREATE TABLE product
(
  id                        INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name                      VARCHAR(100)        NOT NULL,
  price                     INT(11)             NOT NULL,
  type                      INT(11)             NOT NULL,
  descritpion_RU            VARCHAR(600)        NOT NULL,
  descritpion_EN            VARCHAR(600)        NOT NULL,
  deleted                   TINYINT(1)          DEFAULT 0
);

CREATE TABLE product_type
(
  id                        INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name_ru                   VARCHAR(30)        NOT NULL,
  name_en                   VARCHAR(30)        NOT NULL,
  deleted                   TINYINT(1)         DEFAULT 0
);

CREATE TABLE storage
(
  id                        INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name                      VARCHAR(100)        NOT NULL,
  descritpion_RU            VARCHAR(300)        NOT NULL,
  descritpion_EN            VARCHAR(300)        NOT NULL,
  deleted                   TINYINT(1)          DEFAULT 0
);

CREATE TABLE storage_item
(
  id                        INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  storage_id                INT(11)             NOT NULL,
  product_id                INT(11)             NOT NULL,
  amount                    INT(11)             NOT NULL,
  deleted                   TINYINT(1)          DEFAULT 0
);

CREATE TABLE user
(
  id                        INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  email                     VARCHAR(30)         NOT NULL,
  password                  VARCHAR(31)         NOT NULL,
  firtsname                 VARCHAR(20)         NOT NULL,
  lastsname                 VARCHAR(20)         NOT NULL,
  address_id                INT(11)             NOT NULL,
  phonenumber               VARCHAR(15)         NOT NULL,
  role                      VARCHAR(5)          NOT NULL,
  cash                      INT(11))            NOT NULL,
  gender_id                 INT(11)             NOT NULL,
  deleted                   TINYINT(1)          DEFAULT 0
);