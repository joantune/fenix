delete from TUTORSHIP where not exists (select * from STUDENT_CURRICULAR_PLAN where ID_INTERNAL = TUTORSHIP.KEY_STUDENT_CURRICULAR_PLAN );
