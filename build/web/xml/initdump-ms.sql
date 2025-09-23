CREATE TABLE S_USERS
(
  USER_ID         VARCHAR(32)             NOT NULL,
  EMAIL_ADDRESS   VARCHAR(100 ),
  PHONE           VARCHAR(50 ),
  USER_NAME       VARCHAR(50 ),
  PASSWORD        VARCHAR(32 ),
  DIVISION        VARCHAR(20 ),
  DEPARTMENT      VARCHAR(20 ),
  CONTACT_NUMBER  VARCHAR(20 ),
  ACTIVE_DATE     datetime,
  INACTIVE_DATE   datetime,
  LAST_LOGIN      datetime,
  CREATE_WHO      VARCHAR(32 )             NOT NULL,
  CREATE_DATE     datetime                          NOT NULL,
  CHANGE_WHO      VARCHAR(32 ),
  CHANGE_DATE     datetime,
  MOBILE_NUMBER   VARCHAR(16 ),
  CONSTRAINT PK_S_USERS PRIMARY KEY (USER_ID)
) ;

CREATE TABLE S_FUNCTIONS
(
  FUNCTION_ID    VARCHAR(50 )              NOT NULL,
  FUNCTION_NAME  VARCHAR(50 ),
  CTL_ID         VARCHAR(64 ),
  URL            VARCHAR(128 ),
  RESOURCE_ID    VARCHAR(32 ),
  ACTIVE_DATE    datetime,
  INACTIVE_DATE  datetime,
  CONSTRAINT PK_S_FUNCTIONS PRIMARY KEY (FUNCTION_ID)
) ;

CREATE TABLE S_FUNC_ROLES
(
  ROLE_ID      VARCHAR(32 )                NOT NULL,
  FUNCTION_ID  VARCHAR(32 )                NOT NULL,
  CREATE_WHO   VARCHAR(32 )                NOT NULL,
  CREATE_DATE  datetime                             NOT NULL,
  CHANGE_WHO   VARCHAR(32 ),
  CHANGE_DATE  datetime,
  CONSTRAINT PK_S_FUNC_ROLES PRIMARY KEY (ROLE_ID, FUNCTION_ID)
) ;

CREATE TABLE S_PARAMETER
(
  PARAM_ID          VARCHAR(50 )           NOT NULL,
  PARAM_SEQ         NUMERIC(4)                   NOT NULL,
  VALUE_DATE        datetime,
  VALUE_STRING      VARCHAR(255 ),
  VALUE_NUMBER      NUMERIC,
  LAST_UPDATE_DATE  datetime,
  PARAM_DESC        VARCHAR(120 )          NOT NULL,
  PARAM_TYPE        VARCHAR(10 )           NOT NULL,
  PARAM_GROUP       VARCHAR(20 )           NOT NULL,
  PARAM_ORDER       NUMERIC(8)                   NOT NULL,
  PARAM_MODE        VARCHAR(10 ),
  CONSTRAINT PK_S_PARAMETER PRIMARY KEY (PARAM_ID, PARAM_SEQ)
) ;

CREATE TABLE S_USER_ROLES
(
  ROLE_ID      VARCHAR(32 )                NOT NULL,
  USER_ID      VARCHAR(32 )                NOT NULL,
  CREATE_WHO   VARCHAR(32 )                NOT NULL,
  CREATE_DATE  datetime                             NOT NULL,
  CHANGE_WHO   VARCHAR(32 ),
  CHANGE_DATE  datetime,
  CONSTRAINT PK_S_USER_ROLES PRIMARY KEY (ROLE_ID, USER_ID)
);

CREATE TABLE IDMASTER
(
  IDPREFIX    VARCHAR(32 )                 NOT NULL,
  IDSEQUENCE  numeric,
  CONSTRAINT PK_ID_MASTER PRIMARY KEY (IDPREFIX)
) ;

insert into s_users(user_id,user_name,create_who,create_date) values('admin','admin','admin',getdate());
                                                                                                        
CREATE TABLE S_ROLES
(
  ROLE_ID        VARCHAR(32 )              NOT NULL,
  ROLE_NAME      VARCHAR(50 ),
  ACTIVE_DATE    datetime,
  INACTIVE_DATE  datetime,
  CREATE_WHO     VARCHAR(32 )              NOT NULL,
  CREATE_DATE    datetime                           NOT NULL,
  CHANGE_WHO     VARCHAR(32 ),
  CHANGE_DATE    datetime,
  CONSTRAINT PK_S_ROLES PRIMARY KEY (ROLE_ID)
);

