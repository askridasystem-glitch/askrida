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
  ACTIVE_DATE     timestamp,
  INACTIVE_DATE   timestamp,
  LAST_LOGIN      timestamp,
  CREATE_WHO      VARCHAR(32 )             NOT NULL,
  CREATE_DATE     timestamp                          NOT NULL,
  CHANGE_WHO      VARCHAR(32 ),
  CHANGE_DATE     timestamp,
  MOBILE_NUMBER   VARCHAR(16 ),
  CONSTRAINT PK_S_USERS PRIMARY KEY (USER_ID)
) without oids;

CREATE TABLE S_FUNCTIONS
(
  FUNCTION_ID    VARCHAR(50 )              NOT NULL,
  FUNCTION_NAME  VARCHAR(50 ),
  CTL_ID         VARCHAR(64 ),
  URL            VARCHAR(128 ),
  RESOURCE_ID    VARCHAR(32 ),
  ACTIVE_DATE    timestamp,
  INACTIVE_DATE  timestamp,
  CONSTRAINT PK_S_FUNCTIONS PRIMARY KEY (FUNCTION_ID)
) without oids;

CREATE TABLE S_FUNC_ROLES
(
  ROLE_ID      VARCHAR(32 )                NOT NULL,
  FUNCTION_ID  VARCHAR(32 )                NOT NULL,
  CREATE_WHO   VARCHAR(32 )                NOT NULL,
  CREATE_DATE  timestamp                             NOT NULL,
  CHANGE_WHO   VARCHAR(32 ),
  CHANGE_DATE  timestamp,
  CONSTRAINT PK_S_FUNC_ROLES PRIMARY KEY (ROLE_ID, FUNCTION_ID)
) without oids;

CREATE TABLE S_PARAMETER
(
  PARAM_ID          VARCHAR(50 )           NOT NULL,
  PARAM_SEQ         NUMERIC(4)                   NOT NULL,
  VALUE_DATE        timestamp,
  VALUE_STRING      VARCHAR(255 ),
  VALUE_NUMBER      NUMERIC,
  LAST_UPDATE_DATE  timestamp,
  PARAM_DESC        VARCHAR(120 )          NOT NULL,
  PARAM_TYPE        VARCHAR(10 )           NOT NULL,
  PARAM_GROUP       VARCHAR(20 )           NOT NULL,
  PARAM_ORDER       NUMERIC(8)                   NOT NULL,
  PARAM_MODE        VARCHAR(10 ),
  CONSTRAINT PK_S_PARAMETER PRIMARY KEY (PARAM_ID, PARAM_SEQ)
) without oids;

CREATE TABLE S_USER_ROLES
(
  ROLE_ID      VARCHAR(32 )                NOT NULL,
  USER_ID      VARCHAR(32 )                NOT NULL,
  CREATE_WHO   VARCHAR(32 )                NOT NULL,
  CREATE_DATE  timestamp                             NOT NULL,
  CHANGE_WHO   VARCHAR(32 ),
  CHANGE_DATE  timestamp,
  CONSTRAINT PK_S_USER_ROLES PRIMARY KEY (ROLE_ID, USER_ID)
)without oids;

CREATE TABLE IDMASTER
(
  IDPREFIX    VARCHAR(32 )                 NOT NULL,
  IDSEQUENCE  numeric,
  CONSTRAINT PK_ID_MASTER PRIMARY KEY (IDPREFIX)
) without oids;

insert into s_users(user_id,user_name,create_who,create_date) values('admin','admin','admin','now');
                                                                                                        
CREATE TABLE S_ROLES
(
  ROLE_ID        VARCHAR(32 )              NOT NULL,
  ROLE_NAME      VARCHAR(50 ),
  ACTIVE_DATE    timestamp,
  INACTIVE_DATE  timestamp,
  CREATE_WHO     VARCHAR(32 )              NOT NULL,
  CREATE_DATE    timestamp                           NOT NULL,
  CHANGE_WHO     VARCHAR(32 ),
  CHANGE_DATE    timestamp,
  CONSTRAINT PK_S_ROLES PRIMARY KEY (ROLE_ID)
);

CREATE TABLE gl_acct_type
(
  accttype varchar(5) NOT NULL,
  description varchar(32) NOT NULL,
  balancetype varchar(5),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT accttype_pk PRIMARY KEY (accttype)
) ;

insert into gl_acct_type values('A1','Assets - Fixed','DB','now','admin',null,null);
insert into gl_acct_type values('A2','Assets - Current','DB','now','admin',null,null);
insert into gl_acct_type values('A3','Assets - Contra','CR','now','admin',null,null);
insert into gl_acct_type values('E1','Equity','CR','now','admin',null,null);
insert into gl_acct_type values('E2','Equity - Contra','DB','now','admin',null,null);
insert into gl_acct_type values('E3','Equity - Other','CR','now','admin',null,null);
insert into gl_acct_type values('E4','Equity - Profit/Loss',NULL,'now','admin',null,null);
insert into gl_acct_type values('L1','Liability - Current','CR','now','admin',null,null);
insert into gl_acct_type values('L2','Liability - Longterm','CR','now','admin',null,null);
insert into gl_acct_type values('L3','Liability - Other','CR','now','admin',null,null);
insert into gl_acct_type values('L4','Liability - Contra','DB','now','admin',null,null);
insert into gl_acct_type values('R1','Revenue','CR','now','admin',null,null);
insert into gl_acct_type values('R2','Revenue - Non Cash','CR','now','admin',null,null);
insert into gl_acct_type values('R3','Revenue - Contra','DB','now','admin',null,null);
insert into gl_acct_type values('X1','Expense','DB','now','admin',null,null);
insert into gl_acct_type values('X2','Expense - Deprec.','DB','now','admin',null,null);
insert into gl_acct_type values('X3','Expense - Amort.','DB','now','admin',null,null);
insert into gl_acct_type values('X4','Expense - Non Cash.','DB','now','admin',null,null);


CREATE TABLE gl_accounts
(
  account_id int8 NOT NULL,
  accountno varchar(32) NOT NULL,
  acctype varchar(5) NOT NULL,
  allocated_flag varchar(1),
  bal_open numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT accountid_pk PRIMARY KEY (account_id)
)
WITHOUT OIDS;

CREATE TABLE gl_dept
(
  dept_code varchar(8) NOT NULL,
  description varchar(128) NOT NULL,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT dept_pk PRIMARY KEY (dept_code)
)
WITHOUT OIDS;

CREATE TABLE gl_je_detail
(
  trx_id int8 NOT NULL,
  debit numeric,
  credit numeric,
  accountid int8,
  description varchar(128),
  applydate timestamp,
  journal_code varchar(5),
  fiscal_year varchar(8),
  period_no int4,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32)
)
WITHOUT OIDS;

CREATE TABLE gl_journal_master
(
  journal_code varchar(5) NOT NULL,
  description varchar(128),
  journal_type varchar(5) NOT NULL,
  journal_freq varchar(5) NOT NULL,
  enddate timestamp,
  lastposted timestamp,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32)
)
WITHOUT OIDS;

CREATE TABLE gl_period
(
  gl_period_id int8 NOT NULL,
  fiscal_year varchar(5),
  period_num int4,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT gl_period_id_pk PRIMARY KEY (gl_period_id)
)
WITHOUT OIDS;

CREATE TABLE gl_period_det
(
  gl_period_id int8 NOT NULL,
  period_no int4 NOT NULL,
  startdate timestamp,
  enddate timestamp,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT gl_period_det_pk PRIMARY KEY (gl_period_id, period_no)
)
WITHOUT OIDS;

ALTER TABLE gl_accounts ADD COLUMN description varchar(128);

ALTER TABLE gl_je_detail ADD COLUMN trx_no varchar(64);
ALTER TABLE gl_je_detail ALTER COLUMN trx_no SET NOT NULL;

ALTER TABLE gl_period_det ADD COLUMN closed_flag varchar(1);

CREATE TABLE gl_acct_bal
(
  account_id int8 NOT NULL,
  gl_period_id int8 NOT NULL,
  bal numeric,
  CONSTRAINT gl_acct_bal_pk PRIMARY KEY (account_id, gl_period_id)
)
WITHOUT OIDS;

CREATE TABLE gl_rpt
(
  report_id varchar(32) NOT NULL,
  title varchar(255),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT gl_rpt_pk PRIMARY KEY (report_id)
)
WITHOUT OIDS;

CREATE TABLE gl_rpt_col
(
  gl_rpt_id varchar(32) NOT NULL,
  col_no int4,
  col_type varchar(8),
  gl_rpt_col_id int8 NOT NULL,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT gl_rpt_col_pk PRIMARY KEY (gl_rpt_col_id)
)
WITHOUT OIDS;

CREATE TABLE gl_rpt_lin
(
  gl_rpt_lin_id int8 NOT NULL,
  gl_rpt_id varchar(32) NOT NULL,
  line_no int8,
  line_type varchar(8),
  acct_from varchar(32),
  acct_to varchar(32),
  summarize_flag varchar(1),
  blank_line int4,
  description varchar(255),
  indent int4,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT gl_rpt_lin_pk PRIMARY KEY (gl_rpt_lin_id)
)
WITHOUT OIDS;

ALTER TABLE gl_je_detail ADD COLUMN period_id int4;

CREATE TABLE ap_invoice
(
  ap_invoice_id int8 NOT NULL,
  invoice_no varchar(64),
  amount numeric,
  ccy varchar(3),
  due_date timestamp,
  invoice_date timestamp,
  ap_vendor_id int8,
  posted_flag varchar(1),
  amount_settled numeric,
  cancel_flag varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ap_invoice_pk PRIMARY KEY (ap_invoice_id)
) without oids;

CREATE TABLE ap_vendor
(
  ap_vendor_id int8 NOT NULL,
  ent_id int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ap_vendor_pk PRIMARY KEY (ap_vendor_id)
) without oids;

CREATE TABLE ar_customer
(
  ar_cust_id int8 NOT NULL,
  ent_id int8,
  ar_cclass_id int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ar_customer_pk PRIMARY KEY (ar_cust_id)
) without oids;

CREATE TABLE ar_customer_class
(
  ar_cclass_id int8 NOT NULL,
  description varchar(255),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ar_customer_class_pk PRIMARY KEY (ar_cclass_id)
) without oids;

CREATE TABLE ar_invoice
(
  ar_invoice_id int8 NOT NULL,
  invoice_no varchar(64),
  amount numeric,
  invoice_date timestamp,
  due_date timestamp,
  amount_settled numeric,
  ccy varchar(3),
  ar_cust_id int8,
  posted_flag varchar(1),
  cancel_flag varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ar_invoice_pk PRIMARY KEY (ar_invoice_id)
) without oids;

CREATE TABLE ar_payment_method
(
  pmt_method_id int8 NOT NULL,
  rc_id int8,
  gl_acct_id int8,
  description varchar(255),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ar_payment_method_pk PRIMARY KEY (pmt_method_id)
) without oids;

CREATE TABLE ar_receipt
(
  ar_receipt_id int8 NOT NULL,
  receipt_no varchar(64),
  amount numeric,
  ccy varchar(3),
  posted_flag varchar(1),
  pmt_method_id int8,
  cancel_flag varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ar_receipt_pk PRIMARY KEY (ar_receipt_id)
) without oids;

CREATE TABLE ar_receipt_class
(
  rc_id int8 NOT NULL,
  description varchar(255),
  active_flag varchar(1),
  remit_flag varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ar_receipt_class_pk PRIMARY KEY (rc_id)
) without oids;

CREATE TABLE ar_receipt_lines
(
  ar_rcl_id int8 NOT NULL,
  ar_invoice_id int8,
  amount numeric,
  receipt_id int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ar_receipt_lines_pk PRIMARY KEY (ar_rcl_id)
) without oids;

CREATE TABLE ent_address
(
  ent_addr_id int8 NOT NULL,
  ent_id int8,
  addr_type varchar(5),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ent_address_pk PRIMARY KEY (ent_addr_id)
) without oids;

CREATE TABLE ent_master
(
  ent_id int8 NOT NULL,
  ent_class varchar(32),
  ent_name varchar(32),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ent_master_pk PRIMARY KEY (ent_id)
) without oids;

CREATE TABLE ins_entity
(
  ins_entity_id int8 NOT NULL,
  ins_entity_type varchar(5),
  ent_id int8,
  active_flag varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_entity_pk PRIMARY KEY (ins_entity_id)
) without oids;

CREATE TABLE ins_items
(
  ins_item_id int8 NOT NULL,
  description varchar(255),
  active_flag varchar(1),
  comission_flag varchar(1),
  premi_flag varchar(1),
  negative_flag varchar(1),
  item_type varchar(5),
  calc_mode varchar(5),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_items_pk PRIMARY KEY (ins_item_id)
) without oids;


CREATE TABLE ins_pol_entity
(
  ins_pe_id int8 NOT NULL,
  pol_id int8,
  ent_id int8,
  rel_type varchar(5),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_entity_pk PRIMARY KEY (ins_pe_id)
) without oids;

CREATE TABLE ins_pol_items
(
  ins_pol_item_id int8 NOT NULL,
  pol_id int8,
  ins_item_id int8,
  description varchar(255),
  amount numeric,
  ar_invoice_id int8,
  ap_invoice_id int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_items_pk PRIMARY KEY (ins_pol_item_id)
) without oids;

CREATE TABLE ins_policy
(
  pol_id int8 NOT NULL,
  pol_no varchar(32),
  description varchar(255),
  ccy varchar(3),
  posted_flag varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_policy_pk PRIMARY KEY (pol_id)
) without oids;

CREATE TABLE ins_policy_types
(
  pol_type_id int8 NOT NULL,
  description varchar(255),
  gl_production int8,
  gl_comission int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_policy_types_pk PRIMARY KEY (pol_type_id)
) without oids;

CREATE TABLE gl_tax
(
  tax_id int8 NOT NULL,
  description varchar(255),
  tax_rate numeric,
  active_flag varchar(1),
  gl_account int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT gl_tax_pk PRIMARY KEY (tax_id)
) without oids;

