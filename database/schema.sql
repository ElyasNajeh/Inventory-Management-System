CREATE DATABASE IF NOT EXISTS asha
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

USE asha;

CREATE TABLE IF NOT EXISTS category (
    category_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    category_name VARCHAR(100) NOT NULL,
    category_description TEXT NOT NULL,
    PRIMARY KEY (category_id),
    CONSTRAINT uq_category_name UNIQUE (category_name),
    CONSTRAINT chk_category_name_not_blank CHECK (TRIM(category_name) <> '')
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS manufacturer (
    manufacturer_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    manufacturer_name VARCHAR(120) NOT NULL,
    manufacturer_location VARCHAR(160) NOT NULL,
    manufacturer_email VARCHAR(254) NOT NULL,
    PRIMARY KEY (manufacturer_id),
    CONSTRAINT uq_manufacturer_email UNIQUE (manufacturer_email),
    CONSTRAINT chk_manufacturer_name_not_blank CHECK (TRIM(manufacturer_name) <> ''),
    CONSTRAINT chk_manufacturer_location_not_blank CHECK (TRIM(manufacturer_location) <> '')
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS product (
    product_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    product_name VARCHAR(150) NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    product_description TEXT NOT NULL,
    manufacturer_id INT UNSIGNED NOT NULL,
    category_id INT UNSIGNED NOT NULL,
    total_quantity INT UNSIGNED NOT NULL DEFAULT 0,
    discount DECIMAL(5, 2) NOT NULL DEFAULT 0,
    PRIMARY KEY (product_id),
    INDEX idx_product_manufacturer (manufacturer_id),
    INDEX idx_product_category (category_id),
    CONSTRAINT fk_product_manufacturer
        FOREIGN KEY (manufacturer_id) REFERENCES manufacturer (manufacturer_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id) REFERENCES category (category_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT chk_product_name_not_blank CHECK (TRIM(product_name) <> ''),
    CONSTRAINT chk_product_unit_price CHECK (unit_price >= 0 AND unit_price <= 100000),
    CONSTRAINT chk_product_discount CHECK (discount >= 0 AND discount <= 100)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS shipment (
    shipment_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    manufacturer_id INT UNSIGNED NOT NULL,
    shipment_date DATE NOT NULL,
    PRIMARY KEY (shipment_id),
    INDEX idx_shipment_manufacturer (manufacturer_id),
    CONSTRAINT fk_shipment_manufacturer
        FOREIGN KEY (manufacturer_id) REFERENCES manufacturer (manufacturer_id)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS entry (
    entry_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    start_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    quantity INT UNSIGNED NOT NULL,
    shipment_id INT UNSIGNED NOT NULL,
    product_id INT UNSIGNED NOT NULL,
    PRIMARY KEY (entry_id),
    INDEX idx_entry_shipment (shipment_id),
    INDEX idx_entry_product (product_id),
    INDEX idx_entry_expiry_date (expiry_date),
    CONSTRAINT fk_entry_shipment
        FOREIGN KEY (shipment_id) REFERENCES shipment (shipment_id)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_entry_product
        FOREIGN KEY (product_id) REFERENCES product (product_id)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT chk_entry_quantity CHECK (quantity > 0),
    CONSTRAINT chk_entry_dates CHECK (expiry_date > start_date)
) ENGINE = InnoDB;
