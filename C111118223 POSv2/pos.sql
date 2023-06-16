DROP DATABASE IF EXISTS `db_pos`;
CREATE DATABASE IF NOT EXISTS `db_pos`;

USE `db_pos`;

DROP TABLE IF EXISTS `account`;
CREATE TABLE IF NOT EXISTS `account` (
  `id` VARCHAR(20) NOT NULL,
  `money` INT(15) NOT NULL,
  `day` INT(15) NOT NULL,
  PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

INSERT INTO `account` (`id`, `money`, `day`) VALUES
	('a-001', 1000, 1);

DROP TABLE IF EXISTS `ingrediate`;
CREATE TABLE IF NOT EXISTS `ingrediate` (
  `ingrediate_id` varchar(20) NOT NULL,
  `name` varchar(150) NOT NULL,
  `image` varchar(200) NOT NULL, 
  `cost` int(11) NOT NULL,    
  `nowStock` int(11) NOT NULL,       
  `describe` varchar(200) DEFAULT NULL,
  `origin` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`ingrediate_id`)     
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

INSERT INTO `ingrediate` (`ingrediate_id`, `name`, `image`, `cost`, `nowStock`, `describe`, `origin`) VALUES
	('i-001', '雞蛋', 'i-001.jpg', 10, 0, '新鮮的雞蛋，讚喔', '安佳農場:台中市泰仁區興隆路7-4號'),
	('i-002', '麵粉', 'i-002.jpg', 5, 0, '新鮮的麵粉，真香', '大崗農場:嘉義縣中仁區平等路74號'),
	('i-003', '豬肉', 'i-003.jpg', 20, 0, '油脂豐富的豬肉', '高產畜牧業:台東縣高等區不同路52號'),
	('i-004', '牛肉', 'i-004.jpg', 30, 27, '很貴的牛肉', '高產畜牧業:台東縣高等區不同路52號');
	
DROP TABLE IF EXISTS `commodity`;
CREATE TABLE IF NOT EXISTS `commodity` (
  `commodity_id` varchar(20) NOT NULL,
  `commodity_name` varchar(150) NOT NULL,
  `image` varchar(200) NOT NULL, 
  `total_cost` int(11) NOT NULL, 
  `price` int(11) NOT NULL,
  `describe` varchar(200) DEFAULT NULL,
  `purchased` VARCHAR(1) DEFAULT 'n',
  PRIMARY KEY (`commodity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

INSERT INTO `commodity` (`commodity_id`, `commodity_name`, `image`, `total_cost`, `price`, `describe`) VALUES
  ('c-002', '牛排', 'c-002.jpg', 250, 100, '超嫩牛排，超好吃'),
  ('c-003', '烤山豬肉', 'c-003.jpg', 150, 80, '超香烤山豬肉');
  
INSERT INTO `commodity` (`commodity_id`, `commodity_name`, `image`, `total_cost`, `price`, `describe`, `purchased`) VALUES
  ('c-001', '荷包蛋', 'c-001.jpg', 100, 15, '很香的荷包蛋，一度贊', 'y');
  
DROP TABLE IF EXISTS `commodity_textbook_detail`;
CREATE TABLE IF NOT EXISTS `commodity_textbook_detail`(
  `commodity_id` VARCHAR(20) NOT NULL,
  `ingrediate_id` VARCHAR(20) NOT NULL,
  `num` int(11) NOT NULL, 
  `cost` int(11) NOT NULL, 
  PRIMARY KEY (`commodity_id`, `ingrediate_id`),
  KEY `DB_commodity` (`commodity_id`),
  CONSTRAINT `DB_commodity` FOREIGN KEY (`commodity_id`) REFERENCES `commodity` (`commodity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

INSERT INTO `commodity_textbook_detail` (`commodity_id`, `ingrediate_id`, `num`, `cost`) VALUES
  ('c-001', 'i-001', 1, 10),
  ('c-002', 'i-001', 1, 10),
  ('c-002', 'i-004', 1, 30),
  ('c-003', 'i-003', 1, 20);

DROP TABLE IF EXISTS `customer`;
CREATE TABLE IF NOT EXISTS `customer` (
  `customer_id` varchar(20) NOT NULL,
  `customer_name` varchar(150) NOT NULL,
  `customer_address` varchar(250) DEFAULT NULL,
  `customer_phone` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

INSERT INTO `customer` (`customer_id`, `customer_name`, `customer_address`, `customer_phone`) VALUES
	('cu-001', '王範例', '高雄市楠梓區大學路一號', '093256789'),
	('cu-002', '王範例2', '高雄市楠梓區大學路二號', '09356002');
	
DROP TABLE IF EXISTS `sale_order`;
CREATE TABLE IF NOT EXISTS `sale_order` (
  `order_id` varchar(20) NOT NULL,
  `date` datetime NOT NULL DEFAULT current_timestamp(),
  `total_income` double(22,0) NOT NULL DEFAULT 0,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

INSERT INTO `sale_order` (`order_id`, `date`, `total_income`) VALUES
	('so-001', '2021-05-04 22:54:47', 70),
	('so-002', '2021-05-04 22:55:19', 380);

DROP TABLE IF EXISTS `sale_order_detail`;
CREATE TABLE IF NOT EXISTS `sale_order_detail` (
  `order_id` varchar(20) NOT NULL,
  `customer_id` VARCHAR(20) NOT NULL,
  `commodity_id` varchar(20) NOT NULL,
  `quantity` int(11) NOT NULL,
  `total_income` int(11) DEFAULT NULL,
  PRIMARY KEY (`order_id`, `commodity_id`),
  KEY `KEY` (`order_id`),
  CONSTRAINT `FK_order_detail_sale_order` FOREIGN KEY (`order_id`) REFERENCES `sale_order` (`order_id`),
  KEY `KEY2` (`customer_id`),
  CONSTRAINT `FK_order_detail_customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  KEY `DB_commodity_id` (`commodity_id`),
  CONSTRAINT `DB_commodity_foreignKey` FOREIGN KEY (`commodity_id`) REFERENCES `commodity` (`commodity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

INSERT INTO `sale_order_detail` (`order_id`, `customer_id`, `commodity_id`, `quantity`, `total_income`) VALUES
	('so-001', 'cu-001', 'c-001', 1, 70),
	('so-002', 'cu-002', 'c-002', 1, 80),
	('so-002', 'cu-002', 'c-003', 3, 100);
  
DROP TABLE IF EXISTS `purchase_order`;
CREATE TABLE IF NOT EXISTS `purchase_order` (
  `order_id` varchar(20) NOT NULL,
  `date` datetime NOT NULL DEFAULT current_timestamp(),
  `ingrediate_id` varchar(150) NOT NULL,
  `quantity` INT(11) DEFAULT NULL,
  `total_cost` INT(11) NOT NULL,
  PRIMARY KEY (`order_id`),
  KEY `DB_ingrediate_id` (`ingrediate_id`),
  CONSTRAINT `DB_ingrediate_foreignKey` FOREIGN KEY (`ingrediate_id`) REFERENCES `ingrediate` (`ingrediate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

INSERT INTO `purchase_order` (`order_id`, `date`, `ingrediate_id`, `quantity`, `total_cost`) VALUES
	('po-001', '2021-05-04 22:54:47', 'i-001', 10, 100),
	('po-002', '2021-05-04 22:54:47', 'i-001', 10, 100),
	('po-003', '2021-05-04 22:54:47', 'i-001', 10, 100);