ALTER TABLE ins_policy ADD COLUMN pol_type_id int8;

ALTER TABLE ins_pol_items ADD COLUMN ins_entity_id int8;

ALTER TABLE ins_policy ADD COLUMN amount numeric;

ALTER TABLE ar_receipt ADD COLUMN rc_id int8;

ALTER TABLE ar_receipt_lines ADD COLUMN ar_invoice_no varchar(64);

ALTER TABLE ar_receipt_lines ADD COLUMN invoice_amount numeric;

ALTER TABLE ar_receipt ADD COLUMN amount_applied numeric;

ALTER TABLE gl_accounts ALTER COLUMN acctype DROP NOT NULL;

ALTER TABLE gl_rpt_col ALTER col_type TYPE varchar(32);

CREATE TABLE gl_rpt_col_type
(
  col_type varchar(32) NOT NULL,
  col_type_desc varchar(255),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT gl_col_type_pk PRIMARY KEY (col_type)
)  without oids;

ALTER TABLE gl_rpt_col ADD COLUMN  int4;
ALTER TABLE gl_rpt_col ADD COLUMN gl_period int4;
ALTER TABLE gl_rpt_col ADD COLUMN gl_value varchar(5);
ALTER TABLE gl_rpt_col ADD COLUMN col_header varchar(255);

delete from gl_period_det;
ALTER TABLE gl_period_det ADD COLUMN gl_period_detail_id numeric NOT NULL;
ALTER TABLE gl_period_det DROP CONSTRAINT gl_period_det_pk;
ALTER TABLE gl_period_det ADD CONSTRAINT gl_period_det_pk PRIMARY KEY (gl_period_detail_id);


drop table gl_acct_bal;
CREATE TABLE gl_acct_bal
(
  account_id int8 NOT NULL,
  period_year int8 NOT NULL,
  period_no int4 NOT NULL,
  bal numeric,
  CONSTRAINT gl_acct_bal_pk PRIMARY KEY (account_id, period_year, period_no)
) without oids;

ALTER TABLE gl_period_det ADD COLUMN close_flag varchar(1);

ALTER TABLE gl_je_detail DROP COLUMN period_id;

ALTER TABLE gl_rpt_col ADD COLUMN gl_period_to int4;

ALTER TABLE ins_policy_types DROP COLUMN gl_production;
ALTER TABLE ins_policy_types DROP COLUMN gl_comission;

ALTER TABLE ins_policy_types ADD COLUMN gl_comission varchar(32);
ALTER TABLE ins_policy_types ADD COLUMN gl_comission2 varchar(32);

ALTER TABLE ins_policy_types ADD COLUMN gl_ar varchar(32);
ALTER TABLE ins_policy_types ADD COLUMN gl_ar2 varchar(32);
ALTER TABLE ins_policy_types ADD COLUMN gl_rev varchar(32);
ALTER TABLE ins_policy_types ADD COLUMN gl_rev2 varchar(32);

ALTER TABLE ins_pol_entity ADD COLUMN gl_comission varchar(32);

ALTER TABLE ins_entity ADD COLUMN gl_comission varchar(32);

drop table ar_payment_method;
CREATE TABLE payment_method
(
  pmt_method_id int8 NOT NULL,
  rc_id int8,
  gl_acct_id int8,
  description varchar(255),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT payment_method_pk PRIMARY KEY (pmt_method_id)
);

DROP TABLE ar_receipt_class;
CREATE TABLE receipt_class
(
  rc_id int8 NOT NULL,
  description varchar(255),
  active_flag varchar(1),
  remit_flag varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT receipt_class_pk PRIMARY KEY (rc_id)
);

CREATE TABLE ar_invoice_details
(
  ar_invoice_dtl_id int8 NOT NULL,
  ar_invoice_id int8,
  description  varchar(255),
  amount numeric,
  gl_account_id int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ar_invoice_details_pk PRIMARY KEY (ar_invoice_dtl_id)
);

ALTER TABLE ar_invoice ADD COLUMN gl_ar int8;

ALTER TABLE gl_rpt_lin ADD COLUMN negate_flag varchar(1);

ALTER TABLE ins_pol_items RENAME ins_entity_id  TO ent_id;

ALTER TABLE ins_pol_items ADD COLUMN posted_flag varchar(1);

CREATE TABLE ar_trx_type
(
  ar_trx_type_id int8 NOT NULL,
  positive_flag  varchar(1),
  description  varchar(128),
  gl_account_id  int8,
  active_flag  varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ar_trx_type_pk PRIMARY KEY (ar_trx_type_id)
);

ALTER TABLE ar_invoice ADD COLUMN ar_trx_type_id int8;

ALTER TABLE ins_policy ADD COLUMN period_start timestamp;
ALTER TABLE ins_policy ADD COLUMN period_end timestamp;

ALTER TABLE ins_pol_items ADD COLUMN calc_mode varchar(5);
ALTER TABLE ins_pol_items ADD COLUMN entered_amount numeric;

ALTER TABLE ins_pol_items ADD COLUMN rate numeric;


DROP TABLE ins_entity;

CREATE TABLE ins_entity
(
  ent_id int8 NOT NULL,
  ins_entity_type varchar(5),
  active_flag varchar(1),
  gl_comission varchar(32),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_entity_pk PRIMARY KEY (ent_id)
);

ALTER TABLE ins_policy_types ADD COLUMN gl_ap varchar(32);

ALTER TABLE ins_entity RENAME gl_comission  TO gl_ap;

ALTER TABLE ar_invoice ADD COLUMN invoice_type varchar(5);

ALTER TABLE ar_receipt ADD COLUMN invoice_type varchar(5);

ALTER TABLE ar_receipt ADD COLUMN receipt_date timestamp;

ALTER TABLE ar_invoice ADD COLUMN description varchar(255);

ALTER TABLE receipt_class ADD COLUMN rc_type varchar(5);
ALTER TABLE receipt_class ADD COLUMN invoice_type varchar(5);

ALTER TABLE ar_receipt_lines ADD COLUMN line_type varchar(5);

CREATE TABLE gl_currency
(
  ccy_code  varchar(3) NOT NULL,
  description  varchar(128) NOT NULL,
  enabled_flag  varchar(1) NOT NULL,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT gl_currency_pk PRIMARY KEY (ccy_code)
);

CREATE TABLE gl_ccy_history
(
  ccy_hist_id int8 NOT NULL,
  ccy_code  varchar(3),
  ccy_date timestamp,
  ccy_rate numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT gl_ccy_history_pk PRIMARY KEY (ccy_hist_id)
);

ALTER TABLE gl_je_detail ADD COLUMN entered_debit numeric;
ALTER TABLE gl_je_detail ADD COLUMN entered_credit numeric;
ALTER TABLE gl_je_detail ADD COLUMN ccy_code varchar(3);
ALTER TABLE gl_je_detail ADD COLUMN ccy_rate numeric;

CREATE TABLE ins_policy_subtype
(
  pol_type_id int8 NOT NULL,
  pol_subtype_id int8,
  description  varchar(128),
  active_flag  varchar(1),
  premi_rate numeric,
  gl_production  varchar(32),
  gl_cost  varchar(32),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_policy_subtype_pk PRIMARY KEY (pol_subtype_id)
);

CREATE TABLE ins_clausules
(
  ins_clause_id int8 NOT NULL,
  description  varchar(128),
  active_flag  varchar(1),
  rate numeric,
  rate_type  varchar(5),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_clausules_pk PRIMARY KEY (ins_clause_id)
);

CREATE TABLE ins_polsubtype_clause
(
  pol_type_id int8 NOT NULL,
  ins_clause_id int8 NOT NULL,
  rate numeric,
  rate_type  varchar(5),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT policy_subtype_clausules_pk PRIMARY KEY (pol_type_id,ins_clause_id)
);

CREATE TABLE ins_pol_clausules
(
  ins_pol_claus_id int8 NOT NULL,
  ins_clause_id int8,
  pol_id int8,
  rate numeric,
  rate_type  varchar(5),
  amount numeric,
  ins_pol_obj_id int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_clausules_pk PRIMARY KEY (ins_pol_claus_id)
);

CREATE TABLE ins_pol_vehicles
(
  ins_pol_obj_id int8 NOT NULL,
  pol_id int8,
  vehicle_type_id int8,
  police_reg_no  varchar(16),
  prod_year int4,
  chassis_no  varchar(64),
  engine_no  varchar(64),
  seat_num int4,
  insured_amount numeric,
  premi_rate numeric,
  premi_amount numeric,
  tjh3 numeric,
  pa_driver_rate numeric,
  pa_driver_premi numeric,
  self_risk_amount numeric,
  pa_passng_rate numeric,
  pa_passng_premi numeric,
  remarks  text,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_vehicles_pk PRIMARY KEY (ins_pol_obj_id)
);

CREATE TABLE ins_poltype_clausules
(
  pol_type_id int8 NOT NULL,
  ins_clause_id int8,
  rate numeric,
  rate_type  varchar(3),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_poltype_clausules_pk PRIMARY KEY (pol_type_id)
);

ALTER TABLE ins_policy_types ADD COLUMN poltype_code varchar(5);

ALTER TABLE ins_clausules ADD COLUMN pol_type_id int8;
ALTER TABLE ins_clausules ADD COLUMN shortdesc varchar(64);
ALTER TABLE ins_clausules ALTER rate_type TYPE varchar(16);

ALTER TABLE ins_polsubtype_clause ADD COLUMN select_type varchar(16);
ALTER TABLE ins_polsubtype_clause ALTER rate_type TYPE varchar(16);

ALTER TABLE ins_policy ADD COLUMN pol_subtype_id int8;

ALTER TABLE ins_polsubtype_clause RENAME pol_type_id  TO pol_subtype_id;

ALTER TABLE ins_policy ADD COLUMN premi_base numeric;

ALTER TABLE ins_policy ADD COLUMN premi_total numeric;

ALTER TABLE ins_policy ADD COLUMN premi_rate numeric;

ALTER TABLE ins_pol_vehicles ADD COLUMN vehicle_type_desc varchar(128);

ALTER TABLE ins_clausules ADD COLUMN clause_level varchar(16);

ALTER TABLE ins_pol_vehicles ADD COLUMN premi_total numeric;

ALTER TABLE ins_policy ADD COLUMN insured_amount numeric;

ALTER TABLE ins_policy ADD COLUMN policy_date timestamp;

CREATE TABLE s_valueset
(
  vs_group varchar(64) NOT NULL,
  vs_code varchar(64) NOT NULL,
  vs_description varchar(255),
  active_flag varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT s_valueset_pk PRIMARY KEY (vs_group, vs_code)
)
WITHOUT OIDS;

CREATE TABLE s_region
(
  region_id int8 NOT NULL,
  description varchar(255),
  active_flag varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT s_region_pk PRIMARY KEY (region_id)
)
WITHOUT OIDS;

ALTER TABLE ins_policy ADD COLUMN bus_source varchar(32);
ALTER TABLE ins_policy ADD COLUMN region_id int8;
ALTER TABLE ins_policy ADD COLUMN captive_flag varchar(1);
ALTER TABLE ins_policy ADD COLUMN inward_flag varchar(1);

ALTER TABLE ent_address ADD COLUMN primary_flag varchar(1);
ALTER TABLE ent_address ADD COLUMN mailing_flag varchar(1);

ALTER TABLE ent_master ADD COLUMN first_name varchar(32);
ALTER TABLE ent_master ADD COLUMN last_name varchar(32);
ALTER TABLE ent_master ADD COLUMN middle_name varchar(32);
ALTER TABLE ent_master ADD COLUMN title varchar(16);
ALTER TABLE ent_master ADD COLUMN birth_date timestamp;

ALTER TABLE ent_address ADD COLUMN city_id int8;
ALTER TABLE ent_address ADD COLUMN postal_code varchar(10);
ALTER TABLE ent_address ADD COLUMN province_id int8;
ALTER TABLE ent_address ADD COLUMN sub_region_id int8;
ALTER TABLE ent_address ADD COLUMN address varchar(255);

ALTER TABLE ent_master ADD COLUMN sex_id varchar(1);

ALTER TABLE ent_master ALTER ent_name TYPE varchar(255);

ALTER TABLE ins_pol_clausules ALTER rate_type TYPE varchar(16);

drop table ins_entity;
CREATE TABLE ins_entity
(
  ins_entity_id int8 NOT NULL,
  ent_id int8 NOT NULL,
  ins_entity_type varchar(5),
  active_flag varchar(1),
  gl_ap varchar(32),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_entity_pk PRIMARY KEY (ins_entity_id)
) without oids;

ALTER TABLE ins_policy ADD COLUMN premi_netto numeric;
ALTER TABLE ins_policy ADD COLUMN ccy_rate numeric;

CREATE TABLE gl_chart
(
  account_id int8 NOT NULL,
  account_no varchar(32),
  description varchar(128),
  account_type varchar(5),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT gl_char_pk PRIMARY KEY (account_id)
)
WITHOUT OIDS;

ALTER Table gl_dept RENAME TO gl_cost_center;
ALTER TABLE gl_cost_center RENAME dept_code  TO cc_code;

ALTER TABLE gl_chart RENAME account_no  TO accountno;
ALTER TABLE gl_chart RENAME account_type  TO acctype;

ALTER TABLE ar_invoice ADD COLUMN cc_code varchar(8);
ALTER TABLE ar_receipt ADD COLUMN cc_code varchar(8);

ALTER TABLE ins_policy ADD COLUMN cc_code varchar(8);
ALTER TABLE ar_receipt ADD COLUMN account_id int8;

ALTER TABLE ar_receipt ADD COLUMN ccy_rate numeric;

ALTER TABLE ar_receipt ADD COLUMN description varchar(255);
ALTER TABLE ar_receipt ADD COLUMN shortdesc varchar(128);

ALTER TABLE ar_receipt ADD COLUMN excess_acc_id int8;

ALTER TABLE gl_je_detail ADD COLUMN ref_trx varchar(64);
ALTER TABLE gl_je_detail ADD COLUMN ref_reverse int8;

