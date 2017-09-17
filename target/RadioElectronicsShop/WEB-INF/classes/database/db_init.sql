# MySQL server variables are temporarily set to enable faster SQL import by the server.
SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL,ALLOW_INVALID_DATES';
SET GLOBAL max_allowed_packet=104857600;

# Table `gender`
CREATE TABLE IF NOT EXISTS `gender` (
  `id`      INT         NOT NULL AUTO_INCREMENT,
  `name_ru` VARCHAR(45) NOT NULL,
  `name_en` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name_ru` ASC),
  UNIQUE INDEX `name_en_UNIQUE` (`name_en` ASC)
)
  ENGINE = InnoDB;

# Table `user`
CREATE TABLE `user` (
  `id`           INT(11)        NOT NULL AUTO_INCREMENT,
  `email`        VARCHAR(80)    NOT NULL,
  `password`     VARCHAR(45)    NOT NULL,
  `firstname`   VARCHAR(45)             DEFAULT NULL,
  `lastname`    VARCHAR(45)             DEFAULT NULL,
  `address_id`   INT(11)        NOT NULL,
  `phonenumber` VARCHAR(45)             DEFAULT NULL,
  `role`         VARCHAR(45)    NOT NULL,
  `cash`         DECIMAL(12, 0) NOT NULL DEFAULT '0',
  `gender_id`    INT(11)        NOT NULL,
  `deleted`      TINYINT(1)     NOT NULL DEFAULT '0',

  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  KEY `fk_user_gender_idx` (`gender_id`),
  KEY `fk_user_address_idx` (`address_id`),
  CONSTRAINT `fk_user_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`),
  CONSTRAINT `fk_user_gender` FOREIGN KEY (`gender_id`) REFERENCES `gender` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;

# Table `address`
CREATE TABLE IF NOT EXISTS `address` (
  `id`               INT         NOT NULL AUTO_INCREMENT,
  `country`          VARCHAR(45) NOT NULL,
  `city`             VARCHAR(45) NOT NULL,
  `street`           VARCHAR(45) NOT NULL,
  `building_number`  VARCHAR(45) NOT NULL,
  `apartment_number` VARCHAR(45) NOT NULL,
  `deleted`          TINYINT(1)  NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB;

# Table `product_type`
CREATE TABLE IF NOT EXISTS `product_type` (
  `id`      INT          NOT NULL AUTO_INCREMENT,
  `name_ru` VARCHAR(255) NOT NULL,
  `name_en` VARCHAR(255) NOT NULL,
  `deleted` TINYINT(1)   NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB;

# Table `order`
CREATE TABLE IF NOT EXISTS `order` (
  `id`              INT          NOT NULL AUTO_INCREMENT,
  `user_id`         INT          NOT NULL,
  `created`         DATETIME     NOT NULL,
  `description`     VARCHAR(255) NULL,
  `order_status_id` INT          NOT NULL,
  `deleted`         TINYINT(1)   NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`, `user_id`),
  INDEX `fk_orders_users_idx` (`user_id` ASC),
  INDEX `fk_order_order_status_idx` (`order_status_id` ASC),
  CONSTRAINT `fk_orders_users`
  FOREIGN KEY (`user_id`)
  REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_order_status`
  FOREIGN KEY (`order_status_id`)
  REFERENCES `order_status` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;

# Table `order_status`
CREATE TABLE IF NOT EXISTS `order_status` (
  `id`      INT         NOT NULL AUTO_INCREMENT,
  `name_ru` VARCHAR(45) NOT NULL,
  `name_en` VARCHAR(45) NOT NULL,
  `deleted` TINYINT(1)  NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name_ru` ASC),
  UNIQUE INDEX `name_en_UNIQUE` (`name_en` ASC)
)
  ENGINE = InnoDB;

