
/*
create database CustomerManagement;
use CustomerManagement;
*/

CREATE TABLE Customer (
	cusId VARCHAR(20),
	cusName   VARCHAR(10),
	age      INTEGER,
	rank       VARCHAR(10),
	job        VARCHAR(20),
	reserve    INTEGER,
	PRIMARY KEY(cusId)
);

CREATE TABLE Product (
	prodNo  CHAR(3),
	prodName    VARCHAR(20),
	invertory    INTEGER,
	price      INTEGER,
	manufacturer  VARCHAR(20),
	PRIMARY KEY(prodNo),
	CHECK (invertory >= 0)
);

CREATE TABLE CusOrder (
	orderNo  CHAR(3),
	orderCus  VARCHAR(20),
	orderPro  CHAR(3),
	quantity      INTEGER,
	address VARCHAR(30),
	orderDate  DATE,
	PRIMARY KEY(orderNo),
	FOREIGN KEY(orderCus) REFERENCES Customer(cusId),
	FOREIGN KEY(orderPro) REFERENCES Product(prodNo)
);

INSERT INTO Customer VALUES ('apple', 'Jung', 20, 'gold', 'student', 1000);
INSERT INTO Customer VALUES ('banana', 'Kim', 25, 'vip', 'nurse', 2500);
INSERT INTO Customer VALUES ('carrot', 'Ko', 28, 'gold', 'teacher', 4500);
INSERT INTO Customer VALUES ('orange', 'Kim', 22, 'silver', 'student', 0);
INSERT INTO Customer VALUES ('melon', 'Sung', 35, 'gold', 'employee', 5000);
INSERT INTO Customer VALUES ('pear', 'Chea', 31, 'silver', 'employee', 500);
INSERT INTO Customer VALUES ('peach', 'Oh', NULL, 'silver', 'doctor', 300);


INSERT INTO Product VALUES ('p01', 'Dumpling', 5000, 4500, 'Daehan food');
INSERT INTO Product VALUES ('p02', 'Noodles', 2500, 5500, 'MinKook food');
INSERT INTO Product VALUES ('p03', 'Pie', 3600, 2600, 'Hanbit bakery');
INSERT INTO Product VALUES ('p04', 'Chocolate', 1250, 2500, 'Hanbit bakery');
INSERT INTO Product VALUES ('p05', 'Ramen', 2200, 1200, 'Daehan food');
INSERT INTO Product VALUES ('p06', 'Udon', 1000, 1550, 'MinKook food');
INSERT INTO Product VALUES ('p07', 'Biscuit', 1650, 1500, 'Hanbit bakery');


INSERT INTO CusOrder VALUES ('o01', 'apple', 'p03', 10, 'Seoul Mapo', '13/01/01');
INSERT INTO CusOrder VALUES ('o02', 'melon', 'p01', 5, 'Incheon Gyeyang', '13/01/10');
INSERT INTO CusOrder VALUES ('o03', 'banana', 'p06', 45, 'Gyeonggi Bucheon', '13/01/11');
INSERT INTO CusOrder VALUES ('o04', 'carrot', 'p02', 8, 'Busan Geumjeong', '13/02/01');
INSERT INTO CusOrder VALUES ('o05', 'melon', 'p06', 36, 'Gyeonggi Yongin', '13/02/20');
INSERT INTO CusOrder VALUES ('o06', 'banana', 'p01', 19, 'Chungbuk Boeun', '13/03/02');
INSERT INTO CusOrder VALUES ('o07', 'apple', 'p03', 22, 'Seoul Yeongdeungpo', '13/03/15');
INSERT INTO CusOrder VALUES ('o08', 'pear', 'p02', 50, 'Gangwon Chuncheon', '13/04/10');
INSERT INTO CusOrder VALUES ('o09', 'banana', 'p04', 15, 'Jeonnam Mokpo', '13/04/11');
INSERT INTO CusOrder VALUES ('o10', 'carrot', 'p03', 20, 'Gyeonggi Anyang', '13/05/22');