ALTER TABLE receipt_class ADD COLUMN excess_account varchar(32);

ALTER TABLE ar_invoice ADD COLUMN ccy_rate numeric;

ALTER TABLE gl_rpt_lin ADD COLUMN col_no int8;
ALTER TABLE gl_rpt_lin ADD COLUMN print_flag varchar(1);
ALTER TABLE gl_rpt_lin ADD COLUMN prt_cr varchar(1);

ALTER TABLE ent_master ADD COLUMN full_name varchar(128);
ALTER TABLE ent_master ADD COLUMN ent_type varchar(10);
ALTER TABLE ent_master ADD COLUMN customer_status varchar(10);
ALTER TABLE ent_master ADD COLUMN front_office_code varchar(10);
ALTER TABLE ent_master ADD COLUMN tax_file varchar(32);
ALTER TABLE ent_master ADD COLUMN ar_term_id int8;
ALTER TABLE ent_master ADD COLUMN ap_term_id int8;
ALTER TABLE ent_master ADD COLUMN country_id int8;
ALTER TABLE ent_master ADD COLUMN ident_type varchar(10);
ALTER TABLE ent_master ADD COLUMN indent_number varchar(128);
ALTER TABLE ent_master ADD COLUMN marital_status varchar(10);
ALTER TABLE ent_master ADD COLUMN dependent_num int8;
ALTER TABLE ent_master ADD COLUMN religion varchar(10);
ALTER TABLE ent_master ADD COLUMN sales_tax varchar(10);
ALTER TABLE ent_master ADD COLUMN bus_line varchar(32);
ALTER TABLE ent_master ADD COLUMN incorporate_date timestamp;

ALTER TABLE ent_address ADD COLUMN regional_id1 int8;
ALTER TABLE ent_address ADD COLUMN regional_id2 int8;
ALTER TABLE ent_address ADD COLUMN regional_id3 int8;
ALTER TABLE ent_address ADD COLUMN regional_id4 int8;
ALTER TABLE ent_address ADD COLUMN country_id int8;
ALTER TABLE ent_address ADD COLUMN ownership_code varchar(10);
ALTER TABLE ent_address ADD COLUMN occupied_date timestamp;
ALTER TABLE ent_address ADD COLUMN phone varchar(32);
ALTER TABLE ent_address ADD COLUMN phone_mobile varchar(32);
ALTER TABLE ent_address ADD COLUMN phone_fax varchar(32);
ALTER TABLE ent_address ADD COLUMN email varchar(128);
ALTER TABLE ent_address ADD COLUMN website varchar(255);
ALTER TABLE ent_address ADD COLUMN predef_code varchar(32);
ALTER TABLE ent_address ADD COLUMN predef_name varchar(255);


CREATE TABLE ent_rel
(
  ent_rel_id int8 NOT NULL,
  rel_code varchar(10),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  emp_position varchar(10),
  emp_income varchar(10),
  emp_incm_ccy varchar(3),
  emp_incm_amt numeric,
  ent_id1 int8,
  ent_id2 int8,
  CONSTRAINT ent_rel_pk PRIMARY KEY (ent_rel_id)
)
WITHOUT OIDS;

CREATE TABLE s_country
(
  country_id int8 NOT NULL,
  country_name varchar(32),
  active_flag varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT s_country_pk PRIMARY KEY (country_id)
)
WITHOUT OIDS;

CREATE TABLE payment_term
(
  payment_term_id int8 NOT NULL,
  description  varchar(128),
  active_flag   varchar(1),
  due_days  int4,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT payment_term_pk PRIMARY KEY (payment_term_id)
) without oids;

drop table s_country;

CREATE TABLE s_country
(
  country_id varchar(3) NOT NULL,
  country_name varchar(32),
  active_flag varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT s_country_pk PRIMARY KEY (country_id)
)
WITHOUT OIDS;

ALTER TABLE ent_address DROP COLUMN country_id;

ALTER TABLE ent_address ADD COLUMN country_id varchar(3);

CREATE TABLE s_region_map
(
  region_id int8 NOT NULL,
  region_level int8,
  parent_id int8,
  country_id  varchar(3),
  region_name  varchar(128),
  active_flag  varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT s_region_map_pk PRIMARY KEY (region_id)
) without oids;


CREATE TABLE s_region_set
(
  region_set_id int8 NOT NULL,
  country_id  varchar(3),
  level1  varchar(128),
  level2  varchar(128),
  level3  varchar(128),
  level4  varchar(128),
  level5  varchar(128),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT s_region_set_pk PRIMARY KEY (region_set_id)
) without oids;

CREATE TABLE s_religion
(
  religion_id int8 NOT NULL,
  religion_name  varchar(128),
  active_flag  varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT s_religion_pk PRIMARY KEY (religion_id)
) without oids;

ALTER TABLE ent_master DROP COLUMN country_id;

ALTER TABLE ent_master ADD COLUMN country_id varchar(3);

ALTER TABLE ins_policy_types ADD COLUMN gl_code varchar(10);

ALTER TABLE ins_policy_subtype ADD COLUMN gl_code varchar(10);

ALTER TABLE ent_master ADD COLUMN gl_code varchar(16);

ALTER TABLE ent_master ADD COLUMN ins_inward_flag varchar(1);

ALTER TABLE ent_master ADD COLUMN ins_outward_flag varchar(1);

ALTER TABLE ar_invoice ADD COLUMN attr_pol_no varchar(32);
ALTER TABLE ar_invoice ADD COLUMN mutation_date timestamp;
ALTER TABLE ar_invoice ADD COLUMN attr_pol_name varchar(255);
ALTER TABLE ar_invoice ADD COLUMN attr_pol_per_0 timestamp;
ALTER TABLE ar_invoice ADD COLUMN attr_pol_per_1 timestamp;
ALTER TABLE ar_invoice ADD COLUMN attr_pol_address varchar(255);
ALTER TABLE ar_invoice ADD COLUMN attr_pol_tsi numeric;
ALTER TABLE ar_invoice ADD COLUMN attr_pol_tsi_total numeric;
ALTER TABLE ar_invoice ADD COLUMN attr_pol_type_id int8;
ALTER TABLE ar_invoice ADD COLUMN attr_quartal varchar(32);
ALTER TABLE ar_invoice ADD COLUMN attr_underwrit varchar(32);
ALTER TABLE ar_invoice ADD COLUMN attr_pol_id int8;

ALTER TABLE ar_trx_type ADD COLUMN ar_account varchar(32);
ALTER TABLE ar_trx_type ADD COLUMN ap_account varchar(32);

CREATE TABLE ar_trx_line
(
  ar_trx_line_id int8 NOT NULL,
  ar_trx_type_id int8 NOT NULL,
  item_desc  varchar(128) ,
  gl_account  varchar(32) ,
  negative_flag  varchar(1) ,
  enabled_flag  varchar(1) ,
  create_date timestamp ,
  create_who varchar(32) ,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ar_trx_line_pk PRIMARY KEY (ar_trx_line_id)
) without oids;

ALTER TABLE ar_trx_type ADD COLUMN ar_trx_codes text;

ALTER TABLE ar_invoice_details ADD COLUMN negative_flag varchar(1);
ALTER TABLE ar_invoice_details ADD COLUMN entered_amount numeric;

ALTER TABLE ar_invoice ADD COLUMN entered_amount numeric;

ALTER TABLE ar_invoice_details ADD COLUMN gl_account_code varchar(32);
ALTER TABLE ar_invoice ADD COLUMN gl_ar_code varchar(32);
ALTER TABLE ar_invoice ADD COLUMN gl_ap_code varchar(32);

ALTER TABLE ar_invoice ADD COLUMN negative_flag varchar(1);
ALTER TABLE ar_trx_type ADD COLUMN invoice_type varchar(5);

ALTER TABLE ent_master ADD COLUMN ins_company_flag varchar(1);

ALTER TABLE gl_je_detail ADD COLUMN trx_hdr_id int8;

ALTER TABLE ent_master ADD COLUMN captive_flag varchar(1);
ALTER TABLE ent_master ADD COLUMN category1 varchar(16);

ALTER TABLE ar_invoice_details ADD COLUMN ent_id int8;
ALTER TABLE ar_receipt_lines ADD COLUMN comission_flag varchar(1);
ALTER TABLE ar_receipt_lines ADD COLUMN taxable_flag varchar(1);
ALTER TABLE ar_receipt_lines ADD COLUMN parent_id int8;
ALTER TABLE ar_receipt_lines ADD COLUMN description varchar(255);
ALTER TABLE ar_receipt_lines ADD COLUMN amount_entered numeric;
ALTER TABLE ar_receipt_lines ADD COLUMN inv_amt_entered numeric;

ALTER TABLE ar_receipt ADD COLUMN entered_amount numeric;
ALTER TABLE ar_receipt ADD COLUMN rate_diff_amount numeric;

ALTER TABLE ar_receipt_lines ADD COLUMN ccy_code varchar(3);
ALTER TABLE ar_receipt_lines ADD COLUMN ccy_rate numeric;

CREATE TABLE ins_tsi_cat
(
  ins_tsi_cat_id int8 NOT NULL,
  description  varchar(128) ,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_tsi_cat_pk PRIMARY KEY (ins_tsi_cat_id)
) without oids;


CREATE TABLE ins_tsicat_poltype
(
  ins_tcpt_id int8 NOT NULL,
  ins_tsi_cat_id int8 NOT NULL,
  pol_type_id int8 NOT NULL,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_tsicat_poltype_pk PRIMARY KEY (ins_tcpt_id)
) without oids;

CREATE TABLE ins_cover
(
  ins_cover_id int8 NOT NULL,
  description  varchar(128),
  cover_category   varchar(16),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_cover_pk PRIMARY KEY (ins_cover_id)
) without oids;


CREATE TABLE ins_cover_poltype
(
  ins_cvpt_id int8 NOT NULL,
  ins_cover_id int8,
  pol_type_id int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_cover_poltype_pk PRIMARY KEY (ins_cvpt_id)
) without oids;

CREATE TABLE ins_pol_cover
(
  ins_pol_cover_id int8 NOT NULL,
  pol_id int8,
  ins_cover_id int8,
  insured_amount numeric,
  rate numeric,
  premi numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_cover_pk PRIMARY KEY (ins_pol_cover_id)
) without oids;

CREATE TABLE ins_pol_tsi
(
  ins_pol_tsi_id int8 NOT NULL,
  pol_id int8,
  ins_tsi_cat_id int8,
  description text,
  insured_amount  numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_tsi_pk PRIMARY KEY (ins_pol_tsi_id)
) without oids;

ALTER TABLE ins_policy ADD COLUMN entity_id int8;

CREATE TABLE ins_pol_coins
(
  ins_pol_coins_id int8  NOT NULL,
  policy_id int8,
  entity_id int8,
  share_pct numeric,
  amount numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_coins_pk PRIMARY KEY (ins_pol_coins_id)
) without oids;

ALTER TABLE ins_policy ADD COLUMN condition_id int8;
ALTER TABLE ins_policy ADD COLUMN risk_category_id int8;


CREATE TABLE ins_risk_cat
(
  ins_risk_cat_id int8 NOT NULL,
  ins_risk_cat_code  varchar(16),
  description  varchar(128),
  poltype_id int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_risk_cat_pk PRIMARY KEY (ins_risk_cat_id)
) without oids;

ALTER TABLE ins_policy ADD COLUMN cover_type_code varchar(16);

ALTER TABLE ins_pol_cover ADD COLUMN entry_mode varchar(10);

ALTER TABLE ins_pol_vehicles ADD COLUMN policy_no varchar(64);

CREATE TABLE ins_pol_pa
(
  ins_pol_pa_id int8 NOT NULL,
  ins_period int8,
  period_start timestamp,
  period_end timestamp,
  pa_name  varchar(128),
  pa_occupancy  varchar(32),
  pa_birthdate timestamp,
  pa_addr text,
  pa_id_no  varchar(64),
  pa_benef_name  varchar(128),
  pa_benef_rel  varchar(16),
  insured_amount numeric,
  premi_rate numeric,
  premi_amount numeric,
  premi_total numeric,
  policy_no varchar(64),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_pa_pk PRIMARY KEY (ins_pol_pa_id)
) without oids;

ALTER TABLE ins_pol_pa ADD COLUMN birth_place varchar(128);
ALTER TABLE ins_pol_pa ADD COLUMN member_no varchar(128);


ALTER TABLE ins_pol_pa RENAME ins_pol_pa_id  TO ins_pol_obj_id;
ALTER TABLE ins_pol_pa ADD COLUMN pol_id int8;

ALTER TABLE ins_pol_tsi ADD COLUMN ins_pol_obj_id int8;
ALTER TABLE ins_pol_cover ADD COLUMN ins_pol_obj_id int8;

ALTER TABLE ins_pol_cover ADD COLUMN f_entry_insamt varchar(1);
ALTER TABLE ins_pol_cover ADD COLUMN f_entry_rate varchar(1);

ALTER TABLE ins_pol_coins ADD COLUMN premi_amt numeric;
ALTER TABLE ins_pol_coins ADD COLUMN position_code varchar(16);
ALTER TABLE ins_pol_coins ADD COLUMN f_entrybyrate varchar(1);

ALTER TABLE gl_ccy_history ADD COLUMN ccy_master varchar(3);
ALTER TABLE ins_pol_vehicles ADD COLUMN ins_risk_cat_id int8;
ALTER TABLE ins_pol_pa ADD COLUMN ins_risk_cat_id int8;

CREATE TABLE ins_pol_obj
(
  ins_pol_obj_id int8 NOT NULL,
  period_start timestamp,
  period_end timestamp,
  pol_id int8,
  ins_risk_cat_id int8,
  insured_amount numeric,
  premi_rate numeric,
  premi_amount numeric,
  premi_total numeric,
  policy_no varchar(64),
  description   varchar(255),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_obj_pk PRIMARY KEY (ins_pol_obj_id)
) without oids;

ALTER TABLE ins_policy ADD COLUMN f_prodmode varchar(1);

