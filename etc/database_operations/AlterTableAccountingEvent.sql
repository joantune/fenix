alter table ACCOUNTING_EVENT CHANGE COLUMN KEY_REGISTRATION KEY_REGISTRATION_FOR_GRATUITY_EVENT int(11) NULL;
alter table ACCOUNTING_EVENT ADD COLUMN KEY_REGISTRATION_FOR_DFA_REGISTRATION_EVENT int(11) NULL;
alter table ACCOUNTING_EVENT ADD INDEX (KEY_REGISTRATION_FOR_GRATUITY_EVENT);
alter table ACCOUNTING_EVENT ADD INDEX (KEY_REGISTRATION_FOR_DFA_REGISTRATION_EVENT);
