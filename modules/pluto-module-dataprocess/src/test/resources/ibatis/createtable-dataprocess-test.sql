
drop sequence HS if exists;
create sequence HS;
drop table TESTB if exists;
drop table TESTA if exists;
drop table TESTC if exists;
CREATE TABLE TESTA
   (	ID INTEGER NOT NULL, 
	NAME VARCHAR(255), 
	DOUBLE_VALUE DOUBLE, 
	 PRIMARY KEY (ID)
   ) ;
  

CREATE TABLE TESTB
   (	ID INTEGER NOT NULL, 
	NAME VARCHAR(255), 
	TESTA_ID INTEGER NOT NULL,
	 PRIMARY KEY (ID)
   ) ;


CREATE TABLE TESTC
   (	ID INTEGER NOT NULL, 
	NAME VARCHAR(255), 
	 PRIMARY KEY (ID)
   ) ;
   
alter table TESTB add constraint TESTB_FK1 foreign key(TESTA_ID) references TESTA (ID);