ALTER TABLE ar_invoice ADD COLUMN f_premi varchar(1);
ALTER TABLE ar_invoice_details ADD COLUMN f_comission varchar(1);

ALTER TABLE gl_rpt_col ADD COLUMN col_pos numeric;
ALTER TABLE gl_rpt_col ADD COLUMN col_fmt varchar(64);

ALTER TABLE gl_rpt_lin ADD COLUMN vrbl varchar(128);
ALTER TABLE gl_rpt_lin ADD COLUMN formula varchar(255);

ALTER TABLE gl_rpt_lin ADD COLUMN fmt varchar(128);

ALTER TABLE ins_policy ADD COLUMN cust_name varchar(255);
ALTER TABLE ins_policy ADD COLUMN cust_address varchar(255);

ALTER TABLE ent_address ADD COLUMN address varchar(255);
ALTER TABLE ins_pol_items ADD COLUMN f_entryrate varchar(1);

ALTER TABLE ins_clausules ADD COLUMN exclusion_flag varchar(1);

CREATE TABLE ins_clm_cause
(
  ins_clm_caus_id int8 NOT NULL,
  cause_desc  varchar(255),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_clm_cause_pk PRIMARY KEY (ins_clm_caus_id)
) without oids;

CREATE TABLE ins_clm
(
  ins_clm_id int8 NOT NULL,
  refno   varchar(64),
  report_date timestamp,
  rpter_id int8,
  rpter_name  varchar(255),
  rpter_address  varchar(255),
  rpter_phone  varchar(64),
  rpter_idno  varchar(128),
  rpter_remark text,
  doc_status  varchar(16),
  issue_date timestamp,
  entry_code  varchar(32),

  loss_status  varchar(16),
  reco_mode  varchar(16),
  loss_adjuster  varchar(128),
  indm_desc text,
  prel_clm_amt numeric,
  depr_val_amt numeric,
  def_clm_amt numeric,
  bppdan_pokn_amt numeric,
  ow_rettn_amt numeric,
  surplus numeric,
  facultative numeric,
  total_amt numeric,

  approved_by  varchar(128),
  approve_date timestamp,

  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_clm_pk PRIMARY KEY (ins_clm_id)
) without oids;

CREATE TABLE ins_clm_pol
(
  ins_clm_pol_id int8 NOT NULL,
  ins_clm_id int8 NOT NULL,
  policy_id int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_clm_pol_pk PRIMARY KEY (ins_clm_pol_id)
) without oids;

CREATE TABLE ins_clm_obj
(
  ins_clm_obj_id int8 NOT NULL,
  ins_clm_id int8 NOT NULL,
  lks_number  varchar(128),
  policy_id int8,
  ins_pol_obj_id int8,
  ev_location text,
  ev_date timestamp,
  ins_clm_caus_id int8,
  loss_desc text,
  loss_est_amt numeric,
  loss_real_amt numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_clm_pol_pk PRIMARY KEY (ins_clm_obj_id)
) without oids;

CREATE TABLE ins_clm_coins
(
  ins_clm_coins_id int8 NOT NULL,
  ins_clm_id int8 NOT NULL,
  ent_id int8,
  position_code  varchar(16),
  share_pct numeric,
  tsi_amt numeric,
  claim_amt numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_clm_coins_pk PRIMARY KEY (ins_clm_coins_id)
) without oids;

ALTER TABLE ins_policy ADD COLUMN master_policy_id int8;

ALTER TABLE ent_master ADD COLUMN short_name varchar(128);

CREATE TABLE ar_bal
(
  ent_id int8 NOT NULL,
  bal_ar numeric,
  bal_ap numeric,
  CONSTRAINT ar_bal_pk PRIMARY KEY (ent_id)
) without oids;

CREATE TABLE ins_polsubtype_cover
(
  pol_subtype_id int8 NOT NULL,
  ins_cover_id int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_polsubtype_cover_pk PRIMARY KEY (pol_subtype_id,ins_cover_id)
) without oids;

ALTER TABLE ins_cover_poltype ADD COLUMN cover_category varchar(10);

ALTER TABLE ins_cover_poltype ADD COLUMN pol_subtype_id int8;

ALTER TABLE ins_pol_cover ADD COLUMN cover_category varchar(16);

ALTER TABLE ins_pol_cover ADD COLUMN ins_cvpt_id int8;


ALTER TABLE ins_policy ADD COLUMN prod_name varchar(255);
ALTER TABLE ins_policy ADD COLUMN prod_address varchar(255);
ALTER TABLE ins_policy ADD COLUMN prod_id int8;

ALTER TABLE s_users ADD COLUMN branch varchar(64);

ALTER TABLE ar_trx_type ADD COLUMN resource_id varchar(32);

ALTER TABLE ins_pol_obj ADD COLUMN ref1 varchar(64);
ALTER TABLE ins_pol_obj ADD COLUMN ref2 varchar(64);
ALTER TABLE ins_pol_obj ADD COLUMN ref3 varchar(64);
ALTER TABLE ins_pol_obj ADD COLUMN ref4 varchar(64);

ALTER TABLE gl_rpt ADD COLUMN resource_id varchar(32);

CREATE TABLE ins_pol_deduct
(
  ins_pol_deduct_id int8 NOT NULL,
  description  varchar(128),
  amount numeric,
  percentage numeric,
  deduct_type  varchar(16),
  amt_min numeric,
  amt_max numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_deduct_pk PRIMARY KEY (ins_pol_deduct_id)
) without oids;

ALTER TABLE ins_pol_deduct ADD COLUMN pol_id int8;

CREATE TABLE ins_policy_type_grp
(
  ins_policy_type_grp_id int8 NOT NULL,
  group_name  varchar(128),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_polic_type_grp_pk PRIMARY KEY (ins_policy_type_grp_id)
) without oids;

ALTER TABLE ins_policy_types ADD COLUMN ins_policy_type_grp_id int8;
ALTER TABLE ins_policy ADD COLUMN ins_policy_type_grp_id int8;

ALTER TABLE ins_policy ADD COLUMN premi_total_adisc numeric;
ALTER TABLE ins_policy ADD COLUMN total_due numeric;
ALTER TABLE ins_items ADD COLUMN gl_account varchar(255);

ALTER TABLE ar_trx_type ADD COLUMN parent_trx_id int8;
ALTER TABLE ar_trx_type ADD COLUMN super_type_flag varchar(1);
ALTER TABLE ar_invoice ADD COLUMN commit_flag varchar(1);
ALTER TABLE ar_invoice ADD COLUMN approved_flag varchar(1);

<<<
CREATE OR REPLACE FUNCTION trg_ar_inv_x_bal()
  RETURNS "trigger" AS
$BODY$
DECLARE
	cnt numeric;
    BEGIN
        --
        -- Create a row in emp_audit to reflect the operation performed on emp,
        -- make use of the special variable TG_OP to work out the operation.
        --

	select into cnt count(1) from ar_bal where ent_id = NEW.ent_id;

	if (cnt=0) THEN
		INSERT INTO ar_bal(ent_id) values(new.ent_id);
	end if;

	if ((TG_OP='DELETE') or (TG_OP='UPDATE')) then
		if (old.invoice_type='AR') then
			update ar_bal set bal_ar=coalesce(bal_ar,0)-coalesce(old.amount,0) where ent_id=OLD.ent_id;
		else
			update ar_bal set bal_ap=coalesce(bal_ap,0)-coalesce(old.amount,0) where ent_id=OLD.ent_id;
		end if;
	end if;

	if (new.invoice_type='AR') then
		update ar_bal set bal_ar=coalesce(bal_ar,0)+coalesce(new.amount,0) where ent_id=NEW.ent_id;
	else
		update ar_bal set bal_ap=coalesce(bal_ap,0)+coalesce(new.amount,0) where ent_id=NEW.ent_id;
	end if;

        RETURN NULL; -- result is ignored since this is an AFTER trigger
    END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE;
>>>

CREATE TRIGGER trg_ar_inv_x_baln
  AFTER INSERT OR UPDATE OR DELETE
  ON ar_invoice
  FOR EACH ROW
  EXECUTE PROCEDURE trg_ar_inv_x_bal();

CREATE TABLE pol_obj_maph
(
  pol_obj_maph_id  varchar(32) NOT NULL,
  description  varchar(128),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT pol_obj_maph_pk PRIMARY KEY (pol_obj_maph_id)
) without oids;

CREATE TABLE pol_obj_mapd
(
  pol_obj_mapd_id int8 NOT NULL,
  pol_obj_maph_id  varchar(32),
  field_no int8,
  field_desc  varchar(255),
  field_ref  varchar(128),
  field_type  varchar(32),
  field_mandatory  varchar(1),
  field_width int8,
  field_height int8,
  lov  varchar(128),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT pol_obj_mapd_pk PRIMARY KEY (pol_obj_mapd_id)
) without oids;

ALTER TABLE ins_pol_obj ALTER ref1 TYPE varchar(255);
ALTER TABLE ins_pol_obj ALTER ref2 TYPE varchar(255);
ALTER TABLE ins_pol_obj ALTER ref3 TYPE varchar(255);
ALTER TABLE ins_pol_obj ALTER ref4 TYPE varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref5 text;
ALTER TABLE ins_pol_obj ADD COLUMN refd1 timestamp;
ALTER TABLE ins_pol_obj ADD COLUMN refd2 timestamp;
ALTER TABLE ins_pol_obj ADD COLUMN refd3 timestamp;
ALTER TABLE ins_pol_obj ADD COLUMN refd4 timestamp;
ALTER TABLE ins_pol_obj ADD COLUMN refn1 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn2 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn3 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn4 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN ref6 text;
ALTER TABLE ins_pol_obj ADD COLUMN ref7 text;
ALTER TABLE ins_pol_obj ADD COLUMN ref8 text;
ALTER TABLE ins_pol_obj ADD COLUMN ref9 text;
ALTER TABLE ins_pol_obj ADD COLUMN ref10 text;

ALTER TABLE ins_policy_types ALTER poltype_code TYPE varchar(64);

CREATE TABLE gl_acct_bal2
(
  account_id int8 NOT NULL,
  period_year int8,
         cr0 numeric,	db0 numeric,	bal0 numeric,
         cr1 numeric,	db1 numeric,	bal1 numeric,
         cr2 numeric,	db2 numeric,	bal2 numeric,
         cr3 numeric,	db3 numeric,	bal3 numeric,
         cr4 numeric,	db4 numeric,	bal4 numeric,
         cr5 numeric,	db5 numeric,	bal5 numeric,
         cr6 numeric,	db6 numeric,	bal6 numeric,
         cr7 numeric,	db7 numeric,	bal7 numeric,
         cr8 numeric,	db8 numeric,	bal8 numeric,
         cr9 numeric,	db9 numeric,	bal9 numeric,
         cr10 numeric,	db10 numeric,	bal10 numeric,
         cr11 numeric,	db11 numeric,	bal11 numeric,
         cr12 numeric,	db12 numeric,	bal12 numeric,
         cr13 numeric,	db13 numeric,	bal13 numeric,
         cr14 numeric,	db14 numeric,	bal14 numeric,
         cr15 numeric,	db15 numeric,	bal15 numeric,
         cr16 numeric,	db16 numeric,	bal16 numeric,
         cr17 numeric,	db17 numeric,	bal17 numeric,
         cr18 numeric,	db18 numeric,	bal18 numeric,
         cr19 numeric,	db19 numeric,	bal19 numeric,
         cr20 numeric,	db20 numeric,	bal20 numeric,
         cr21 numeric,	db21 numeric,	bal21 numeric,
         cr22 numeric,	db22 numeric,	bal22 numeric,
         cr23 numeric,	db23 numeric,	bal23 numeric,
         cr24 numeric,	db24 numeric,	bal24 numeric,
  CONSTRAINT gl_acct_bal2_pk PRIMARY KEY (account_id, period_year)
) without oids;

<<<
CREATE OR REPLACE FUNCTION trg_gl_bal() RETURNS TRIGGER AS $BODY$
DECLARE
	cnt numeric;
   BEGIN
      if ((TG_OP='DELETE') or (TG_OP='UPDATE')) then
         EXECUTE 'update gl_acct_bal2 set db'||OLD.period_no ||'=coalesce(db'||OLD.period_no||',0)-coalesce('||old.debit||',0) where account_id = '||OLD.accountid||' and period_year='||OLD.fiscal_year;
         EXECUTE 'update gl_acct_bal2 set cr'||OLD.period_no ||'=coalesce(cr'||OLD.period_no||',0)-coalesce('||old.credit||',0) where account_id = '||OLD.accountid||' and period_year='||OLD.fiscal_year;
         EXECUTE 'update gl_acct_bal2 set bal'||OLD.period_no ||'=coalesce(bal'||OLD.period_no||',0)+coalesce('||old.debit||',0)-coalesce('||old.credit||',0) where account_id = '||OLD.accountid||' and period_year='||OLD.fiscal_year;
      end if;

      if ((TG_OP='INSERT') or (TG_OP='UPDATE')) then

         select into cnt count(1) from gl_acct_bal2 where account_id = NEW.accountid and period_year=NEW.fiscal_year;

         if (cnt=0) THEN
            INSERT INTO gl_acct_bal2(account_id,period_year) values(NEW.accountid,cast(NEW.fiscal_year as int8));
         end if;

         EXECUTE 'update gl_acct_bal2 set db'||NEW.period_no ||'=coalesce(db'||NEW.period_no||',0)+coalesce('||NEW.debit||',0) where account_id = '||NEW.accountid||' and period_year='||NEW.fiscal_year;
         EXECUTE 'update gl_acct_bal2 set cr'||NEW.period_no ||'=coalesce(cr'||NEW.period_no||',0)+coalesce('||NEW.credit||',0) where account_id = '||NEW.accountid||' and period_year='||NEW.fiscal_year;
         EXECUTE 'update gl_acct_bal2 set bal'||NEW.period_no ||'=coalesce(bal'||NEW.period_no||',0)-coalesce('||NEW.debit||',0)+coalesce('||NEW.credit||',0) where account_id = '||NEW.accountid||' and period_year='||NEW.fiscal_year;
      end if;

      RETURN NULL; -- result is ignored since this is an AFTER trigger
   END;