# Table `product`
CREATE TABLE IF NOT EXISTS `product` (
  `id`              INT          NOT NULL AUTO_INCREMENT,
  `name`            VARCHAR(45)  NOT NULL,
  `price`           DECIMAL(12)  NOT NULL,
  `type_id` INT          NOT NULL,
  `description_RU`  VARCHAR(255) NULL,
  `description_EN`  VARCHAR(255) NULL,
  `deleted`         TINYINT(1)   NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `fk_product_product_type_idx` (`type_id` ASC),
  CONSTRAINT `fk_product_product_type`
  FOREIGN KEY (`type_id`)
  REFERENCES `product_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;

# Table `ordering_item`
CREATE TABLE IF NOT EXISTS `ordering_item` (
  `id`         INT        NOT NULL AUTO_INCREMENT,
  `order_id`   INT        NOT NULL,
  `product_id` INT        NOT NULL,
  `amount`     INT        NOT NULL,
  `deleted`    TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`, `order_id`, `product_id`),
  INDEX `fk_order_item_orders_idx` (`order_id` ASC),
  INDEX `fk_order_item_product_idx` (`product_id` ASC),
  CONSTRAINT `fk_order_item_orders`
  FOREIGN KEY (`order_id`)
  REFERENCES `order` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_item_product`
  FOREIGN KEY (`product_id`)
  REFERENCES `product` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;

# Table `storage`
CREATE TABLE IF NOT EXISTS `storage` (
  `id`             INT          NOT NULL AUTO_INCREMENT,
  `name`           VARCHAR(45)  NOT NULL,
  `description_RU` VARCHAR(255) NULL,
  `description_EN` VARCHAR(255) NULL,
  `deleted`        TINYINT(1)   NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB;

# Table `storage_item`
CREATE TABLE IF NOT EXISTS `storage_item` (
  `id`         INT        NOT NULL AUTO_INCREMENT,
  `storage_id` INT        NOT NULL,
  `product_id` INT        NOT NULL,
  `amount`     INT        NOT NULL,
  `deleted`    TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`, `storage_id`, `product_id`),
  INDEX `fk_storage_items_storages_idx` (`storage_id` ASC),
  INDEX `fk_storage_item_product_idx` (`product_id` ASC),
  CONSTRAINT `fk_storage_items_storages`
  FOREIGN KEY (`storage_id`)
  REFERENCES `storage` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_storage_item_product`
  FOREIGN KEY (`product_id`)
  REFERENCES `product` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;

# Table `image`
CREATE TABLE IF NOT EXISTS `image` (
  `id`           INT         NOT NULL AUTO_INCREMENT,
  `name`         VARCHAR(45) NOT NULL,
  `product_id`   INT         NOT NULL,
  `content`      LONGBLOB    NOT NULL,
  `date_modified`     DATETIME    NOT NULL,
  `deleted`      TINYINT(1)  NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`, `product_id`),
  INDEX `fk_image_product_idx` (`product_id` ASC),
  CONSTRAINT `fk_image_product`
  FOREIGN KEY (`product_id`)
  REFERENCES `product` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
  ENGINE = InnoDB;

# Server variables are reset at the end of the script
SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;

SET @@GLOBAL.wait_timeout=86400;
# Init default data
INSERT INTO gender (name_ru, name_en) VALUES ('Мужчина', 'Male');
INSERT INTO gender (name_ru, name_en) VALUES ('Женщина', 'Female');

INSERT INTO user (email, password, firstname, lastname, address_id, phonenumber, role, cash, gender_id)
VALUES ('lapin1995@mail.ru', 'd6b2f0af433a27cb8ed917f13cfe0d4a', 'Igor', 'Lapin', 1, '87472151228','admin', 50000, 1);
INSERT INTO user (email, password, firstname, lastname, address_id, phonenumber, role, cash, gender_id)
VALUES ('supermacho@mail.ru', 'd6b2f0af433a27cb8ed917f13cfe0d4a', 'Igor', 'Lapin', 3, '87472151230','user', 200000, 1);
INSERT INTO user (email, password, firstname, lastname, address_id, phonenumber, role, cash, gender_id)
VALUES ('ermrodion@mail.ru', 'd6b2f0af433a27cb8ed917f13cfe0d4a', 'Rodion', 'Ermolin', 2, '87473643644','user', 150000, 1);

