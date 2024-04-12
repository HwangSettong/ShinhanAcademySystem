## DB 연동 실습과제

<aside>
💡 Java와 DataBase 연동한 프로그램 구현

</aside>

## 요구사항

- 회원 Entity를 포함한 최소 2개 이상의 Entity 설계
- 최소 두 개의 Entity는 Relation을 가지고 있음
- 기능은 기본적인 CRUD 기능을 가지고 있음
- 회원과 관련된 주제는 본인(팀원)과 협의 후 아이디어 선정하여 개발
- 객체지향, 자료구조, 디자인패턴, 예외처리 등 기본 기능 외 추가하거나 보완사항 발표내용 포함
- 주제 및 기능정의, 설계외 협업과정과 협의내용등도 발표 포함

## 프로젝트 주제

신한 DS SW AC 시스템

## 기능 정의

- 메인
    
    학생, 강사 로그인/로그아웃 가능해야함
    
- 학생
    - 시험 성적 조회
    - 입실 기록
    - 퇴실 기록
    - 총 출석시간 조회
- 강사
    - 학생 조회
    - 시험 성적 입력
    - 시험 성적 조회

## ERD

![erd](https://file.notion.so/f/f/e28b4bbd-4167-4e81-b79e-2543bebfef8d/c6e6a80e-c149-4336-abc7-6dbbf45a26c6/Untitled.png?id=bd019c57-6a4a-425d-9837-c63ea1696394&table=block&spaceId=e28b4bbd-4167-4e81-b79e-2543bebfef8d&expirationTimestamp=1713052800000&signature=om6Hfx1PPkaY9074yeX8mLVPm0hFq3LT8tG15W9HDXY&downloadName=Untitled.png)

- 테이블 정리
    - 회원(학생)
        - 학생id
        - 학생이름
        - 총 출석 시간 조회
        - 강사id
    - 강사
        - 강사id
        - 강사명
    - 출결
        - 출결id
        - 학생id
        - 입실시간
        - 퇴실시간
    - 시험성적
        - 시험id
        - 테스트명
        - 학생id
        - 점수
- 테이블 및 데이터 생성
    
    테이블 생성
    
    ```java
    CREATE TABLE student (
    	std_id	varchar2(20)		NOT NULL,
    	std_pw	varchar2(20)		NOT NULL,
    	tc_id	varchar2(20)		NULL,
    	std_name	varchar2(20)		NULL,
    	attendance_time	number(10)		NULL
    );
    
    CREATE TABLE teacher (
    	tc_id	varchar2(20)		NOT NULL,
    	tc_pw	varchar2(20)		NOT NULL,
    	tc_name	varchar2(20)		NULL
    );
    
    CREATE TABLE attendance (
    	atd_id	number(10)		NOT NULL,
    	std_id	varchar2(20)		NOT NULL,
    	checkin_time	date		NULL,
    	checkout_time	date		NULL
    );
    
    CREATE TABLE test (
    	test_id	number(10)		NOT NULL,
    	std_id	varchar2(20)		NOT NULL,
    	test_name	varchar2(20)		NULL,
    	score	number(10)		NULL
    );
    
    ALTER TABLE student ADD CONSTRAINT PK_STUDENT PRIMARY KEY (
    	std_id
    );
    
    ALTER TABLE teacher ADD CONSTRAINT PK_TEACHER PRIMARY KEY (
    	tc_id
    );
    
    ALTER TABLE attendance ADD CONSTRAINT PK_ATTENDANCE PRIMARY KEY (
    	atd_id
    );
    
    ALTER TABLE test ADD CONSTRAINT PK_TEST PRIMARY KEY (
    	test_id
    );
    
    ALTER TABLE student ADD CONSTRAINT FK_teacher_TO_student_1 FOREIGN KEY (
    	tc_id
    )
    REFERENCES teacher (
    	tc_id
    );
    
    ALTER TABLE Attendance ADD CONSTRAINT FK_student_TO_Attendance_1 FOREIGN KEY (
    	std_id
    )
    REFERENCES student (
    	std_id
    );
    
    ALTER TABLE test ADD CONSTRAINT FK_student_TO_test_1 FOREIGN KEY (
    	std_id
    )
    REFERENCES student (
    	std_id
    );
    
    CREATE SEQUENCE SEQ_AID NOCACHE;
    CREATE SEQUENCE SEQ_TID NOCACHE;
    ```
    
    데이터 삽입
    
    - 강사테이블
        
        ```sql
        INSERT INTO TEACHER(tc_id, tc_name, tc_pw)
        values('teacher01', '서민구', 'tc1234');
        INSERT INTO TEACHER(tc_id, tc_name, tc_pw)
        values('teacher02', '정진', 'tc1234');
        ```
        
    - 학생테이블
        
        ```sql
        INSERT INTO STUDENT VALUES ('std1', 'std1234', 'teacher01', '윤해빈', 720);
        INSERT INTO STUDENT VALUES ('std2', 'std1234', 'teacher01', '황세현', 720);
        INSERT INTO STUDENT VALUES ('std3', 'std1234', 'teacher02', '홍길동', 600);
        INSERT INTO STUDENT VALUES ('std4', 'std1234', 'teacher02', '이순신', 700);
        ```
        
    - 시험성적 테이블
        
        ```sql
        INSERT INTO TEST VALUES (seq_tid.nextval, 'std1', '자바', 80);
        INSERT INTO TEST VALUES (seq_tid.nextval, 'std2', '자바', 90);
        INSERT INTO TEST VALUES (seq_tid.nextval, 'std3', '데이터베이스', 100);
        INSERT INTO TEST VALUES (seq_tid.nextval, 'std4', '자바', 50);
        INSERT INTO TEST VALUES (seq_tid.nextval, 'std4', '데이터베이스', 80);
        ```
        
    - 입/퇴실 테이블
        
        ```sql
        INSERT INTO attendance VALUES(seq_aid.nextval, 'std1', to_date('20240408 09:01:00', 'YYYYMMDD HH24:MI:SS'), to_date('20240408 18:04:00', 'YYYYMMDD HH24:MI:SS'));
        INSERT INTO attendance VALUES(seq_aid.nextval, 'std1', to_date('20240409 09:00:00', 'YYYYMMDD HH24:MI:SS'), to_date('20240409 17:59:00', 'YYYYMMDD HH24:MI:SS'));
        INSERT INTO attendance VALUES(seq_aid.nextval, 'std2', to_date('20240408 09:03:00', 'YYYYMMDD HH24:MI:SS'), to_date('20240408 17:53:00', 'YYYYMMDD HH24:MI:SS'));
        INSERT INTO attendance VALUES(seq_aid.nextval, 'std2', to_date('20240409 08:50:00', 'YYYYMMDD HH24:MI:SS'), to_date('20240409 17:50:00', 'YYYYMMDD HH24:MI:SS'));
        INSERT INTO attendance VALUES(seq_aid.nextval, 'std3', to_date('20240408 09:00:00', 'YYYYMMDD HH24:MI:SS'), to_date('20240408 17:55:00', 'YYYYMMDD HH24:MI:SS'));
        INSERT INTO attendance VALUES(seq_aid.nextval, 'std3', to_date('20240409 08:00:00', 'YYYYMMDD HH24:MI:SS'), to_date('20240409 18:00:00', 'YYYYMMDD HH24:MI:SS'));
        INSERT INTO attendance VALUES(seq_aid.nextval, 'std4', to_date('20240408 09:00:00', 'YYYYMMDD HH24:MI:SS'), to_date('20240408 17:57:00', 'YYYYMMDD HH24:MI:SS'));
        INSERT INTO attendance VALUES(seq_aid.nextval, 'std4', to_date('20240409 09:08:00', 'YYYYMMDD HH24:MI:SS'), to_date('20240409 18:01:00', 'YYYYMMDD HH24:MI:SS'));
        ```
        

## 코드구조

![java](https://file.notion.so/f/f/e28b4bbd-4167-4e81-b79e-2543bebfef8d/311daa8f-d59e-44ab-ae8f-68bc85c142a0/Untitled.png?id=f73bb4c0-83e7-4c5d-a470-f0045066d99c&table=block&spaceId=e28b4bbd-4167-4e81-b79e-2543bebfef8d&expirationTimestamp=1713052800000&signature=UDVXJBCwQCial3-EUwmJpEXrT6shtXsPOXkfrVt02HM&downloadName=Untitled.png)