$BODY$ language plpgsql;
>>>

CREATE TRIGGER trg_gl_balt
AFTER INSERT OR UPDATE OR DELETE ON gl_je_detail
    FOR EACH ROW EXECUTE PROCEDURE trg_gl_bal();

ALTER TABLE ar_invoice_details ADD COLUMN ar_trx_line_id int8;

CREATE TABLE ins_cover_source
(
  ins_cover_source_id  varchar(32) NOT NULL,
  description  varchar(128),
  active_flag  varchar(1),
  ar_trx_type_id int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_cover_source_pk PRIMARY KEY (ins_cover_source_id)
) without oids;

ALTER TABLE ins_cover_source ADD COLUMN share_mode varchar(16);
ALTER TABLE ins_cover_source ADD COLUMN ap_trx_type_id int8;

ALTER TABLE ins_items ADD COLUMN ar_trx_line_id int8;
ALTER TABLE ins_items ADD COLUMN ap_trx_line_id int8;

ALTER TABLE ins_items ADD COLUMN ins_cover_source_id varchar(32);

ALTER TABLE ins_items ALTER item_type TYPE varchar(16);
ALTER TABLE ar_trx_type ALTER ar_account TYPE varchar(255);
ALTER TABLE ar_trx_type ALTER ap_account TYPE varchar(255);
ALTER TABLE ar_trx_line ALTER gl_account TYPE varchar(255);
ALTER TABLE ins_items ADD COLUMN entry_min int8;
ALTER TABLE ins_items ADD COLUMN entry_max int8;
ALTER TABLE ar_trx_line ADD COLUMN comission_flag varchar(1);

ALTER TABLE ar_receipt_lines ADD COLUMN commit_flag varchar(1);

ALTER TABLE ins_pol_deduct ADD COLUMN ccy varchar(3);

ALTER TABLE ins_pol_deduct ADD COLUMN ins_clm_caus_id int8;

ALTER TABLE ar_receipt_lines ADD COLUMN ar_rcl_ref_id int8;

ALTER TABLE ar_receipt_lines ADD COLUMN ar_invoice_dtl_id int8;

ALTER TABLE ar_invoice_details ADD COLUMN amount_settled numeric;

<<<CREATE OR REPLACE FUNCTION trg_ar_rcp_line_settle()
  RETURNS "trigger" AS
$BODY$
DECLARE
	cnt numeric;
    BEGIN
         if ((TG_OP='DELETE') or (TG_OP='UPDATE')) then
            if (old.commit_flag='Y') then
               if ((old.line_type='INVOC') or (old.line_type='NOTE')) then
                  update ar_invoice set amount_settled=coalesce(amount_settled,0)-coalesce(old.amount_entered,0) where ar_invoice_id=OLD.ar_invoice_id;
               end if;
               if ((old.line_type='COMM')) then
                  update ar_invoice_details set amount_settled=coalesce(amount_settled,0)-coalesce(old.amount_entered,0) where ar_invoice_dtl_id=old.ar_invoice_dtl_id;
               end if;
             end if;
         end if;
         if ((TG_OP='INSERT') or (TG_OP='UPDATE')) then
            if (new.commit_flag='Y') then
               if ((new.line_type='INVOC') or (new.line_type='NOTE')) then
                  update ar_invoice set amount_settled=coalesce(amount_settled,0)+coalesce(new.amount_entered,0) where ar_invoice_id=new.ar_invoice_id;
               end if;
               if ((new.line_type='COMM')) then
                  update ar_invoice_details set amount_settled=coalesce(amount_settled,0)+coalesce(new.amount_entered,0) where ar_invoice_dtl_id=new.ar_invoice_dtl_id;
               end if;
            end if;
         end if;
        RETURN NULL;
    END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE;>>>

CREATE TRIGGER trg_ar_rcp_line_settle_t
AFTER INSERT OR UPDATE OR DELETE ON ar_receipt_lines
    FOR EACH ROW EXECUTE PROCEDURE trg_ar_rcp_line_settle();

CREATE TABLE ar_tax
(
  tax_code int8 NOT NULL,
  description  varchar(128),
  inclusive_flag  varchar(1),
  rate numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ar_tax_pk PRIMARY KEY (tax_code)
) without oids;

ALTER TABLE ar_trx_line ADD COLUMN tax_code int8;

ALTER TABLE ar_tax ADD COLUMN account_code varchar(128);

<<<CREATE OR REPLACE FUNCTION trg_ar_inv_x_bal()
  RETURNS "trigger" AS
$BODY$
DECLARE
	cnt numeric;
    BEGIN

        select into cnt count(1) from ar_bal where ent_id = NEW.ent_id;
        if (cnt=0) THEN
            INSERT INTO ar_bal(ent_id) values(new.ent_id);
        end if;
        if ((TG_OP='DELETE') or (TG_OP='UPDATE')) then
            if (old.commit_flag='Y') then
                if (old.invoice_type='AR') then
                    update ar_bal set bal_ar=coalesce(bal_ar,0)-coalesce(old.amount,0) where ent_id=OLD.ent_id;
                else
                    update ar_bal set bal_ap=coalesce(bal_ap,0)-coalesce(old.amount,0) where ent_id=OLD.ent_id;
                end if;
            end if;
	    end if;
        if ((TG_OP='INSERT') or (TG_OP='UPDATE')) then
            if (new.commit_flag='Y') then
                if (new.invoice_type='AR') then
                    update ar_bal set bal_ar=coalesce(bal_ar,0)+coalesce(new.amount,0) where ent_id=NEW.ent_id;
                else
                    update ar_bal set bal_ap=coalesce(bal_ap,0)+coalesce(new.amount,0) where ent_id=NEW.ent_id;
                end if;
            end if;
        end if;
        RETURN NULL;
    END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE;
ALTER FUNCTION trg_ar_inv_x_bal() OWNER TO postgres;>>>

ALTER TABLE ins_pol_items ADD COLUMN tax_code int8;
ALTER TABLE ar_invoice_details ADD COLUMN tax_code int8;
ALTER TABLE ar_invoice_details ADD COLUMN tax_rate numeric;
ALTER TABLE ar_invoice_details ADD COLUMN tax_amount numeric;

ALTER TABLE ar_invoice_details ADD COLUMN tax_code_settle int8;

ALTER TABLE ins_policy ADD COLUMN ins_period_id int8;
ALTER TABLE ins_policy ADD COLUMN inst_period_id int8;
ALTER TABLE ins_policy ADD COLUMN inst_periods int8;

CREATE TABLE ins_period
(
  ins_period_id int8 NOT NULL,
  description  varchar(255),
  period_length int8,
  period_unit  varchar(16),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_period_pk PRIMARY KEY (ins_period_id)
) without oids;

ALTER TABLE ins_policy ADD COLUMN period_rate numeric;

ALTER TABLE pol_obj_maph ADD COLUMN desc_field varchar(64);
ALTER TABLE ar_trx_type ADD COLUMN ra_flag varchar(1);

CREATE TABLE ff_detail
(
  ff_detail_id int8 NOT NULL,
  ff_header_id varchar(32),
  field_no int8,
  field_desc varchar(255),
  field_ref varchar(128),
  field_type varchar(32),
  field_mandatory varchar(1),
  field_width int8,
  field_height int8,
  lov varchar(128),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ff_detail_pk PRIMARY KEY (ff_detail_id)
)
WITHOUT OIDS;

CREATE TABLE ff_header
(
  ff_header_id varchar(32) NOT NULL,
  description varchar(128),
  desc_field varchar(64),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ff_header_pk PRIMARY KEY (ff_header_id)
)
WITHOUT OIDS;

ALTER TABLE ins_pol_obj ADD COLUMN ref11 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref12 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref13 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref14 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref15 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref16 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref17 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref18 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref19 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref20 varchar(255);

ALTER TABLE ins_pol_obj ADD COLUMN refn5 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn6 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn7 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn8 numeric;


ALTER TABLE ins_policy ADD COLUMN ref1 varchar(255);
ALTER TABLE ins_policy ADD COLUMN ref2 varchar(255);
ALTER TABLE ins_policy ADD COLUMN ref3 varchar(255);
ALTER TABLE ins_policy ADD COLUMN ref4 varchar(255);
ALTER TABLE ins_policy ADD COLUMN ref5 varchar(255);
ALTER TABLE ins_policy ADD COLUMN ref6 varchar(255);
ALTER TABLE ins_policy ADD COLUMN ref7 varchar(255);
ALTER TABLE ins_policy ADD COLUMN ref8 varchar(255);
ALTER TABLE ins_policy ADD COLUMN ref9 varchar(255);
ALTER TABLE ins_policy ADD COLUMN ref10 varchar(255);
ALTER TABLE ins_policy ADD COLUMN ref11 varchar(255);
ALTER TABLE ins_policy ADD COLUMN ref12 varchar(255);

ALTER TABLE ins_policy ADD COLUMN refd1 timestamp;
ALTER TABLE ins_policy ADD COLUMN refd2 timestamp;
ALTER TABLE ins_policy ADD COLUMN refd3 timestamp;
ALTER TABLE ins_policy ADD COLUMN refd4 timestamp;
ALTER TABLE ins_policy ADD COLUMN refd5 timestamp;

ALTER TABLE ins_policy ADD COLUMN refn1 numeric;
ALTER TABLE ins_policy ADD COLUMN refn2 numeric;
ALTER TABLE ins_policy ADD COLUMN refn3 numeric;
ALTER TABLE ins_policy ADD COLUMN refn4 numeric;
ALTER TABLE ins_policy ADD COLUMN refn5 numeric;

ALTER TABLE ins_policy ADD COLUMN parent_id int8;

ALTER TABLE ins_policy ADD COLUMN status varchar(32);
ALTER TABLE ins_policy ADD COLUMN active_flag varchar(1);

update ins_policy set active_flag='Y',status='DRAFT';

ALTER TABLE ins_policy ADD COLUMN sppa_no varchar(128);

ALTER TABLE ins_pol_items ADD COLUMN tax_code int8;

ALTER TABLE ins_pol_obj ADD COLUMN ref21 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref22 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref23 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref24 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref25 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref26 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref27 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref28 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref29 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref30 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref31 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref32 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref33 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref34 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref35 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref36 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref37 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref38 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref39 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref40 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ins_pol_obj_ref_id int8;
ALTER TABLE ins_pol_tsi ADD COLUMN insured_amount_ref numeric;
ALTER TABLE ins_pol_cover ADD COLUMN ins_pol_cover_ref_id numeric;
ALTER TABLE ins_pol_tsi ADD COLUMN ins_pol_tsi_ref_id int8;
ALTER TABLE s_valueset ADD COLUMN ref1 varchar(255);

ALTER TABLE ins_policy ADD COLUMN claim_no varchar(128);
ALTER TABLE ins_policy ADD COLUMN claim_date timestamp;
ALTER TABLE ins_policy ADD COLUMN claim_cause int8;
ALTER TABLE ins_policy ADD COLUMN claim_cause_desc text;
ALTER TABLE ins_policy ADD COLUMN event_location text;
ALTER TABLE ins_policy ADD COLUMN claim_person_id int8;
ALTER TABLE ins_policy ADD COLUMN claim_person_name varchar(255);
ALTER TABLE ins_policy ADD COLUMN claim_person_address varchar(255);
ALTER TABLE ins_policy ADD COLUMN claim_person_status varchar(255);
ALTER TABLE ins_policy ADD COLUMN claim_amount_est numeric;
ALTER TABLE ins_policy ADD COLUMN claim_currency varchar(3);
ALTER TABLE ins_policy ADD COLUMN claim_loss_status varchar(32);
ALTER TABLE ins_policy ADD COLUMN claim_benefit varchar(10);
ALTER TABLE ins_policy ADD COLUMN claim_documents text;

ALTER TABLE ins_policy ADD COLUMN endorse_date timestamp;
ALTER TABLE ins_policy ADD COLUMN effective_flag varchar(1);
ALTER TABLE ins_policy ADD COLUMN claim_status varchar(10);

update ins_policy set effective_flag='Y' where active_flag='Y';
ALTER TABLE ins_policy ALTER description TYPE text;
ALTER TABLE ins_policy ADD COLUMN endorse_notes text;

ALTER TABLE ent_master ADD COLUMN ent_addr_id_prim int8;
ALTER TABLE ins_policy ADD COLUMN print_code varchar(64);
ALTER TABLE ins_policy ADD COLUMN root_id int8;

ALTER TABLE ins_policy ADD COLUMN insured_amount_e numeric;

ALTER TABLE ins_policy ADD COLUMN ins_period_base_id int8;
ALTER TABLE ins_policy ADD COLUMN period_rate_before numeric;

update ins_policy set ins_period_base_id=3 where ins_period_base_id is null;
ALTER TABLE ins_policy ADD COLUMN ins_period_base_b4 int8;

ALTER TABLE ins_pol_cover ADD COLUMN void_flag varchar(1);
ALTER TABLE ins_pol_obj ADD COLUMN void_flag varchar(1);
ALTER TABLE ins_pol_vehicles ADD COLUMN void_flag varchar(1);
ALTER TABLE ins_pol_pa ADD COLUMN void_flag varchar(1);
ALTER TABLE ins_pol_deduct ADD COLUMN ins_pol_obj_id int8;

update ins_policy set root_id=parent_id where root_id is null;

update ins_policy set root_id=pol_id where root_id is null;

CREATE TABLE ins_period_base
(
  ins_period_base_id int8 NOT NULL,
  base_unit numeric,
  description  varchar(128),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_period_base_pk PRIMARY KEY (ins_period_base_id)
) without oids;

ALTER TABLE ins_pol_pa ADD COLUMN ins_pol_obj_ref_id int8;
ALTER TABLE ins_pol_vehicles ADD COLUMN ins_pol_obj_ref_id int8;
ALTER TABLE ins_pol_tsi ADD COLUMN void_flag varchar(1);

