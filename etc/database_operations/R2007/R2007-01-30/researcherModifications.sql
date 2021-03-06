alter table RESULT drop column KEY_RESULT_DOCUMENT_FILE;
alter table RESULT add column KEY_CREATOR int(11) NOT NULL;
alter table RESULT add COLUMN CONFERENCE varchar(255) ;
alter table RESULT add column KEYWORDS longtext;
alter table RESULT add column UNIQUE_STORAGE_ID varchar(255);

alter table RESULT add key `KEY_PUBLISHER` (`KEY_PUBLISHER`);
alter table RESULT add key `KEY_ORGANIZATION` (`KEY_ORGANIZATION`);
alter table RESULT add key `KEY_EVENT` (`KEY_EVENT`);
alter table RESULT add key `KEY_CREATOR` (`KEY_CREATOR`);

alter table RESULT_PARTICIPATION add key `KEY_PERSON` (`KEY_PERSON`);
alter table RESULT_PARTICIPATION add key `KEY_RESULT` (`KEY_RESULT`);
alter table RESULT_PARTICIPATION add key `KEY_ROOT_DOMAIN_OBJECT` (`KEY_ROOT_DOMAIN_OBJECT`);

alter table RESULT_TEACHER add key `KEY_TEACHER` (`KEY_TEACHER`);
alter table RESULT_TEACHER add key `KEY_RESULT` (`KEY_RESULT`);
alter table RESULT_TEACHER add key `KEY_ROOT_DOMAIN_OBJECT` (`KEY_ROOT_DOMAIN_OBJECT`);


alter table RESULT add column UNIQUE_STORAGE_ID varchar(255);