INSERT INTO address (country, city, street, building_number, apartment_number)
VALUES ('Kazakhstan', 'Karaganda', 'Stepnoi', '4', '98');
INSERT INTO address (country, city, street, building_number, apartment_number)
VALUES ('Kazakhstan', 'Karaganda', 'Vostok', '5', '13');
INSERT INTO address (country, city, street, building_number, apartment_number)
VALUES ('Kazakhstan', 'Astana', 'Kunanbaev', '45', '12');

INSERT INTO product_type (name_ru, name_en) VALUES ('Диоды', 'Diodes');
INSERT INTO product_type (name_ru, name_en) VALUES ('Резисторы', 'Resistors');
INSERT INTO product_type (name_ru, name_en) VALUES ('Датчики', 'Sensors');
INSERT INTO product_type (name_ru, name_en) VALUES ('Двигатели', 'Motors');
INSERT INTO product_type (name_ru, name_en) VALUES ('Светодиоды и индикаторы', 'LED and indicators');

INSERT INTO product (name, price, description_RU, description_EN, type_id) VALUES ('7 segment indicator', 125,
                                                                                           'Семисегментный индикатор, служит для отображения информации. Управляется цифровыми сигналами.',
                                                                                           '7-segment indicator serves for displaying information. Controlled by digital signals.',
                                                                                           5);
INSERT INTO product (name, price, description_RU, description_EN, type_id) VALUES ('10 segment indicator', 250,
                                                                                   'Десятисегментный индикатор, служит для отображения информации. Управляется цифровыми сигналами.',
                                                                                   '10-segment indicator serves for displaying information. Controlled by digital signals.',
                                                                                   5);
INSERT INTO product (name, price, description_RU, description_EN, type_id) VALUES ('Laser 650nm', 500,
                                                                                           'Лазер с длинной волны 650нм. Красный диапазон',
                                                                                           ' Laser with wavelength 650nm. Red range',
                                                                                           5);
INSERT INTO product (name, price, description_RU, description_EN, type_id) VALUES ('Digital Hole sensor', 450,
                                                                                           'Датчик Холла. Служит для измерения скорости вращения в бесщеточных двигателях',
                                                                                           'Hole sensor. Serves for speed measurement in non-brush motors',
                                                                                           4);
INSERT INTO product (name, price, description_RU, description_EN, type_id) VALUES ('LED red-green', 79000,
                                                                                           'Светодиод 5мм, с общим анодом. Цвета: красный и зеленый.',
                                                                                           'LED 5mm, with common anod. Colors: red and green.',
                                                                                           5);
INSERT INTO product (name, price, description_RU, description_EN, type_id) VALUES ('Thermistor', 240,
                                                                                           'Датчик температуры. Термистор. 0 - 70.',
                                                                                           'Temperature sensor. Thermistor 0 - 70.',
                                                                                           3);
INSERT INTO product (name, price, description_RU, description_EN, type_id) VALUES ('Motor driver', 1000,
                                                                                           'Драйвер шагового двигателя. Аналоговый, 5В.',
                                                                                           'Step motor driver. Analog, 5V.',
                                                                                           4);
INSERT INTO product (name, price, description_RU, description_EN, type_id) VALUES ('Driver TB6560', 4500,
                                                                                           'Драйвер шагового двигателя, 3А. Напряжение 24В.',
                                                                                           'Step motor driver, 3A. Voltage 24V.',
                                                                                           4);
INSERT INTO product (name, price, description_RU, description_EN, type_id) VALUES ('Photoresistor', 150,
                                                                                           'Фоторезистор. Датчик света.',
                                                                                           'Photoresistor. Light sensor.',
                                                                                           3);
INSERT INTO product (name, price, description_RU, description_EN, type_id) VALUES ('LED Lamp', 400,
                                                                                           'Светодиодная лампа, 5Вт.',
                                                                                           'LED lamp. 5W.',
                                                                                   5);
INSERT INTO product (name, price, description_RU, description_EN, type_id) VALUES ('Step motor', 700,
                                                                                           'Шаговый двигатель 1А.',
                                                                                           'Step motor 1A.',
                                                                                   4);