ALTER TABLE ins_pol_cover ADD COLUMN premi_new numeric;

ALTER TABLE ins_pol_cover ADD COLUMN calculation_desc varchar(255);

CREATE TABLE ins_premium_factor
(
  ins_premium_factor_id int8 NOT NULL,
  description  varchar(255),
  period_rate_factor numeric,
  premi_factor numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_premium_factor_pk PRIMARY KEY (ins_premium_factor_id)
) without oids;

ALTER TABLE ins_policy ADD COLUMN ins_premium_factor_id int8;

ALTER TABLE ins_policy_types ADD COLUMN wording1 text;

update ins_policy set status='PROPOSAL' where status='DRAFT';

ALTER TABLE ins_policy ADD COLUMN dla_no varchar(32);

CREATE TABLE s_lang
(
  lang_id varchar(3) NOT NULL,
  lang_name  varchar(128),
  active_flag  varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT s_lang_pk PRIMARY KEY (lang_id)
) without oids;


CREATE TABLE s_region_map2
(
  region_map_id int8 NOT NULL,
  city_name varchar(64),
  region_class varchar(5),
  region_name varchar(128),
  postal_code varchar(10),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  region_map_desc varchar(255),
  CONSTRAINT s_region_map2_pk PRIMARY KEY (region_map_id)
)
WITHOUT OIDS;


ALTER TABLE ent_address ADD COLUMN region_map_id int8;

CREATE TABLE ins_treaty
(
  ins_treaty_id int8 NOT NULL,
  treaty_name  varchar(128),
  prop_tre_flag  varchar(1),
  retro_cess_flag  varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_treaty_pk PRIMARY KEY (ins_treaty_id)
) without oids;

CREATE TABLE ins_treaty_detail
(
  ins_treaty_detail_id int NOT NULL,
  ins_treaty_id int8,
  treaty_type  varchar(5),
  policy_type_id int8,
  treaty_limit numeric,
  rate_mindep_pct numeric,
  comm_ri_pct numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_treaty_detail_pk PRIMARY KEY (ins_treaty_detail_id)
) without oids;

CREATE TABLE ins_treaty_shares
(
  ins_treaty_shares_id int8 NOT NULL,
  ins_treaty_detail_id int8,
  member_ent_id int8,
  sharepct numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_treaty_shares_pk PRIMARY KEY (ins_treaty_shares_id)
) without oids;

CREATE TABLE ins_pol_treaty
(
  ins_pol_treaty_id int8 NOT NULL,
  ins_treaty_id int8,
  policy_id int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_treaty_pk PRIMARY KEY (ins_pol_treaty_id)
) without oids;

CREATE TABLE ins_pol_treaty_detail
(
  ins_pol_tre_det_id int8 NOT NULL,
  ins_pol_treaty_id int8,
  ins_treaty_detail_id int8,
  tsi_amount numeric,
  premi_amount numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_treaty_detail_pk PRIMARY KEY (ins_pol_tre_det_id)
) without oids;


CREATE TABLE ins_pol_ri
(
  ins_pol_ri_id int8 NOT NULL,
  ins_pol_treaty_id int8,
  ins_pol_tre_det_id int8,
  ins_treaty_detail_id int8,
  ins_treaty_shares_id int8,
  member_ent_id int8,
  sharepct numeric,
  premi_amount numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_ri_pk PRIMARY KEY (ins_pol_ri_id)
) without oids;

CREATE TABLE ins_treaty_types
(
  ins_treaty_type_id  varchar(5) NOT NULL,
  treaty_type_name  varchar(128),
  treaty_type_level numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_treaty_types_pk PRIMARY KEY (ins_treaty_type_id)
) without oids;

ALTER TABLE ins_policy ADD COLUMN ins_treaty_id int8;

ALTER TABLE ins_pol_treaty_detail ADD COLUMN comm_rate numeric;
ALTER TABLE ins_pol_treaty_detail ADD COLUMN comm_amt numeric;

ALTER TABLE ff_detail ADD COLUMN lov_pop varchar(1);

ALTER TABLE ins_cover_source ADD COLUMN inward_flag varchar(1);
ALTER TABLE ins_cover_source ADD COLUMN coins_flag varchar(1);

ALTER TABLE ins_pol_coins ADD COLUMN holding_company_flag varchar(1);

ALTER TABLE ins_treaty_detail ADD COLUMN qs_pct numeric;

CREATE TABLE ins_prt_log
(
  ins_prt_log_id int8 NOT NULL,
  policy_id int8,
  print_type varchar(64),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_prt_log_pk PRIMARY KEY (ins_prt_log_id)
) without oids;

ALTER TABLE ins_prt_log ADD COLUMN serial_number varchar(64);

ALTER TABLE s_valueset ADD COLUMN default_flag varchar(1);
ALTER TABLE s_valueset ADD COLUMN orderseq numeric;
ALTER TABLE s_valueset ADD COLUMN ref2 varchar(1);

ALTER TABLE s_region ADD COLUMN cc_code varchar(8);
ALTER TABLE s_region ADD COLUMN region_code varchar(8);

ALTER TABLE ins_pol_obj ADD COLUMN risk_class varchar(8);

ALTER TABLE ins_risk_cat ADD COLUMN tre_limit0 numeric;
ALTER TABLE ins_risk_cat ADD COLUMN tre_limit1 numeric;
ALTER TABLE ins_risk_cat ADD COLUMN tre_limit2 numeric;
ALTER TABLE ins_risk_cat ADD COLUMN tre_limit3 numeric;

ALTER TABLE s_lang ADD COLUMN lang_order numeric;

ALTER TABLE ins_pol_treaty ADD COLUMN ins_pol_obj_id int8;

ALTER TABLE ins_pol_treaty_detail ADD COLUMN treaty_limit numeric;
ALTER TABLE ins_pol_treaty_detail ADD COLUMN treaty_limit_ratio numeric;

ALTER TABLE ins_policy ADD COLUMN total_fee numeric;
ALTER TABLE ins_clausules ALTER description TYPE text;

ALTER TABLE ins_pol_ri ADD COLUMN or_flag varchar(1);
ALTER TABLE ins_pol_treaty_detail ADD COLUMN mindep_rate numeric;

ALTER TABLE ins_pol_obj ADD COLUMN ref41 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref42 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref43 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref44 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref45 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref46 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref47 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref48 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref49 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref50 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref51 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref52 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref53 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref54 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref55 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref56 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref57 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref58 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref59 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref60 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref61 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref62 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref63 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref64 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref65 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref66 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref67 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref68 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref69 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref70 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref71 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref72 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref73 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref74 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref75 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref76 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref77 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref78 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref79 varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref80 varchar(255);

ALTER TABLE ins_treaty_types ADD COLUMN or_share_flag varchar(1);
ALTER TABLE ins_treaty_types ADD COLUMN free_members_flag varchar(1);

ALTER TABLE ins_treaty_detail ADD COLUMN parent_id numeric;
ALTER TABLE ins_pol_treaty_detail ADD COLUMN parent_id numeric;

ALTER TABLE ins_treaty_detail ADD COLUMN tsi_pct numeric;
ALTER TABLE ins_pol_treaty_detail ADD COLUMN tsi_pct numeric;

ALTER TABLE ins_treaty_detail ADD COLUMN premi_rate numeric;
ALTER TABLE ins_treaty_detail DROP COLUMN rate_mindep_pct;

ALTER TABLE ins_pol_treaty_detail ADD COLUMN premi_rate numeric;
ALTER TABLE ins_pol_treaty_detail DROP COLUMN mindep_rate;

ALTER TABLE ins_treaty ADD COLUMN treaty_period_start timestamp;
ALTER TABLE ins_treaty ADD COLUMN treaty_period_end timestamp;
ALTER TABLE ins_treaty ADD COLUMN treaty_priority numeric;
ALTER TABLE ins_treaty ADD COLUMN active_flag varchar(1);

ALTER TABLE ins_clausules ALTER shortdesc TYPE varchar(255);
update ins_clausules set shortdesc=description;

ALTER TABLE ins_pol_obj ADD COLUMN risk_location varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN zipcode varchar(16);
ALTER TABLE ins_pol_obj ADD COLUMN riskcardno varchar(32);
ALTER TABLE ins_pol_obj ADD COLUMN riskcode varchar(32);

ALTER TABLE ins_policy_types ADD COLUMN custom_handler varchar(128);

ALTER TABLE ff_detail ADD COLUMN read_only varchar(1);
ALTER TABLE ins_pol_cover ADD COLUMN f_autorate varchar(1);

CREATE TABLE ins_rates_big
(
  ins_rates_id varchar(32) NOT NULL,
  rate_class  varchar(8),

  period_start timestamp,
  period_end timestamp,

  ref1 varchar(8),
  ref2 varchar(8),
  ref3 varchar(8),

  rate0 numeric,
  rate1 numeric,
  rate2 numeric,
  rate3 numeric,
  rate4 numeric,
  rate5 numeric,
  rate6 numeric,
  rate7 numeric,
  rate8 numeric,
  rate9 numeric,
  rate10 numeric,

  rate11 numeric,
  rate12 numeric,
  rate13 numeric,
  rate14 numeric,
  rate15 numeric,
  rate16 numeric,
  rate17 numeric,
  rate18 numeric,
  rate19 numeric,
  rate20 numeric,

  rate21 numeric,
  rate22 numeric,
  rate23 numeric,
  rate24 numeric,
  rate25 numeric,
  rate26 numeric,
  rate27 numeric,
  rate28 numeric,
  rate29 numeric,
  rate30 numeric,

  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_rates_pk PRIMARY KEY (ins_rates_id)
) without oids;

ALTER TABLE ins_pol_tsi ADD COLUMN auto_flag varchar(1);

ALTER TABLE ff_detail ADD COLUMN reference1 varchar(255);
ALTER TABLE ff_detail ADD COLUMN reference2 varchar(255);
ALTER TABLE ff_detail ADD COLUMN field_id varchar(64);

ALTER TABLE ins_pol_obj ADD COLUMN refn9 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn10 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn11 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn12 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn13 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn14 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn15 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn16 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn17 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn18 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn19 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn20 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn21 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn22 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn23 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn24 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn25 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn26 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn27 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn28 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn29 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN refn30 numeric;

ALTER TABLE ins_cover_poltype ADD COLUMN use_rate_flag varchar(1);
ALTER TABLE ins_cover_poltype ADD COLUMN use_rate_lock_flag varchar(1);
ALTER TABLE ins_cover_poltype ADD COLUMN auto_rate_flag varchar(1);
ALTER TABLE ins_cover_poltype ADD COLUMN auto_rate_lock_flag varchar(1);
ALTER TABLE ins_cover_poltype ADD COLUMN default_cover_flag varchar(1);
ALTER TABLE ins_cover_poltype ADD COLUMN manual_tsi_flag varchar(1);
ALTER TABLE ins_cover_poltype ADD COLUMN manual_tsi_lock_flag varchar(1);

ALTER TABLE ins_pol_cover ADD COLUMN default_cover_flag varchar(1);

ALTER TABLE ins_tsicat_poltype ADD COLUMN default_tsi_flag varchar(1);
ALTER TABLE ins_tsicat_poltype ADD COLUMN manual_tsi_flag varchar(1);
ALTER TABLE ins_tsicat_poltype ADD COLUMN manual_tsi_lock_flag varchar(1);

ALTER TABLE ins_pol_tsi ADD COLUMN default_tsi_flag varchar(1);
ALTER TABLE ins_pol_tsi ADD COLUMN manual_tsi_lock_flag varchar(1);

ALTER TABLE ins_pol_tsi ADD COLUMN ins_tcpt_id int8;

ALTER TABLE ins_items ADD COLUMN ins_item_cat varchar(8);

ALTER TABLE ar_invoice ADD COLUMN pol_id int8;

ALTER TABLE ins_policy ADD COLUMN nd_comm1 numeric;
ALTER TABLE ins_policy ADD COLUMN nd_comm2 numeric;
ALTER TABLE ins_policy ADD COLUMN nd_comm3 numeric;
ALTER TABLE ins_policy ADD COLUMN nd_comm4 numeric;
ALTER TABLE ins_policy ADD COLUMN nd_brok1 numeric;
ALTER TABLE ins_policy ADD COLUMN nd_brok2 numeric;
ALTER TABLE ins_policy ADD COLUMN nd_hfee numeric;
ALTER TABLE ins_policy ADD COLUMN nd_sfee numeric;
ALTER TABLE ins_policy ADD COLUMN nd_pcost numeric;

ALTER TABLE ins_policy ADD COLUMN nd_update varchar(1);

ALTER TABLE ins_policy ADD COLUMN nd_brok1pct numeric;
ALTER TABLE ins_policy ADD COLUMN nd_brok2pct numeric;
ALTER TABLE ins_policy ADD COLUMN nd_hfeepct numeric;

ALTER TABLE ins_policy ADD COLUMN nd_premi1 numeric;
ALTER TABLE ins_policy ADD COLUMN nd_premi2 numeric;
ALTER TABLE ins_policy ADD COLUMN nd_premi3 numeric;
ALTER TABLE ins_policy ADD COLUMN nd_premi4 numeric;

ALTER TABLE ins_policy ADD COLUMN nd_premirate1 numeric;
ALTER TABLE ins_policy ADD COLUMN nd_premirate2 numeric;
ALTER TABLE ins_policy ADD COLUMN nd_premirate3 numeric;
ALTER TABLE ins_policy ADD COLUMN nd_premirate4 numeric;

ALTER TABLE ins_policy ADD COLUMN nd_disc1 numeric;
ALTER TABLE ins_policy ADD COLUMN nd_disc2 numeric;

ALTER TABLE ins_policy ADD COLUMN nd_disc1pct numeric;
ALTER TABLE ins_policy ADD COLUMN nd_disc2pct numeric;

