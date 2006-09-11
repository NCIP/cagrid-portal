
drop database IF EXISTS grid;
create database grid;
USE grid
;

DROP TABLE IF EXISTS GRID_USERS
;

CREATE TABLE GRID_USERS ( 
	id INTEGER AUTO_INCREMENT  NOT NULL,
	userid VARCHAR(50),
	password VARCHAR(50),
	first_name VARCHAR(50),
	last_name VARCHAR(50),
	email_id VARCHAR(200),
	PRIMARY KEY(id)
) 
;

ALTER TABLE GRID_USERS
ADD CONSTRAINT UQ_GRID_USERS_userid UNIQUE (userid)
;


insert into GRID_USERS( userid, password, first_name, last_name, email_id) values ("user1","password1", "firstName_1","lastName_1","first.last@zzz.com");
insert into GRID_USERS( userid, password, first_name, last_name, email_id) values ("user2","password2", null,"lastName_2","first2.last@zzz.com");