INSERT INTO product (name, price, description_RU, description_EN, type_id) VALUES ('Step motor', 800,
                                                                                           'Шаговый двигатель 2А. С платой управления.',
                                                                                           'Step motor 2A. With control board.',
                                                                                   4);
INSERT INTO product (name, price, description_RU, description_EN, type_id) VALUES ('Step motor', 600,
                                                                                           'Шаговый двигатель 24В.',
                                                                                           'Step motor 24V.',
                                                                                   4);
INSERT INTO product (name, price, description_RU, description_EN, type_id) VALUES ('LED color lens', 330,
                                                                                           'Цветная светодиодная линза.',
                                                                                           'LED color lence',
                                                                                   5);
INSERT INTO product (name, price, description_RU, description_EN, type_id) VALUES ('LED-rect', 100,
                                                                                           'Светодиод прямоугольный, 5В.',
                                                                                           'LED rectangle, 5V.',
                                                                                   5);
INSERT INTO product (name, price, description_RU, description_EN, type_id) VALUES ('Infrared reflector', 280,
                                                                                           'Инфракрасный отражатель. Датчик наличия.',
                                                                                           'Infrared reflector. Availability sensor.',
                                                                                   3);
INSERT INTO product (name, price, description_RU, description_EN, type_id) VALUES ('Servo', 280,
                                                                                         'Сервопривод с лопостями.',
                                                                                         'Servo driver vanes.',
                                                                                   4);
INSERT INTO product (name, price, description_RU, description_EN, type_id) VALUES ('SMD-0805', 2400,
                                                                                         'Чип (SMD) резисторы 0805, комплект (1700 штук)',
                                                                                         'Chip (SMD) resistors 0805, pack (1700 pieces)',
                                                                                   2);
INSERT INTO product (name, price, description_RU, description_EN, type_id) VALUES ('Metal foil resistors', 50,
                                                                                         'Металлофольговый резисторы, 0,25Вт.',
                                                                                         'Metal foil resistors, 0,25W.',
                                                                                   2);
INSERT INTO product (name, price, description_RU, description_EN, type_id) VALUES ('Alternate resistors', 150,
                                                                                         'Переменные резисторы, потенциометры. 20мм.',
                                                                                         'Alternate resistors, potentiometers. 20mm.',
                                                                                   2);
INSERT INTO product (name, price, description_RU, description_EN, type_id) VALUES ('KBL610 ', 240,
                                                                                         'Диодный мост.',
                                                                                         'Diode bridge.',
                                                                                   1);
INSERT INTO product (name, price, description_RU, description_EN, type_id) VALUES ('SMA4007 ', 70,
                                                                                         'Диод, 1А.',
                                                                                         'Diode, 1A.',
                                                                                   1);
INSERT INTO product (name, price, description_RU, description_EN, type_id) VALUES ('KBPC3504 ', 635,
                                                                                         'Диодный мост. 35А.',
                                                                                         'Diode bridge. 35A.',
                                                                                   1);


INSERT INTO storage (name, description_ru, description_en)
VALUES ('Central', 'Центральный склад. Город Карагадна.', 'Central storage. Karaganda city.');