CREATE OR REPLACE FUNCTION summ(text,text)
  RETURNS text AS
'select coalesce($1,$2)'
  LANGUAGE 'sql' VOLATILE;

CREATE OR REPLACE FUNCTION summ(numeric,numeric)
  RETURNS numeric AS
'select coalesce($1,0)+coalesce($2,0)'
  LANGUAGE 'sql' VOLATILE;

CREATE OR REPLACE FUNCTION summ(int8,int8)
  RETURNS int8 AS
'select coalesce($1,$2)'
  LANGUAGE 'sql' VOLATILE;

CREATE AGGREGATE summ (BASETYPE = bigint, SFUNC = summ, STYPE = bigint);
CREATE AGGREGATE summ (BASETYPE = text, SFUNC = summ, STYPE = text);
CREATE AGGREGATE summ (BASETYPE = numeric, SFUNC = summ, STYPE = numeric);

ALTER TABLE ins_pol_obj ADD COLUMN nd_premi1 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN nd_premi2 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN nd_premi3 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN nd_premi4 numeric;

ALTER TABLE ins_pol_obj ADD COLUMN nd_premirate1 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN nd_premirate2 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN nd_premirate3 numeric;
ALTER TABLE ins_pol_obj ADD COLUMN nd_premirate4 numeric;

<<<CREATE or replace FUNCTION dc_update_policy(v_pol_id text) RETURNS text AS $$
DECLARE
    mviews RECORD;

    comm1 numeric;
    comm2 numeric;
    comm3 numeric;
    comm4 numeric;
    brok1 numeric;
    brok2 numeric;
    hfee numeric;
    hfeepct numeric;
    sfee numeric;
    pdut numeric;
    commt1 numeric;
    commt2 numeric;
    commt3 numeric;
    commt4 numeric;
    brok1pct numeric;
    brok2pct numeric;

    disc1 numeric;
    disc2 numeric;
    disc1pct numeric;
    disc2pct numeric;

    premi1 numeric;
    premi2 numeric;
    premi3 numeric;
    premi4 numeric;
    premi1pct numeric;
    premi2pct numeric;
    premi3pct numeric;
    premi4pct numeric;
    cur_ins_pol_obj_id numeric;
BEGIN

   FOR mviews IN
      select a.amount, a.rate, b.ins_item_cat from ins_pol_items a,ins_items b where b.ins_item_id=a.ins_item_id and a.pol_id=v_pol_id
      LOOP

      if (mviews.ins_item_cat='COMM') then
         if comm1 is null then comm1 := mviews.amount;
            elsif comm2 is null then comm2 := mviews.amount;
            elsif  comm3 is null then comm3 := mviews.amount;
            elsif  comm4 is null then comm4 := mviews.amount;
         end if;
      end if;

      if (mviews.ins_item_cat='DISC') then
         if disc1 is null then
            disc1 := mviews.amount;
            disc1pct := mviews.rate;
         elsif disc2 is null then
            disc2 := mviews.amount;
            disc2pct := mviews.rate;
         end if;
      end if;

      if (mviews.ins_item_cat='BROKR') then
         if brok1 is null then
            brok1 := mviews.amount;
            brok1pct := mviews.rate;
         elsif brok2 is null then
            brok2 := mviews.amount;
            brok2pct := mviews.rate;
         end if;

      end if;

      if (mviews.ins_item_cat='HFEE') then
         hfee := mviews.amount;
         hfeepct := mviews.rate;
      end if;

      if (mviews.ins_item_cat='SFEE') then
         sfee := mviews.amount;
      end if;

      if (mviews.ins_item_cat='PCOST') then
         pdut := mviews.amount;
      end if;

   END LOOP;

   cur_ins_pol_obj_id := null;

   FOR mviews IN
     select a.premi, a.rate, a.ins_pol_obj_id from ins_pol_cover a where a.pol_id=v_pol_id order by ins_pol_obj_id, ins_cover_id
      LOOP

      if (cur_ins_pol_obj_id <> mviews.ins_pol_obj_id) then
         update ins_pol_obj set
            nd_premi1 = premi1,
            nd_premi2 = premi2,
            nd_premi3 = premi3,
            nd_premi4 = premi4,
            nd_premirate1 = premi1pct,
            nd_premirate2 = premi2pct,
            nd_premirate3 = premi3pct,
            nd_premirate4 = premi4pct
         where
            ins_pol_obj_id=mviews.ins_pol_obj_id;

         premi1 := null;
         premi2 := null;
         premi3 := null;
         premi4 := null;
         premi1pct := null;
         premi2pct := null;
         premi3pct := null;
         premi4pct := null;
      end if;

      cur_ins_pol_obj_id :=  mviews.ins_pol_obj_id;

      if (premi1 is null) then
         premi1:=mviews.premi;
         premi1pct:=mviews.rate;
      elsif (premi2 is null)then
         premi2:=mviews.premi;
         premi2pct:=mviews.rate;
      elsif (premi3 is null)then
         premi3:=mviews.premi;
         premi3pct:=mviews.rate;
      elsif (premi4 is null)then
         premi4:=mviews.premi;
         premi4pct:=mviews.rate;
      end if;
   END LOOP;

   update ins_pol_obj set
      nd_premi1 = premi1,
      nd_premi2 = premi2,
      nd_premi3 = premi3,
      nd_premi4 = premi4,
      nd_premirate1 = premi1pct,
      nd_premirate2 = premi2pct,
      nd_premirate3 = premi3pct,
      nd_premirate4 = premi4pct
   where
      ins_pol_obj_id=cur_ins_pol_obj_id;

   update ins_policy set
      nd_comm1 = comm1,
      nd_comm2 = comm2,
      nd_comm3 = comm3,
      nd_comm4 = comm4,
      nd_brok1 = brok1,
      nd_brok1pct = brok1pct,
      nd_brok2 = brok2,
      nd_brok2pct = brok2pct,
      nd_hfee = hfee,
      nd_hfeepct = hfeepct,
      nd_sfee = sfee,
      nd_pcost = pdut,
      nd_disc1 = disc1,
      nd_disc2 = disc2,
      nd_disc1pct = disc1pct,
      nd_disc2pct = disc2pct,
      nd_premi1 = premi1,
      nd_premi2 = premi2,
      nd_premi3 = premi3,
      nd_premi4 = premi4,
      nd_premirate1 = premi1pct,
      nd_premirate2 = premi2pct,
      nd_premirate3 = premi3pct,
      nd_premirate4 = premi4pct,
      nd_update='Y'
    where pol_id=v_pol_id;

   RETURN 'OK';
END;
$$ LANGUAGE plpgsql;>>>

ALTER TABLE ff_detail ADD COLUMN desc_field_ref varchar(128);


ALTER TABLE ins_treaty_shares ADD COLUMN premi_rate numeric;
ALTER TABLE ins_treaty_shares ADD COLUMN premi_amount numeric;
ALTER TABLE ins_treaty_shares ADD COLUMN auto_rate_flag varchar(1);
ALTER TABLE ins_treaty_shares ADD COLUMN use_rate_flag varchar(1);
ALTER TABLE ins_treaty_shares ADD COLUMN ricomm_rate numeric;
ALTER TABLE ins_treaty_shares ADD COLUMN ricomm_amt numeric;
ALTER TABLE ins_treaty_shares ADD COLUMN notes varchar(255);
ALTER TABLE ins_treaty_shares ADD COLUMN tsi_amount numeric;

ALTER TABLE ins_treaty_types ADD COLUMN non_proportional_flag varchar(1);


ALTER TABLE ins_pol_ri ADD COLUMN tsi_amount numeric;
ALTER TABLE ins_pol_ri ADD COLUMN premi_rate numeric;
ALTER TABLE ins_pol_ri ADD COLUMN auto_rate_flag varchar(1);
ALTER TABLE ins_pol_ri ADD COLUMN use_rate_flag varchar(1);
ALTER TABLE ins_pol_ri ADD COLUMN ricomm_rate numeric;
ALTER TABLE ins_pol_ri ADD COLUMN ricomm_amt numeric;
ALTER TABLE ins_pol_ri ADD COLUMN notes varchar(255);

ALTER TABLE ff_header ADD COLUMN ref1 varchar(64);
ALTER TABLE ins_pol_obj ADD COLUMN description2 varchar(255);


ALTER TABLE ins_pol_coins ADD COLUMN hfee_rate numeric;
ALTER TABLE ins_pol_coins ADD COLUMN hfee_amount numeric;

create or replace view branch as
select cc_code as branchid, description as branchname, create_who,change_who,create_date,change_date from gl_cost_center;

CREATE TABLE s_user_log
(
  user_log_id int8 NOT NULL,
  user_action  varchar(32),
  reference1  varchar(255) ,
  user_id  varchar(32),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT s_user_log_pk PRIMARY KEY (user_log_id)
) without oids;

ALTER TABLE s_users ADD COLUMN failed_login int8;
ALTER TABLE s_users ADD COLUMN failed_login_date timestamp;

ALTER TABLE s_roles ADD COLUMN transaction_limit numeric;

ALTER TABLE s_roles ADD COLUMN refn1 numeric;
ALTER TABLE s_roles ADD COLUMN refn2 numeric;
ALTER TABLE s_roles ADD COLUMN refn3 numeric;

CREATE TABLE ins_pol_nomerator
(
  ins_pol_nomerator_id varchar(16) NOT NULL,
  cc_code int8,
  policy_id int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_nomerator_pk PRIMARY KEY (ins_pol_nomerator_id)
) without oids;

ALTER TABLE ins_pol_ri ADD COLUMN ri_slip_no varchar(32);

ALTER TABLE s_functions ALTER function_name TYPE varchar(255);

ALTER TABLE ins_items ADD COLUMN item_code varchar(8);

ALTER TABLE ins_policy ADD COLUMN pfx_clauses varchar(1);
ALTER TABLE ins_policy ADD COLUMN pfx_interest varchar(1);
ALTER TABLE ins_policy ADD COLUMN pfx_coverage varchar(1);
ALTER TABLE ins_policy ADD COLUMN pfx_deductible varchar(1);

<<<CREATE OR REPLACE FUNCTION trg_ar_inv_x_bal()
  RETURNS "trigger" AS
$BODY$
DECLARE
	cnt numeric;
    BEGIN
        if ((TG_OP='INSERT') or (TG_OP='UPDATE')) then
          select into cnt count(1) from ar_bal where ent_id = NEW.ent_id;
          if (cnt=0) THEN
            INSERT INTO ar_bal(ent_id) values(new.ent_id);
          end if;
        end if;
        if ((TG_OP='DELETE') or (TG_OP='UPDATE')) then
            if (old.commit_flag='Y') then
                if (old.invoice_type='AR') then
                    update ar_bal set bal_ar=coalesce(bal_ar,0)-coalesce(old.amount,0) where ent_id=OLD.ent_id;
                else
                    update ar_bal set bal_ap=coalesce(bal_ap,0)-coalesce(old.amount,0) where ent_id=OLD.ent_id;
                end if;
            end if;
	    end if;
        if ((TG_OP='INSERT') or (TG_OP='UPDATE')) then
            if (new.commit_flag='Y') then
                if (new.invoice_type='AR') then
                    update ar_bal set bal_ar=coalesce(bal_ar,0)+coalesce(new.amount,0) where ent_id=NEW.ent_id;
                else
                    update ar_bal set bal_ap=coalesce(bal_ap,0)+coalesce(new.amount,0) where ent_id=NEW.ent_id;
                end if;
            end if;
        end if;
        RETURN NULL;
    END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE;>>>

ALTER TABLE receipt_class ADD COLUMN excess_account_neg varchar(32);
ALTER TABLE receipt_class ADD COLUMN rate_diff_acc varchar(32);
ALTER TABLE receipt_class ADD COLUMN rate_diff_acc_neg varchar(32);

ALTER TABLE ar_receipt ADD COLUMN ar_ap_invoice_id int8;

CREATE TABLE receipt_class_item
(
  rc_item_id int8 NOT NULL,
  rc_id int8,
  description  varchar(128),
  chrg_account  varchar(128),
  chrg_account_neg  varchar(128),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT receipt_class_item_pk PRIMARY KEY (rc_item_id)
) without oids;

CREATE TABLE ar_settlement
(
  ar_settlement_id int8 NOT NULL,
  description  varchar(128),
  arap_account  varchar(128),
  arap_account_neg varchar(128),
  excess_account varchar(128),
  excess_account_neg varchar(128),
  rate_diff_account varchar(128),
  rate_diff_account_neg varchar(128),
  arap_trx_type_id int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ar_settlement_pk PRIMARY KEY (ar_settlement_id)
) without oids;

ALTER TABLE ar_settlement ADD COLUMN control_flags varchar(128);

ALTER TABLE ar_settlement ADD COLUMN trx_type varchar(2);

ALTER TABLE ar_receipt ADD COLUMN ar_settlement_id int8;
ALTER TABLE ar_receipt ADD COLUMN entity_id int8;

ALTER TABLE ar_receipt_lines ADD COLUMN f_expanded varchar(1);

ALTER TABLE ar_settlement ADD COLUMN excess_description varchar(128);

CREATE TABLE ar_settlement_excess
(
  ar_settlement_xc_id int8 NOT NULL,
  description  varchar(128),
  f_negative  varchar(1),
  f_positive  varchar(1),
  gl_account  varchar(128),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ar_settlement_excess_pk PRIMARY KEY (ar_settlement_xc_id)
) without oids;

ALTER TABLE ar_settlement_excess ADD COLUMN ar_settlement_id int8;

ALTER TABLE ar_receipt_lines ADD COLUMN ar_settlement_xc_id int8;

ALTER TABLE ins_policy ADD COLUMN claim_ded_amount numeric;

ALTER TABLE ins_policy ADD COLUMN premi_pay_date timestamp;

ALTER TABLE ins_items ADD COLUMN ins_item_class varchar(32);

ALTER TABLE ins_period ADD COLUMN order_seq int8;

ALTER TABLE ins_policy ADD COLUMN claim_amount numeric;
ALTER TABLE ins_policy ADD COLUMN claim_amount_approved numeric;

