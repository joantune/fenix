alter table INQUIRIES_COURSE add column HIGH_WORK_LOAD_REASON_CU_ORGANIZATION_PROBLEMS tinyint(1);
alter table INQUIRIES_COURSE add column HIGH_WORK_LOAD_REASON_PERSONAL_OR_TEAM_PROBLEMS tinyint(1);
alter table INQUIRIES_COURSE add column STUDY_METHOD_ATTEND_TO_CLASSES tinyint(1);
alter table INQUIRIES_COURSE add column STUDY_METHOD_OTHER text;
alter table INQUIRIES_COURSE add column STUDY_METHOD_STUDENT_DOCUMENTS tinyint(1);
alter table INQUIRIES_COURSE add column STUDY_METHOD_SUGGESTED_BIBLIOGRAPHY tinyint(1);
alter table INQUIRIES_COURSE add column STUDY_METHOD_TEACHER_DOCUMENTS tinyint(1);