INSERT INTO storage_item (storage_id, product_id, amount) VALUES (1, 1, 5);
INSERT INTO storage_item (storage_id, product_id, amount) VALUES (1, 2, 10);
INSERT INTO storage_item (storage_id, product_id, amount) VALUES (1, 3, 11);
INSERT INTO storage_item (storage_id, product_id, amount) VALUES (1, 4, 100);
INSERT INTO storage_item (storage_id, product_id, amount) VALUES (1, 5, 12);
INSERT INTO storage_item (storage_id, product_id, amount) VALUES (1, 6, 13);
INSERT INTO storage_item (storage_id, product_id, amount) VALUES (1, 7, 17);
INSERT INTO storage_item (storage_id, product_id, amount) VALUES (1, 8, 28);
INSERT INTO storage_item (storage_id, product_id, amount) VALUES (1, 9, 3);
INSERT INTO storage_item (storage_id, product_id, amount) VALUES (1, 10, 8);
INSERT INTO storage_item (storage_id, product_id, amount) VALUES (1, 11, 10);
INSERT INTO storage_item (storage_id, product_id, amount) VALUES (1, 12, 10);
INSERT INTO storage_item (storage_id, product_id, amount) VALUES (1, 13, 16);
INSERT INTO storage_item (storage_id, product_id, amount) VALUES (1, 14, 8);
INSERT INTO storage_item (storage_id, product_id, amount) VALUES (1, 15, 7);
INSERT INTO storage_item (storage_id, product_id, amount) VALUES (1, 16, 9);
INSERT INTO storage_item (storage_id, product_id, amount) VALUES (1, 17, 12);
INSERT INTO storage_item (storage_id, product_id, amount) VALUES (1, 18, 18);
INSERT INTO storage_item (storage_id, product_id, amount) VALUES (1, 19, 150);
INSERT INTO storage_item (storage_id, product_id, amount) VALUES (1, 20, 60);
INSERT INTO storage_item (storage_id, product_id, amount) VALUES (1, 21, 45);
INSERT INTO storage_item (storage_id, product_id, amount) VALUES (1, 22, 33);
INSERT INTO storage_item (storage_id, product_id, amount) VALUES (1, 23, 9);

INSERT INTO order_status (name_ru, name_en) VALUES ('Не обработан', 'Not processed');
INSERT INTO order_status (name_ru, name_en) VALUES ('В обработке', 'Processing');
INSERT INTO order_status (name_ru, name_en) VALUES ('Доставлен', 'Delivered');
INSERT INTO order_status (name_ru, name_en) VALUES ('Закрыт', 'Closed');

INSERT INTO `order` (user_id, created, description, status_id) VALUES (2, NOW(),'I want to buy this!', 1);
INSERT INTO `order` (user_id, created, description, status_id) VALUES (3, NOW(),'I want to buy this!', 1);
INSERT INTO `order` (user_id, created, description, status_id) VALUES (1, NOW(),'For EXCM-ST-42', 1);

INSERT INTO ordering_item ( order_id, product_id, amount) VALUES (1, 1, 5);
INSERT INTO ordering_item ( order_id, product_id, amount) VALUES (1, 4, 2);
INSERT INTO ordering_item ( order_id, product_id, amount) VALUES (2, 3, 1);
INSERT INTO ordering_item ( order_id, product_id, amount) VALUES (2, 5, 3);
INSERT INTO ordering_item ( order_id, product_id, amount) VALUES (2, 6, 4);
INSERT INTO ordering_item ( order_id, product_id, amount) VALUES (3, 12, 7);

