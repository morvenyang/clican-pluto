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

insert into template(ID,NAME,SAMPLE_PICTURE,DESC_PICTURE,IPHONE,IPAD) VALUES(1,'Matching','/template/matching_sample.jpg','/template/matching_desc.jpg',1,1);
insert into template(ID,NAME,SAMPLE_PICTURE,DESC_PICTURE,IPHONE,IPAD) VALUES(2,'Bingo','/template/bingo_sample.jpg','/template/bingo_desc.jpg',1,1);
insert into template(ID,NAME,SAMPLE_PICTURE,DESC_PICTURE,IPHONE,IPAD) VALUES(3,'MultiChoice1','/template/multiChoice1_sample.jpg','/template/multiChoice1_desc.jpg',1,1);
insert into template(ID,NAME,SAMPLE_PICTURE,DESC_PICTURE,IPHONE,IPAD) VALUES(4,'MultiChoice2','/template/multiChoice2_sample.jpg','/template/multiChoice2_desc.jpg',1,1);
insert into template(ID,NAME,SAMPLE_PICTURE,DESC_PICTURE,IPHONE,IPAD) VALUES(5,'MultiChoice3','/template/multiChoice3_sample.jpg','/template/multiChoice3_desc.jpg',1,1);
insert into template(ID,NAME,SAMPLE_PICTURE,DESC_PICTURE,IPHONE,IPAD) VALUES(6,'CatchItWord','/template/catchItWord_sample.jpg','/template/catchItWord_desc.jpg',1,1);
insert into template(ID,NAME,SAMPLE_PICTURE,DESC_PICTURE,IPHONE,IPAD) VALUES(7,'CatchItSentence','/template/catchItSentence_sample.jpg','/template/catchItSentence_desc.jpg',1,1);