CREATE TABLE gl_acct_type
(
  accttype varchar(5) NOT NULL,
  description varchar(32) NOT NULL,
  balancetype varchar(5),
  create_date datetime NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date datetime,
  change_who varchar(32),
  CONSTRAINT accttype_pk PRIMARY KEY (accttype)
) ;

insert into gl_acct_type values('A1','Assets - Fixed','DB',getdate(),'admin',null,null);
insert into gl_acct_type values('A2','Assets - Current','DB',getdate(),'admin',null,null);
insert into gl_acct_type values('A3','Assets - Contra','CR',getdate(),'admin',null,null);
insert into gl_acct_type values('E1','Equity','CR',getdate(),'admin',null,null);
insert into gl_acct_type values('E2','Equity - Contra','DB',getdate(),'admin',null,null);
insert into gl_acct_type values('E3','Equity - Other','CR',getdate(),'admin',null,null);
insert into gl_acct_type values('E4','Equity - Profit/Loss',NULL,getdate(),'admin',null,null);
insert into gl_acct_type values('L1','Liability - Current','CR',getdate(),'admin',null,null);
insert into gl_acct_type values('L2','Liability - Longterm','CR',getdate(),'admin',null,null);
insert into gl_acct_type values('L3','Liability - Other','CR',getdate(),'admin',null,null);
insert into gl_acct_type values('L4','Liability - Contra','DB',getdate(),'admin',null,null);
insert into gl_acct_type values('R1','Revenue','CR',getdate(),'admin',null,null);
insert into gl_acct_type values('R2','Revenue - Non Cash','CR',getdate(),'admin',null,null);
insert into gl_acct_type values('R3','Revenue - Contra','DB',getdate(),'admin',null,null);
insert into gl_acct_type values('X1','Expense','DB',getdate(),'admin',null,null);
insert into gl_acct_type values('X2','Expense - Deprec.','DB',getdate(),'admin',null,null);
insert into gl_acct_type values('X3','Expense - Amort.','DB',getdate(),'admin',null,null);
insert into gl_acct_type values('X4','Expense - Non Cash.','DB',getdate(),'admin',null,null);


CREATE TABLE gl_accounts
(
  account_id numeric(8) NOT NULL,
  accountno varchar(32) NOT NULL,
  acctype varchar(5) NOT NULL,
  allocated_flag varchar(1),
  bal_open numeric,
  create_date datetime NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date datetime,
  change_who varchar(32),
  CONSTRAINT accountid_pk PRIMARY KEY (account_id)
)
;

CREATE TABLE gl_dept
(
  dept_code varchar(8) NOT NULL,
  description varchar(128) NOT NULL,
  create_date datetime NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date datetime,
  change_who varchar(32),
  CONSTRAINT dept_pk PRIMARY KEY (dept_code)
)
;

CREATE TABLE gl_je_detail
(
  trx_id numeric(8) NOT NULL,
  debit numeric,
  credit numeric,
  accountid numeric(8),
  description varchar(128),
  applydate datetime,
  journal_code varchar(5),
  fiscal_year varchar(8),
  period_no numeric(4),
  create_date datetime NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date datetime,
  change_who varchar(32)
)
;

CREATE TABLE gl_journal_master
(
  journal_code varchar(5) NOT NULL,
  description varchar(128),
  journal_type varchar(5) NOT NULL,
  journal_freq varchar(5) NOT NULL,
  enddate datetime,
  lastposted datetime,
  create_date datetime NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date datetime,
  change_who varchar(32)
)
;

CREATE TABLE gl_period
(
  gl_period_id numeric(8) NOT NULL,
  fiscal_year varchar(5),
  period_num numeric(4),
  create_date datetime NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date datetime,
  change_who varchar(32),
  CONSTRAINT gl_period_id_pk PRIMARY KEY (gl_period_id)
)
;

CREATE TABLE gl_period_det
(
  gl_period_id numeric(8) NOT NULL,
  period_no numeric(4) NOT NULL,
  startdate datetime,
  enddate datetime,
  create_date datetime NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date datetime,
  change_who varchar(32),
  CONSTRAINT gl_period_det_pk PRIMARY KEY (gl_period_id, period_no)
)
;

ALTER TABLE gl_accounts ADD description varchar(128);

ALTER TABLE gl_je_detail ADD trx_no varchar(64) NOT NULL;