insert into QUIZ_USER(ACCOUNT,PASSWORD,REAL_NAME,ROLE,CREATE_TIME) VALUES('admin','7c4a8d09ca3762af61e59520943dc26494f8941b','admin',2,CURRENT_TIME());
insert into QUIZ_USER(ACCOUNT,PASSWORD,REAL_NAME,ROLE,CREATE_TIME) VALUES('teacher01','7c4a8d09ca3762af61e59520943dc26494f8941b','teacher01',1,CURRENT_TIME());

insert into LEARNING_POINT(POINT,SUB_POINT) VALUES('English entry','Pronunciation');
insert into LEARNING_POINT(POINT,SUB_POINT) VALUES('English entry','Alphabet');
insert into LEARNING_POINT(POINT,SUB_POINT) VALUES('English entry','Listening');

insert into LEARNING_POINT(POINT,SUB_POINT) VALUES('Math','Concept');
insert into LEARNING_POINT(POINT,SUB_POINT) VALUES('Math','Basic operation');
insert into LEARNING_POINT(POINT,SUB_POINT) VALUES('Math','Application');

insert into LEARNING_POINT(POINT,SUB_POINT) VALUES('Logical','Judgment');
insert into LEARNING_POINT(POINT,SUB_POINT) VALUES('Logical','Analysis');
insert into LEARNING_POINT(POINT,SUB_POINT) VALUES('Logical','Compare');

insert into TEMPLATE(ID,NAME,SAMPLE_PICTURE,DESC_PICTURE,IPHONE,IPAD) VALUES(1,'Matching','/template/matching_sample.png','/template/matching_desc.png',1,1);
insert into TEMPLATE(ID,NAME,SAMPLE_PICTURE,DESC_PICTURE,IPHONE,IPAD) VALUES(2,'Bingo','/template/bingo_sample.png','/template/bingo_desc.png',1,1);