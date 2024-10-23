CREATE DATABASE 

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fullname VARCHAR(100) DEFAULT '',
    phone_name VARCHAR(15) NOT NULL,
    address VARCHAR(200) DEFAULT '',
    password VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active TINYINT(1) DEFAULT 1,
    date_of_birth DATE,
    facebook_account_id INT DEFAULT 0,
    google_account_id INT DEFAULT 0
);
ALTER TABLE users ADD COLUMN role_id INT;
ALTER TABLE users ADD FOREIGN KEY (role_id) REFERENCES roles (id);
CREATE TABLE roles (
    id INT PRIMARY KEY,
    name VARCHAR (20) NOT NULL
);
CREATE TABLE tokens (
    id INT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(255) UNIQUE NOT NULL,
    token_type VARCHAR(50) NOT NULL,
    expiration_date DATETIME,
    revoked TINYINT(1) NOT NULL DEFAULT 0,  -- Thêm giá trị mặc định cho các cột boolean
    expired TINYINT(1) NOT NULL DEFAULT 0,
    user_id INT,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE social_account(
id INT PRIMARY KEY AUTO_INCREMENT,
provider VARCHAR(20) NOT NULL COMMENT 'Tên nhà social network',
provide_id VARCHAR(50) NOT NULL,
email VARCHAR(150) NOT NULL COMMENT 'Email tài khoản',
name VARCHAR(100) NOT NULL COMMENT 'Tên người dùng',
user_id int,
(user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name varchar(100) NOT NULL DEFAULT '' COMMENT 'Tên danh mục, vd: đồ điện tử'
);

CREATE TABLE products(
id INT PRIMARY KEY AUTO_INCREMENT,
name varchar(350) COMMENT 'Tên sản phẩm',
price FLOAT NOT NULL CHECK(price>=0),
thumbnail VARCHAR(300) DEFAULT '',
description LONGTEXT DEFAULT '',
created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
category_id INT,
FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE orders (
 id INT PRIMARY KEY AUTO_INCREMENT,
 user_id int,
 FOREIGN KEY (user_id) REFERENCES users(id),
 fullname VARCHAR(100) NOT NULL,
 email VARCHAR(100) NOT NULL,
 phone_number VARCHAR (20) NOT NULL,
 address VARCHAR (200) NOT NULL,
 note VARCHAR(100) DEFAULT '',
 order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
 status VARCHAR(20)
 total_money FLOAT CHECK(total_money >=0)
);

-- cac thuoc tinh phu
ALTER TABLE orders ADD COLUMN shipping_method VARCHAR(100);
ALTER TABLE orders ADD COLUMN shipping_address VARCHAR(200);
ALTER TABLE orders ADD COLUMN shipping_date DATE;
ALTER TABLE orders ADD COLUMN shipping_number VARCHAR(100);


--trang thai don hang chi nhan 1 so gia tri cu the
ALTER TABLE orders
MODIFY COLUMN status ENUM('pending','processing','shipped','delivered','cancelled')
COMMENT 'trang thai don hang'
CREATE TABLE order_details (
id INT PRIMARY KEY AUTO_INCREMENT,
order_id INT,
FOREIGN KEY (order_id) REFERENCES orders(id)
product_id INT,
FOREIGN KEY (product_id) REFERENCES products(id),
price FLOAT CHECK(price>= 0),
number_of_products INT CHECK (number_of_products >0)
total_money FLOAT CHECK(total_money >=0),
color VARCHAR(20) DEFAULT ''
); 

