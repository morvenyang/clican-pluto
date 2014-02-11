insert into quiz_user(ACCOUNT,PASSWORD,REAL_NAME,ROLE,CREATE_TIME,ACTIVE) VALUES('admin','7c4a8d09ca3762af61e59520943dc26494f8941b','admin',2,CURRENT_TIME(),1);
insert into quiz_user(ACCOUNT,PASSWORD,REAL_NAME,ROLE,CREATE_TIME,ACTIVE) VALUES('teacher01','7c4a8d09ca3762af61e59520943dc26494f8941b','teacher01',1,CURRENT_TIME(),1);

insert into learning_point(POINT,SUB_POINT) VALUES('English entry','Pronunciation');
insert into learning_point(POINT,SUB_POINT) VALUES('English entry','Alphabet');
insert into learning_point(POINT,SUB_POINT) VALUES('English entry','Listening');

insert into learning_point(POINT,SUB_POINT) VALUES('Math','Concept');
insert into learning_point(POINT,SUB_POINT) VALUES('Math','Basic operation');
insert into learning_point(POINT,SUB_POINT) VALUES('Math','Application');

insert into learning_point(POINT,SUB_POINT) VALUES('Logical','Judgment');
insert into learning_point(POINT,SUB_POINT) VALUES('Logical','Analysis');
insert into learning_point(POINT,SUB_POINT) VALUES('Logical','Compare');

insert into template(ID,NAME,SAMPLE_PICTURE,DESC_PICTURE,IPHONE,IPAD) VALUES(1,'Matching','/template/matching_sample.png','/template/matching_desc.png',1,1);
insert into template(ID,NAME,SAMPLE_PICTURE,DESC_PICTURE,IPHONE,IPAD) VALUES(2,'Bingo','/template/bingo_sample.png','/template/bingo_desc.png',1,1);
insert into template(ID,NAME,SAMPLE_PICTURE,DESC_PICTURE,IPHONE,IPAD) VALUES(3,'MultiChoice1','/template/multiChoice_sample.png','/template/multiChoice_desc.png',1,1);
insert into template(ID,NAME,SAMPLE_PICTURE,DESC_PICTURE,IPHONE,IPAD) VALUES(4,'MultiChoice2','/template/multiChoice_sample.png','/template/multiChoice_desc.png',1,1);
insert into template(ID,NAME,SAMPLE_PICTURE,DESC_PICTURE,IPHONE,IPAD) VALUES(5,'MultiChoice3','/template/multiChoice_sample.png','/template/multiChoice_desc.png',1,1);