INSERT INTO image (name, product_id, content, date_modified)
VALUES ('7-segment-indicator', 1, LOAD_FILE('C:\Users\User\Desktop\photo\7-segmentnyj-cifrovoj-led-indikator.jpg'), NOW());
INSERT INTO image (name, product_id, content, date_modified)
VALUES ('7-segment-indicator', 2, LOAD_FILE('.src\\main\\resources\\database\\test_photo\\10-segmentnyj-zelenyj-svetodiodnyj-indikator.jpg'), NOW());
INSERT INTO image (name, product_id, content, date_modified)
VALUES ('7-segment-indicator', 3, LOAD_FILE('.src/main/resources/database/test_photo/650nm-lazer-5mvt.jpg'), NOW());
INSERT INTO image (name, product_id, content, date_modified)
VALUES ('7-segment-indicator', 4, LOAD_FILE('.src|main|resources|database|test_photo|chip-smd-rezistory-0805-komplekt-5-1700-shtuk.jpg'), NOW());
INSERT INTO image (name, product_id, content, date_modified)
VALUES ('7-segment-indicator', 5, LOAD_FILE('.src\main\resources\database\test_photo\cifrovoj-datchik-kholla-ss41f.jpg'), NOW());
INSERT INTO image (name, product_id, content, date_modified)
VALUES ('7-segment-indicator', 6, LOAD_FILE('.src\main\resources\database\test_photo\datchik-temperatury-termistor-ntc-mf52-103-3435.jpg'), NOW());
INSERT INTO image (name, product_id, content, date_modified)
VALUES ('7-segment-indicator', 7, LOAD_FILE('.src\main\resources\database\test_photo\drajver-dvigatelej-analog-l298n.jpg'), NOW());
INSERT INTO image (name, product_id, content, date_modified)
VALUES ('7-segment-indicator', 8, LOAD_FILE('.src\main\resources\database\test_photo\drajver-shagovogo-dvigatelya-tb6600-4a.jpg'), NOW());
INSERT INTO image (name, product_id, content, date_modified)
VALUES ('7-segment-indicator', 9, LOAD_FILE('.src\main\resources\database\test_photo\fotorezistor-gl5516.jpg'), NOW());
INSERT INTO image (name, product_id, content, date_modified)
VALUES ('7-segment-indicator', 10, LOAD_FILE('.src\main\resources\database\test_photo\kbl610-dioidnyj-most-kbl.jpg'), NOW());
INSERT INTO image (name, product_id, content, date_modified)
VALUES ('7-segment-indicator', 11, LOAD_FILE('.src\main\resources\database\test_photo\led-lampochka-5w-svetodiodnaya.jpg'), NOW());
INSERT INTO image (name, product_id, content, date_modified)
VALUES ('7-segment-indicator', 12, LOAD_FILE('.src\main\resources\database\test_photo\metallofolgovye-rezistory-025vt-1.jpg'), NOW());
INSERT INTO image (name, product_id, content, date_modified)
VALUES ('7-segment-indicator', 13, LOAD_FILE('.src\main\resources\database\test_photo\rezistor-peremennyj-potenciometr-wh148-1a.jpg'), NOW());
INSERT INTO image (name, product_id, content, date_modified)
VALUES ('7-segment-indicator', 14, LOAD_FILE('.src\main\resources\database\test_photo\shagovyj-dvigatel-17hd3404-23d-12v-24v-13a-026nm.jpg'), NOW());
INSERT INTO image (name, product_id, content, date_modified)
VALUES ('7-segment-indicator', 15, LOAD_FILE('.src\main\resources\database\test_photo\shagovyj-dvigatel-28byj-48-s-platoj-upravleniya.jpg'), NOW());
INSERT INTO image (name, product_id, content, date_modified)
VALUES ('7-segment-indicator', 16, LOAD_FILE('.src\main\resources\database\test_photo\svetodiod-3-mm-s-cvetnoj-linzoj.jpg'), NOW());
INSERT INTO image (name, product_id, content, date_modified)
VALUES ('7-segment-indicator', 17, LOAD_FILE('.src\main\resources\database\test_photo\svetodiodnaya-matrica-50vt-6000k-3500lm-35v.jpg'), NOW());
INSERT INTO image (name, product_id, content, date_modified)
VALUES ('7-segment-indicator', 18, LOAD_FILE('.src\main\resources\database\test_photo\svetodiod-pryamougolnyj-257.jpg'), NOW());
INSERT INTO image (name, product_id, content, date_modified)
VALUES ('7-segment-indicator', 19, LOAD_FILE('.src\main\resources\database\test_photo\tcrt5000-infrakrasnyj-otrazhatel.jpg'), NOW());
INSERT INTO image (name, product_id, content, date_modified)
VALUES ('7-segment-indicator', 20, LOAD_FILE('.src\main\resources\database\test_photo\towerpro-mg995-servoprivod.jpg'), NOW());
INSERT INTO image (name, product_id, content, date_modified)
VALUES ('7-segment-indicator', 21, LOAD_FILE('.src\main\resources\database\test_photo\ultrazvukovoj-dalnomer-us-100-s-interfejsom-uart.jpg'), NOW());
INSERT INTO image (name, product_id, content, date_modified)
VALUES ('7-segment-indicator', 22, LOAD_FILE('.src\main\resources\database\test_photo\universalnyj-zvukovoj-datchik-analog-i-cifra.jpg'), NOW());
INSERT INTO image (name, product_id, content, date_modified)
VALUES ('7-segment-indicator', 23, LOAD_FILE('.src\main\resources\database\test_photo\universalnyj-zvukovoj-datchik-analog-i-cifra.jpg'), NOW());


