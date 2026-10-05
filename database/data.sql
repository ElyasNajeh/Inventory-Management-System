USE asha;

INSERT INTO category (category_id, category_name, category_description) VALUES
    (1, 'Drinks', 'Beverage products'),
    (2, 'Food', 'Packaged food products'),
    (3, 'Personal Care', 'Personal care products') AS new
ON DUPLICATE KEY UPDATE
    category_name = new.category_name,
    category_description = new.category_description;

INSERT INTO manufacturer
    (manufacturer_id, manufacturer_name, manufacturer_location, manufacturer_email) VALUES
    (1, 'Asha Supply', 'Ramallah', 'supply@asha.example'),
    (2, 'Palestine Foods', 'Nablus', 'sales@palestinefoods.example') AS new
ON DUPLICATE KEY UPDATE
    manufacturer_name = new.manufacturer_name,
    manufacturer_location = new.manufacturer_location,
    manufacturer_email = new.manufacturer_email;

INSERT INTO product
    (product_id, product_name, unit_price, product_description,
     manufacturer_id, category_id, total_quantity, discount) VALUES
    (1, 'Mineral Water', 2.50, 'Bottled mineral water', 1, 1, 24, 0),
    (2, 'Orange Juice', 6.00, 'Orange juice bottle', 2, 1, 12, 10),
    (3, 'Rice', 12.00, 'Packaged rice', 2, 2, 0, 0) AS new
ON DUPLICATE KEY UPDATE
    product_name = new.product_name,
    unit_price = new.unit_price,
    product_description = new.product_description,
    manufacturer_id = new.manufacturer_id,
    category_id = new.category_id,
    total_quantity = new.total_quantity,
    discount = new.discount;

INSERT INTO shipment (shipment_id, manufacturer_id, shipment_date) VALUES
    (1, 1, DATE_SUB(CURDATE(), INTERVAL 2 MONTH)),
    (2, 2, DATE_SUB(CURDATE(), INTERVAL 2 MONTH)) AS new
ON DUPLICATE KEY UPDATE
    manufacturer_id = new.manufacturer_id,
    shipment_date = new.shipment_date;

INSERT INTO entry
    (entry_id, start_date, expiry_date, quantity, shipment_id, product_id) VALUES
    (1, DATE_SUB(CURDATE(), INTERVAL 2 MONTH), DATE_ADD(CURDATE(), INTERVAL 10 MONTH), 24, 1, 1),
    (2, DATE_SUB(CURDATE(), INTERVAL 2 MONTH), DATE_ADD(CURDATE(), INTERVAL 4 MONTH), 12, 2, 2) AS new
ON DUPLICATE KEY UPDATE
    start_date = new.start_date,
    expiry_date = new.expiry_date,
    quantity = new.quantity,
    shipment_id = new.shipment_id,
    product_id = new.product_id;
