# triple-api

트리플여행자 클럽 마일리지 서비스 api 구현





## DB

Mybatis + MySQL 연동

~~~xml
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/mileage
spring.datasource.username=triple
spring.datasource.password=12345678
~~~



**DB 개념적 설계**

전체적인 프로세서 구상
API 작성시 필요한 DB는 사용자DB, 리뷰이벤트DB

![image-20220626215834617](README-images/image-20220626215834617.png)

![image-20220626215706141](README-images/image-20220626215706141.png)

**DB 논리적 설계**

리뷰 이벤트 릴레이션

| <u>id</u> | action | review_id | content | attachedPhotoIds | user_id | place_id |
| --------- | ------ | --------- | ------- | ---------------- | ------- | -------- |

유저 릴레이션

| <u>user_id</u> | point | ...  |
| -------------- | ----- | ---- |



**DDL 작성**

~~~sql
DROP TABLE IF EXISTS REVIEW_TB CASCADE;
DROP TABLE IF EXISTS EVENT_TB CASCADE;
DROP TABLE IF EXISTS USER_TB CASCADE;
DROP TABLE IF EXISTS PLACE_TB CASCADE;

#PLACE_TB
CREATE TABLE EVENT_TB (
  place_id VARCHAR(36) NOT NULL
);
#USER_TB
CREATE TABLE USER_TB (
	user_id VARCHAR(36) NOT NULL,
  point int NOT NULL DEFAULT 0
);
#REVIEW_TB
CREATE TABLE REVIEW_TB (
	review_id VARCHAR(36) NOT NULL,
  content VARCHAR(2000) NOT NULL,
  photo_url VARCHAR(2000),
  user_id VARCHAR(36) NOT NULL,
  place_id VARCHAR(36) NOT NULL
);
#PHOTO_TB
CREATE TABLE PHOTO_TB (
	photo_id VARCHAR(36) NOT NULL,
  photo_url VARCHAR(100) NOT NULL,
  user_id VARCHAR(36) NOT NULL
);

#Primary Key
ALTER TABLE PLACE_TB ADD CONSTRAINT PLACE_PK PRIMARY KEY(place_id);
ALTER TABLE USER_TB ADD CONSTRAINT USER_PK PRIMARY KEY(user_id);
ALTER TABLE REVIEW_TB ADD CONSTRAINT REVIEW_PK PRIMARY KEY(review_id);
ALTER TABLE PHOTO_TB ADD CONSTRAINT PHOTO_PK PRIMARY KEY(photo_id);

#Foreign Key
ALTER TABLE REVIEW_TB ADD CONSTRAINT REVIEW_USER_ID_FK FOREIGN KEY (user_id) REFERENCES USER_TB (user_id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE REVIEW_TB ADD CONSTRAINT REVIEW_PLACE_ID_FK FOREIGN KEY (place_id) REFERENCES PLACE_TB (place_id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE PHOTO_TB ADD CONSTRAINT PHOTO_USER_ID_FK FOREIGN KEY (user_id) REFERENCES USER_TB (user_id) ON DELETE CASCADE ON UPDATE CASCADE;
~~~

