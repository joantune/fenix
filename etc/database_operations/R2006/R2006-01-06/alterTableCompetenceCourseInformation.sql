alter table COMPETENCE_COURSE_INFORMATION drop column KEY_EXECUTION_YEAR;
alter table COMPETENCE_COURSE_INFORMATION drop column ECTS_CREDITS;
alter table COMPETENCE_COURSE_INFORMATION drop column THEORETICAL_HOURS;
alter table COMPETENCE_COURSE_INFORMATION drop column LAB_HOURS;
alter table COMPETENCE_COURSE_INFORMATION drop column CODE;
alter table COMPETENCE_COURSE_INFORMATION drop column PROBLEMS_HOURS;
alter table COMPETENCE_COURSE_INFORMATION drop column PROJECT_HOURS;
alter table COMPETENCE_COURSE_INFORMATION drop column SEMINARY_HOURS;
alter table COMPETENCE_COURSE_INFORMATION drop column GENERAL_OBJECTIVES;
alter table COMPETENCE_COURSE_INFORMATION drop column OPERATIONAL_OBJECTIVES;
alter table COMPETENCE_COURSE_INFORMATION drop column PREREQUISITES;
alter table COMPETENCE_COURSE_INFORMATION drop column GENERAL_OBJECTIVES_EN;
alter table COMPETENCE_COURSE_INFORMATION drop column OPERATIONAL_OBJECTIVES_EN;
alter table COMPETENCE_COURSE_INFORMATION drop column PREREQUISITES_EN;

alter table COMPETENCE_COURSE_INFORMATION add column ACRONYM varchar(50) NOT NULL default '';
alter table COMPETENCE_COURSE_INFORMATION add column OBJECTIVES text;
alter table COMPETENCE_COURSE_INFORMATION add column OBJECTIVES_EN text;
alter table COMPETENCE_COURSE_INFORMATION add column END_DATE date default NULL;
