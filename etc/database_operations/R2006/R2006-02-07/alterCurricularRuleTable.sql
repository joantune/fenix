ALTER TABLE CURRICULAR_RULE CHANGE COLUMN KEY_DEGREE_MODULE_TO_APPLY_RULE KEY_DEGREE_MODULE_TO_APPLY_RULE int(11) default NULL;
ALTER TABLE CURRICULAR_RULE CHANGE COLUMN KEY_BEGIN_EXECUTION_PERIOD KEY_BEGIN_EXECUTION_PERIOD int(11) default NULL;
ALTER TABLE CURRICULAR_RULE CHANGE COLUMN CURRICULAR_RULE_TYPE CURRICULAR_RULE_TYPE varchar(100) default NULL;
ALTER TABLE CURRICULAR_RULE CHANGE COLUMN KEY_DONE_DEGREE_MODULE KEY_PRECEDENCE_DEGREE_MODULE int(11) default NULL;

ALTER TABLE CURRICULAR_RULE ADD COLUMN KEY_CONTEXT_COURSE_GROUP int(11) default NULL;
ALTER TABLE CURRICULAR_RULE ADD COLUMN CURRICULAR_PERIOD_TYPE varchar(100) default NULL;
ALTER TABLE CURRICULAR_RULE ADD COLUMN MINIMUM double default 0;
ALTER TABLE CURRICULAR_RULE ADD COLUMN MAXIMUM double default 0;