update ins_items set ins_item_class='POL_'||ins_cover_source_id;

ALTER TABLE ins_pol_items ADD COLUMN item_class varchar(3);

update ins_pol_items set item_class='PRM';
ALTER TABLE ins_pol_items ADD COLUMN f_ro varchar(1);

ALTER TABLE ins_treaty_types ADD COLUMN free_tsi_flag varchar(1);

ALTER TABLE ins_pol_treaty_detail ADD COLUMN base_tsi_amount numeric;

ALTER TABLE ins_treaty_types ADD COLUMN use_rate_flag varchar(1);

ALTER TABLE ins_treaty_shares ADD COLUMN control_flags varchar(128);

ALTER TABLE ins_pol_ri ADD COLUMN control_flags varchar(128);

ALTER TABLE ins_policy ADD COLUMN pla_no varchar(64);

ALTER TABLE ins_policy ADD COLUMN dla_remark varchar(255);


ALTER TABLE ins_pol_items ADD COLUMN f_tax_auto_rate varchar(1);
ALTER TABLE ins_pol_items ADD COLUMN tax_amount numeric;
ALTER TABLE ins_pol_items ADD COLUMN tax_rate numeric;
ALTER TABLE ins_pol_items ADD COLUMN f_tax_auto_amount varchar(1);

ALTER TABLE ar_invoice_details ADD COLUMN ref_invoice_dtl_id int8;

ALTER TABLE receipt_class ADD COLUMN method_code varchar(5);

ALTER TABLE payment_method ADD COLUMN bank_code varchar(8);
ALTER TABLE payment_method ADD COLUMN cc_code varchar(8);

CREATE TABLE ins_document_type
(
  ins_document_type_id int8 NOT NULL,
  description  varchar(128),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_document_pk PRIMARY KEY (ins_document_type_id)
) without oids;

CREATE TABLE ins_pol_documents
(
  ins_pol_document_id int8 NOT NULL,
  ins_document_type_id int8,
  document_class  varchar(8),
  file_physic int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_documents_pk PRIMARY KEY (ins_pol_document_id)
) without oids;

CREATE TABLE s_files
(
  file_id int8 NOT NULL,
  orig_name  varchar(255),
  file_path  varchar(255),
  file_size int8,
  file_date timestamp,
  file_type  varchar(64),
  mime_type  varchar(64),
  compressed_flag  varchar(1),
  orig_size int8,
  parent_id int8,
  image_width int8,
  image_height int8,
  image_colors int8,
  folder_flag  varchar(1),
  thumb_flag  varchar(1),
  file_group  varchar(255),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT s_files_pk PRIMARY KEY (file_id)
) without oids;

CREATE TABLE ins_documents
(
  ins_documents_id int8 NOT NULL,
  ins_document_type_id int8,
  pol_type_id int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_documents_pk PRIMARY KEY (ins_documents_id)
) without oids;

ALTER TABLE ins_documents ADD COLUMN document_class varchar(8);

ALTER TABLE ins_pol_documents ADD COLUMN policy_id int8;
ALTER TABLE ins_pol_documents ADD COLUMN ins_pol_obj_id int8;

ALTER TABLE s_files ADD COLUMN description varchar(255);
ALTER TABLE s_files ADD COLUMN image_flag varchar(1);

ALTER TABLE ins_pol_items ADD COLUMN amount_settled numeric;
ALTER TABLE ins_pol_items ADD COLUMN f_settled varchar(1);

ALTER TABLE ins_policy ADD COLUMN claim_cust_amount numeric;

ALTER TABLE ins_policy ADD COLUMN claim_cust_ded_amount numeric;

ALTER TABLE ins_pol_items ADD COLUMN f_chargable varchar(1);

ALTER TABLE ar_invoice ADD COLUMN refx0 varchar(8);
ALTER TABLE ar_invoice ADD COLUMN refx1 varchar(64);
ALTER TABLE ar_invoice ADD COLUMN refy0 varchar(8);
ALTER TABLE ar_invoice ADD COLUMN refy1 varchar(64);
ALTER TABLE ar_invoice ADD COLUMN refz0 varchar(8);
ALTER TABLE ar_invoice ADD COLUMN refz1 varchar(64);
ALTER TABLE ar_invoice ADD COLUMN refa0 varchar(8);
ALTER TABLE ar_invoice ADD COLUMN refa1 varchar(64);
ALTER TABLE ar_invoice ADD COLUMN refc0 varchar(8);
ALTER TABLE ar_invoice ADD COLUMN refc1 varchar(64);
ALTER TABLE ar_invoice ADD COLUMN refd0 varchar(8);
ALTER TABLE ar_invoice ADD COLUMN refd1 varchar(64);
ALTER TABLE ar_invoice ADD COLUMN refe0 varchar(8);
ALTER TABLE ar_invoice ADD COLUMN refe1 varchar(64);

ALTER TABLE ins_treaty_types ADD COLUMN treaty_type_gl_code varchar(8);
ALTER TABLE ins_treaty_detail ADD COLUMN ar_trx_line_id int8;

ALTER TABLE ar_trx_line ADD COLUMN item_class varchar(8);

ALTER TABLE ins_policy ADD COLUMN claim_ri_amount numeric;

ALTER TABLE ins_treaty_types ADD COLUMN treaty_type_gl_code2 varchar(8);
ALTER TABLE ins_treaty_types ADD COLUMN treaty_type_gl_code3 varchar(8);

ALTER TABLE ins_policy ADD COLUMN claim_object_id int8;

ALTER TABLE ins_treaty_detail ADD COLUMN xol_lower numeric;
ALTER TABLE ins_treaty_detail ADD COLUMN xol_upper numeric;

ALTER TABLE ins_pol_treaty_detail ADD COLUMN claim_amount numeric;

ALTER TABLE ins_pol_ri ADD COLUMN claim_amount numeric;

ALTER TABLE ins_items ADD COLUMN entity_flag varchar(1);

ALTER TABLE ins_treaty_types ADD COLUMN treaty_type_gl_code4 varchar(8);
ALTER TABLE ins_treaty_types ADD COLUMN treaty_type_gl_code5 varchar(8);

ALTER TABLE ins_treaty_detail ADD COLUMN ar_trx_line_id_clm int8;

ALTER TABLE ins_risk_cat ADD COLUMN rate1 numeric;
ALTER TABLE ins_risk_cat ADD COLUMN rate2 numeric;
ALTER TABLE ins_risk_cat ADD COLUMN rate3 numeric;
ALTER TABLE ins_risk_cat ADD COLUMN rate0 numeric;

ALTER TABLE ent_master ADD COLUMN sharef0 varchar(32);
ALTER TABLE ent_master ADD COLUMN sharef1 varchar(32);
ALTER TABLE ent_master ADD COLUMN sharef2 varchar(32);

update ent_master set sharef2='NAT' where sharef0 is null;
ALTER TABLE ar_invoice ADD COLUMN no_journal_flag varchar(1);

ALTER TABLE gl_je_detail ADD COLUMN reverse_flag varchar(1);

ALTER TABLE ins_policy_types ADD COLUMN short_desc varchar(64);

ALTER TABLE gl_je_detail ADD COLUMN ref_trx_type varchar(8);
ALTER TABLE gl_je_detail ADD COLUMN ref_trx_no varchar(32);

update gl_je_detail set ref_trx_type=split_part(ref_trx,'/',1) where ref_trx_type is null;

update gl_je_detail set ref_trx_no=split_part(ref_trx,'/',2) where ref_trx_no is null;

ALTER TABLE gl_je_detail ADD COLUMN ref_trx_det_no varchar(32);

ALTER TABLE ent_address ALTER addr_type TYPE varchar(8);

ALTER TABLE ins_pol_ri ADD COLUMN f_approve varchar(1);

ALTER TABLE ins_policy ADD COLUMN f_ri_finish varchar(1);

ALTER TABLE ins_policy ADD COLUMN print_stamp varchar(64);

ALTER TABLE s_parameter
   ALTER COLUMN param_desc DROP NOT NULL;

ALTER TABLE s_parameter
   ALTER COLUMN param_group DROP NOT NULL;

ALTER TABLE s_parameter
   ALTER COLUMN param_order DROP NOT NULL;

ALTER TABLE ins_clausules ADD COLUMN ALTER TABLE ins_clausules ADD COLUMN f_default varchar(1);
varchar(1);

ALTER TABLE ins_treaty_types ADD COLUMN treaty_class varchar(16);

ALTER TABLE ins_treaty_detail ADD COLUMN period_start timestamp;
ALTER TABLE ins_treaty_detail ADD COLUMN period_end timestamp;

ALTER TABLE ins_policy_types ADD COLUMN ins_cover_source_id int8;

ALTER TABLE ins_treaty ADD COLUMN treaty_class varchar(16);

update ins_treaty set treaty_class='RE' where treaty_class is null;

ALTER TABLE ins_policy ADD COLUMN co_treaty_id int8;

ALTER TABLE ins_clausules ADD COLUMN f_default varchar(1);

ALTER TABLE ins_rates_big ADD COLUMN refid1 int8;

ALTER TABLE ins_policy_types DROP COLUMN ins_cover_source_id;
ALTER TABLE ins_policy_types ADD COLUMN ins_cover_source_id varchar(32);

ALTER TABLE ins_pol_coins ADD COLUMN f_auto_premi varchar(1);

CREATE TABLE ff_table
(
  fft_id int8 NOT NULL,
  ref1  varchar(255),
  ref2  varchar(255),
  ref3  varchar(255),
  ref4  varchar(255),
  ref5  varchar(255),
  refid1 int8,
  refid2 int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ff_table_pk PRIMARY KEY (fft_id)
) without oids;

ALTER TABLE ff_table ADD COLUMN fft_group_id varchar(32);
ALTER TABLE ff_table ADD COLUMN ref6 varchar(255);
ALTER TABLE ff_table ADD COLUMN ref7 varchar(255);

ALTER TABLE gl_accounts ADD COLUMN accountno2 varchar(32);

CREATE INDEX idx_gl_accounts_1 ON gl_accounts (accountno);

ALTER TABLE gl_accounts ADD COLUMN cc_code varchar(8);
ALTER TABLE gl_accounts ADD COLUMN f_cash_flow varchar(1);

ALTER TABLE payment_method ADD COLUMN ext_account_no varchar(32);

ALTER TABLE ar_invoice ADD COLUMN f_hide varchar(1);
ALTER TABLE ar_invoice ADD COLUMN f_npostable varchar(1);

ALTER TABLE ar_receipt ADD COLUMN status varchar(8);

ALTER TABLE ar_receipt ADD COLUMN status varchar(8);

update ar_invoice set commit_flag='Y' where commit_flag is null;

ALTER TABLE ar_invoice ADD COLUMN refid0 varchar(32);
ALTER TABLE ar_invoice ADD COLUMN refid1 varchar(32);
ALTER TABLE ar_invoice ADD COLUMN refid2 varchar(32);

ALTER TABLE ar_invoice_details ADD COLUMN refid0 varchar(32);
ALTER TABLE ar_invoice_details ADD COLUMN refid1 varchar(32);
ALTER TABLE ar_invoice_details ADD COLUMN refid2 varchar(32);

ALTER TABLE ins_pol_coins ADD COLUMN claim_amt numeric;
ALTER TABLE ins_pol_coins ADD COLUMN f_auto_clmamt varchar(1);
ALTER TABLE ins_cover_source ADD COLUMN claim_trx_type_id int8;

create or replace view ar_pol as
select c.ins_item_cat,a.amount,a.amount_settled,b.pol_id from ar_invoice a, ins_pol_items b, ins_items c
where a.refid2='POLI/' || ins_pol_item_id and
b.ins_item_id=c.ins_item_id and commit_flag='Y';

ALTER TABLE ins_pol_cover ADD COLUMN rate_scale varchar(1);

update ins_pol_cover set rate_scale='%' where rate_scale is null;

ALTER TABLE ins_premium_factor ADD COLUMN ref1 varchar(16);
ALTER TABLE ins_premium_factor ADD COLUMN f_default varchar(1);
ALTER TABLE ins_premium_factor ADD COLUMN f_active varchar(1);

CREATE TABLE ins_rates_bigh
(
  ins_rates_hdr_id int8 NOT NULL,
  description varchar(32),
  rate_class varchar(32),
  period_start timestamp,
  period_end timestamp,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_rates_bigh_pk PRIMARY KEY (ins_rates_hdr_id)
) without oids;

ALTER TABLE ins_rates_big ADD COLUMN ins_rates_hdr_id int8;
ALTER TABLE ins_rates_big ADD COLUMN change_date timestamp;
ALTER TABLE ins_rates_big ADD COLUMN change_who varchar(32);

ALTER TABLE ff_table ADD COLUMN refn1 numeric;

ALTER TABLE ins_pol_obj ADD COLUMN ins_pol_obj_ref_root_id int8;

ALTER TABLE ent_master ADD COLUMN ref1 varchar(32);
ALTER TABLE ent_master ADD COLUMN ref2 varchar(32);
ALTER TABLE ent_master ADD COLUMN ref3 varchar(32);
ALTER TABLE ent_master ADD COLUMN ref4 varchar(32);
ALTER TABLE ent_master ADD COLUMN ref5 varchar(32);
ALTER TABLE ent_master ADD COLUMN ref6 varchar(32);
ALTER TABLE ent_master ADD COLUMN ref7 varchar(32);

ALTER TABLE ent_master ADD COLUMN cc_code varchar(32);

ALTER TABLE ins_pol_obj ADD COLUMN ref1d varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref2d varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref3d varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref4d varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref5d varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref6d varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref7d varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref8d varchar(255);
ALTER TABLE ins_pol_obj ADD COLUMN ref9d varchar(255);

ALTER TABLE gl_je_detail ADD COLUMN ref_ent_id int8;
ALTER TABLE ff_table ADD COLUMN refn2 numeric;
ALTER TABLE ff_table ADD COLUMN refn3 numeric;
