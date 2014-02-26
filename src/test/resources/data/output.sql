CREATE TABLE PROJECT
(
    PROJECT_ID                      VARCHAR2(128) NOT NULL,
    PROJECT_NM                      VARCHAR2(256),
    PROJECT_TYPE                    VARCHAR2(128),
    PROJECT_CLASS                   VARCHAR2(128),
    PROJECT_START_DATE              DATE,
    PROJECT_END_DATE                DATE,
    CUSTOMER_ID                     VARCHAR2(128),
    PM                              VARCHAR2(128),
    PL                              VARCHAR2(128),
    NOTE_FREE                       VARCHAR2(512)
)
/
ALTER TABLE PROJECT
    ADD(CONSTRAINT PK_PROJECT PRIMARY KEY (PROJECT_ID) USING INDEX)
/
CREATE TABLE CUSTOMER
(
    CUSTOMER_ID                     VARCHAR2(128) NOT NULL,
    CUSTOMER_NM                     VARCHAR2(256),
    JOB_TYPE                        VARCHAR2(128)
)
/
ALTER TABLE CUSTOMER
    ADD(CONSTRAINT PK_CUSTOMER PRIMARY KEY (CUSTOMER_ID) USING INDEX)
/
