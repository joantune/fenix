update DEGREE set DEGREE.TIPO_CURSO = concat('BOLONHA_', DEGREE.BOLONHA_DEGREE_TYPE) where BOLONHA_DEGREE_TYPE is not null;