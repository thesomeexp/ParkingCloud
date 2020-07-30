-- SET NAMES utf8mb4;
-- SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS parking;
CREATE DATABASE parking DEFAULT CHARACTER SET utf8mb4;
use parking;

CREATE TABLE user (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(64) NOT NULL,
  password varchar(64) NOT NULL,
  salt varchar(64) NOT NULL,
  phone varchar(64) NOT NULL UNIQUE,
  role varchar(64) NOT NULL,
  score int NOT NULL,
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE info (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(64) NOT NULL,
  content varchar(4000) DEFAULT NULL,
  longitude double NOT NULL,
  latitude double NOT NULL,
  geoHash varchar(64) NOT NULL,
  uid int(11) NOT NULL,
  state varchar(64) NOT NULL,
  infoSubmitDate DATETIME NOT NULL,
  stateUpdateDate DATETIME NOT NULL,
  t0 int NOT NULL,
  t1 int NOT NULL,
  t2 int NOT NULL,
  t3 int NOT NULL,
  t4 int NOT NULL,
  t5 int NOT NULL,
  t6 int NOT NULL,
  t7 int NOT NULL,
  t8 int NOT NULL,
  t9 int NOT NULL,
  t10 int NOT NULL,
  t11 int NOT NULL,
  t12 int NOT NULL,
  t13 int NOT NULL,
  t14 int NOT NULL,
  t15 int NOT NULL,
  t16 int NOT NULL,
  t17 int NOT NULL,
  t18 int NOT NULL,
  t19 int NOT NULL,
  t20 int NOT NULL,
  t21 int NOT NULL,
  t22 int NOT NULL,
  t23 int NOT NULL,
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE review (
  id int(11) NOT NULL AUTO_INCREMENT,
  uid int(11) NOT NULL,
  pid int(11) NOT NULL,
  submitDate  timestamp NOT NULL,
  accuracy int(11) NOT NULL,
  easyToFind int(11) NOT NULL,
  content varchar(4000) NOT NULL,
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE temp (
  id int(11) NOT NULL AUTO_INCREMENT,
  uid int(11) NOT NULL,
  pid int(11) NOT NULL,
  submitDate  timestamp NOT NULL,
  state int NOT NULL,
  useful int NOT NULL,
  bad int NOT NULL,
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE tempUser (
  id int(11) NOT NULL AUTO_INCREMENT,
  uid int(11) NOT NULL,
  tid int(11) NOT NULL,
  useful int NOT NULL,
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE infoImage (
  id int(11) NOT NULL AUTO_INCREMENT,
  pid int(11) NOT NULL,
  uid int(11) NOT NULL,
  submitDate  timestamp NOT NULL,
  state varchar(64) NOT NULL,
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;






