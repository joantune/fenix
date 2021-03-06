SET AUTOCOMMIT = 0;

START TRANSACTION;

INSERT INTO EXECUTION_PATH (EXECUTION_PATH,CONTENT_ID,KEY_FUNCTIONALITY,KEY_ROOT_DOMAIN_OBJECT) VALUES ('/exportAssiduousness.do?method=prepareExportADISTEmployees','bca75161-0bc0-48f7-b385-bb5ec51d8035',1,1)  ;

INSERT INTO CONTENT (EXECUTION_PATH,VISIBLE,KEY_EXECUTION_PATH_VALUE,CONTENT_ID,CREATION_DATE,NAME,TITLE,BODY,DESCRIPTION,NORMALIZED_NAME,KEY_ROOT_DOMAIN_OBJECT,KEY_AVAILABILITY_POLICY,KEY_PORTAL,KEY_CREATOR,OJB_CONCRETE_CLASS) VALUES ('/exportAssiduousness.do?method=prepareExportADISTEmployees',null,2,'5423673f-c9c0-4124-996b-cb3c07d36c83','2008-02-13 11:05:07','pt14:Exportar ADIST',null,null,null,'pt14:exportar-adist',1,null,null,null,'net.sourceforge.fenixedu.domain.functionalities.Functionality')  ;

INSERT INTO NODE (NODE_ORDER,ASCENDING,VISIBLE,CONTENT_ID,KEY_ROOT_DOMAIN_OBJECT,KEY_PARENT,KEY_CHILD,OJB_CONCRETE_CLASS) VALUES (4,1,1,'11279eeb-6e02-4374-a255-fc140930f111:5423673f-c9c0-4124-996b-cb3c07d36c83',1,3,4,'net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode')  ;

UPDATE EXECUTION_PATH SET EXECUTION_PATH='/exportAssiduousness.do?method=prepareExportADISTEmployees',CONTENT_ID='bca75161-0bc0-48f7-b385-bb5ec51d8035',KEY_FUNCTIONALITY=5,KEY_ROOT_DOMAIN_OBJECT=1 WHERE CONTENT_ID = 'bca75161-0bc0-48f7-b385-bb5ec51d8035'  ;

UPDATE CONTENT SET EXECUTION_PATH='/exportAssiduousness.do?method=prepareExportADISTEmployees',VISIBLE=null,KEY_EXECUTION_PATH_VALUE=6,CONTENT_ID='5423673f-c9c0-4124-996b-cb3c07d36c83',CREATION_DATE='2008-02-13 11:05:07',NAME='pt14:Exportar ADIST',TITLE=null,BODY=null,DESCRIPTION=null,NORMALIZED_NAME='pt14:exportar-adist',KEY_ROOT_DOMAIN_OBJECT=1,KEY_AVAILABILITY_POLICY=null,KEY_PORTAL=null,KEY_CREATOR=null,OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.functionalities.Functionality' WHERE CONTENT_ID = '5423673f-c9c0-4124-996b-cb3c07d36c83'  ;

UPDATE NODE SET NODE_ORDER=4,ASCENDING=1,VISIBLE=1,CONTENT_ID='11279eeb-6e02-4374-a255-fc140930f111:5423673f-c9c0-4124-996b-cb3c07d36c83',KEY_ROOT_DOMAIN_OBJECT=1,KEY_PARENT=7,KEY_CHILD=8,OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode' WHERE CONTENT_ID = '11279eeb-6e02-4374-a255-fc140930f111:5423673f-c9c0-4124-996b-cb3c07d36c83'  ;

CREATE TEMPORARY TABLE UUID_TEMP_TABLE(COUNTER integer, UUID varchar(255), FROM_UUID varchar(255));

INSERT INTO UUID_TEMP_TABLE(COUNTER,UUID,FROM_UUID) VALUES(1,'5423673f-c9c0-4124-996b-cb3c07d36c83','bca75161-0bc0-48f7-b385-bb5ec51d8035') ;
INSERT INTO UUID_TEMP_TABLE(COUNTER,UUID,FROM_UUID) VALUES(2,'bca75161-0bc0-48f7-b385-bb5ec51d8035','5423673f-c9c0-4124-996b-cb3c07d36c83') ;
INSERT INTO UUID_TEMP_TABLE(COUNTER,UUID,FROM_UUID) VALUES(3,'11279eeb-6e02-4374-a255-fc140930f111','11279eeb-6e02-4374-a255-fc140930f111:5423673f-c9c0-4124-996b-cb3c07d36c83') ;
INSERT INTO UUID_TEMP_TABLE(COUNTER,UUID,FROM_UUID) VALUES(4,'5423673f-c9c0-4124-996b-cb3c07d36c83','11279eeb-6e02-4374-a255-fc140930f111:5423673f-c9c0-4124-996b-cb3c07d36c83') ;
INSERT INTO UUID_TEMP_TABLE(COUNTER,UUID,FROM_UUID) VALUES(5,'5423673f-c9c0-4124-996b-cb3c07d36c83','bca75161-0bc0-48f7-b385-bb5ec51d8035') ;
INSERT INTO UUID_TEMP_TABLE(COUNTER,UUID,FROM_UUID) VALUES(6,'bca75161-0bc0-48f7-b385-bb5ec51d8035','5423673f-c9c0-4124-996b-cb3c07d36c83') ;
INSERT INTO UUID_TEMP_TABLE(COUNTER,UUID,FROM_UUID) VALUES(7,'11279eeb-6e02-4374-a255-fc140930f111','11279eeb-6e02-4374-a255-fc140930f111:5423673f-c9c0-4124-996b-cb3c07d36c83') ;
INSERT INTO UUID_TEMP_TABLE(COUNTER,UUID,FROM_UUID) VALUES(8,'5423673f-c9c0-4124-996b-cb3c07d36c83','11279eeb-6e02-4374-a255-fc140930f111:5423673f-c9c0-4124-996b-cb3c07d36c83') ;
ALTER TABLE UUID_TEMP_TABLE ADD INDEX (COUNTER), ADD INDEX (UUID), ADD INDEX (FROM_UUID); 


UPDATE NODE T, UUID_TEMP_TABLE UIT, CONTENT CT set T.KEY_PARENT=CT.ID_INTERNAL WHERE T.KEY_PARENT=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;
UPDATE NODE T, UUID_TEMP_TABLE UIT, CONTENT CT set T.KEY_CHILD=CT.ID_INTERNAL WHERE T.KEY_CHILD=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;
UPDATE CONTENT T, UUID_TEMP_TABLE UIT, CONTENT CT set T.KEY_INITIAL_CONTENT=CT.ID_INTERNAL WHERE T.KEY_INITIAL_CONTENT=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;
UPDATE CONTENT T, UUID_TEMP_TABLE UIT, EXECUTION_PATH CT set T.KEY_EXECUTION_PATH_VALUE=CT.ID_INTERNAL WHERE T.KEY_EXECUTION_PATH_VALUE=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;
UPDATE CONTENT T, UUID_TEMP_TABLE UIT, CONTENT CT set T.KEY_PORTAL=CT.ID_INTERNAL WHERE T.KEY_PORTAL=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;
UPDATE CONTENT T, UUID_TEMP_TABLE UIT, AVAILABILITY_POLICY CT set T.KEY_AVAILABILITY_POLICY=CT.ID_INTERNAL WHERE T.KEY_AVAILABILITY_POLICY=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;
UPDATE AVAILABILITY_POLICY T, UUID_TEMP_TABLE UIT, CONTENT CT set T.KEY_CONTENT=CT.ID_INTERNAL WHERE T.KEY_CONTENT=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;
UPDATE EXECUTION_PATH T, UUID_TEMP_TABLE UIT, CONTENT CT set T.KEY_FUNCTIONALITY=CT.ID_INTERNAL WHERE T.KEY_FUNCTIONALITY=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;
DROP TABLE UUID_TEMP_TABLE;

INSERT INTO NODE (NODE_ORDER,ASCENDING,VISIBLE,CONTENT_ID,KEY_ROOT_DOMAIN_OBJECT,KEY_PARENT,KEY_CHILD,OJB_CONCRETE_CLASS) VALUES (4,1,1,'004b472cf566a3fac5c49bf07809d810:5423673f-c9c0-4124-996b-cb3c07d36c83',1,9,10,'net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode')  ;

UPDATE NODE SET NODE_ORDER=4,ASCENDING=1,VISIBLE=1,CONTENT_ID='004b472cf566a3fac5c49bf07809d810:5423673f-c9c0-4124-996b-cb3c07d36c83',KEY_ROOT_DOMAIN_OBJECT=1,KEY_PARENT=11,KEY_CHILD=12,OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode' WHERE CONTENT_ID = '004b472cf566a3fac5c49bf07809d810:5423673f-c9c0-4124-996b-cb3c07d36c83'  ;

CREATE TEMPORARY TABLE UUID_TEMP_TABLE(COUNTER integer, UUID varchar(255), FROM_UUID varchar(255));

INSERT INTO UUID_TEMP_TABLE(COUNTER,UUID,FROM_UUID) VALUES(9,'004b472cf566a3fac5c49bf07809d810','004b472cf566a3fac5c49bf07809d810:5423673f-c9c0-4124-996b-cb3c07d36c83') ;
INSERT INTO UUID_TEMP_TABLE(COUNTER,UUID,FROM_UUID) VALUES(10,'5423673f-c9c0-4124-996b-cb3c07d36c83','004b472cf566a3fac5c49bf07809d810:5423673f-c9c0-4124-996b-cb3c07d36c83') ;
INSERT INTO UUID_TEMP_TABLE(COUNTER,UUID,FROM_UUID) VALUES(11,'004b472cf566a3fac5c49bf07809d810','004b472cf566a3fac5c49bf07809d810:5423673f-c9c0-4124-996b-cb3c07d36c83') ;
INSERT INTO UUID_TEMP_TABLE(COUNTER,UUID,FROM_UUID) VALUES(12,'5423673f-c9c0-4124-996b-cb3c07d36c83','004b472cf566a3fac5c49bf07809d810:5423673f-c9c0-4124-996b-cb3c07d36c83') ;
ALTER TABLE UUID_TEMP_TABLE ADD INDEX (COUNTER), ADD INDEX (UUID), ADD INDEX (FROM_UUID); 


UPDATE NODE T, UUID_TEMP_TABLE UIT, CONTENT CT set T.KEY_PARENT=CT.ID_INTERNAL WHERE T.KEY_PARENT=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;
UPDATE NODE T, UUID_TEMP_TABLE UIT, CONTENT CT set T.KEY_CHILD=CT.ID_INTERNAL WHERE T.KEY_CHILD=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;
UPDATE CONTENT T, UUID_TEMP_TABLE UIT, CONTENT CT set T.KEY_INITIAL_CONTENT=CT.ID_INTERNAL WHERE T.KEY_INITIAL_CONTENT=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;
UPDATE CONTENT T, UUID_TEMP_TABLE UIT, EXECUTION_PATH CT set T.KEY_EXECUTION_PATH_VALUE=CT.ID_INTERNAL WHERE T.KEY_EXECUTION_PATH_VALUE=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;
UPDATE CONTENT T, UUID_TEMP_TABLE UIT, CONTENT CT set T.KEY_PORTAL=CT.ID_INTERNAL WHERE T.KEY_PORTAL=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;
UPDATE CONTENT T, UUID_TEMP_TABLE UIT, AVAILABILITY_POLICY CT set T.KEY_AVAILABILITY_POLICY=CT.ID_INTERNAL WHERE T.KEY_AVAILABILITY_POLICY=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;
UPDATE AVAILABILITY_POLICY T, UUID_TEMP_TABLE UIT, CONTENT CT set T.KEY_CONTENT=CT.ID_INTERNAL WHERE T.KEY_CONTENT=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;
UPDATE EXECUTION_PATH T, UUID_TEMP_TABLE UIT, CONTENT CT set T.KEY_FUNCTIONALITY=CT.ID_INTERNAL WHERE T.KEY_FUNCTIONALITY=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;
DROP TABLE UUID_TEMP_TABLE;

UPDATE NODE SET NODE_ORDER=3,ASCENDING=1,VISIBLE=1,CONTENT_ID='004b472cf566a3fac5c49bf07809d810:01b9d1a4-259d-4d73-8503-f56c2556b591',KEY_ROOT_DOMAIN_OBJECT=1,KEY_PARENT=13,KEY_CHILD=14,OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode' WHERE CONTENT_ID = '004b472cf566a3fac5c49bf07809d810:01b9d1a4-259d-4d73-8503-f56c2556b591'  ;

UPDATE NODE SET NODE_ORDER=1,ASCENDING=1,VISIBLE=1,CONTENT_ID='004b472cf566a3fac5c49bf07809d810:5c4825a6-724a-4d55-beb1-260603a64543',KEY_ROOT_DOMAIN_OBJECT=1,KEY_PARENT=15,KEY_CHILD=16,OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode' WHERE CONTENT_ID = '004b472cf566a3fac5c49bf07809d810:5c4825a6-724a-4d55-beb1-260603a64543'  ;

UPDATE NODE SET NODE_ORDER=4,ASCENDING=1,VISIBLE=1,CONTENT_ID='004b472cf566a3fac5c49bf07809d810:ad15f24f-f3f7-43c4-923e-b7789139d427',KEY_ROOT_DOMAIN_OBJECT=1,KEY_PARENT=17,KEY_CHILD=18,OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode' WHERE CONTENT_ID = '004b472cf566a3fac5c49bf07809d810:ad15f24f-f3f7-43c4-923e-b7789139d427'  ;

UPDATE NODE SET NODE_ORDER=2,ASCENDING=1,VISIBLE=1,CONTENT_ID='004b472cf566a3fac5c49bf07809d810:483a8089-9a65-44d5-a046-66edadb3d84d',KEY_ROOT_DOMAIN_OBJECT=1,KEY_PARENT=19,KEY_CHILD=20,OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode' WHERE CONTENT_ID = '004b472cf566a3fac5c49bf07809d810:483a8089-9a65-44d5-a046-66edadb3d84d'  ;

UPDATE NODE SET NODE_ORDER=5,ASCENDING=1,VISIBLE=1,CONTENT_ID='004b472cf566a3fac5c49bf07809d810:5423673f-c9c0-4124-996b-cb3c07d36c83',KEY_ROOT_DOMAIN_OBJECT=1,KEY_PARENT=21,KEY_CHILD=22,OJB_CONCRETE_CLASS='net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode' WHERE CONTENT_ID = '004b472cf566a3fac5c49bf07809d810:5423673f-c9c0-4124-996b-cb3c07d36c83'  ;

CREATE TEMPORARY TABLE UUID_TEMP_TABLE(COUNTER integer, UUID varchar(255), FROM_UUID varchar(255));

INSERT INTO UUID_TEMP_TABLE(COUNTER,UUID,FROM_UUID) VALUES(13,'004b472cf566a3fac5c49bf07809d810','004b472cf566a3fac5c49bf07809d810:01b9d1a4-259d-4d73-8503-f56c2556b591') ;
INSERT INTO UUID_TEMP_TABLE(COUNTER,UUID,FROM_UUID) VALUES(14,'01b9d1a4-259d-4d73-8503-f56c2556b591','004b472cf566a3fac5c49bf07809d810:01b9d1a4-259d-4d73-8503-f56c2556b591') ;
INSERT INTO UUID_TEMP_TABLE(COUNTER,UUID,FROM_UUID) VALUES(15,'004b472cf566a3fac5c49bf07809d810','004b472cf566a3fac5c49bf07809d810:5c4825a6-724a-4d55-beb1-260603a64543') ;
INSERT INTO UUID_TEMP_TABLE(COUNTER,UUID,FROM_UUID) VALUES(16,'5c4825a6-724a-4d55-beb1-260603a64543','004b472cf566a3fac5c49bf07809d810:5c4825a6-724a-4d55-beb1-260603a64543') ;
INSERT INTO UUID_TEMP_TABLE(COUNTER,UUID,FROM_UUID) VALUES(17,'004b472cf566a3fac5c49bf07809d810','004b472cf566a3fac5c49bf07809d810:ad15f24f-f3f7-43c4-923e-b7789139d427') ;
INSERT INTO UUID_TEMP_TABLE(COUNTER,UUID,FROM_UUID) VALUES(18,'ad15f24f-f3f7-43c4-923e-b7789139d427','004b472cf566a3fac5c49bf07809d810:ad15f24f-f3f7-43c4-923e-b7789139d427') ;
INSERT INTO UUID_TEMP_TABLE(COUNTER,UUID,FROM_UUID) VALUES(19,'004b472cf566a3fac5c49bf07809d810','004b472cf566a3fac5c49bf07809d810:483a8089-9a65-44d5-a046-66edadb3d84d') ;
INSERT INTO UUID_TEMP_TABLE(COUNTER,UUID,FROM_UUID) VALUES(20,'483a8089-9a65-44d5-a046-66edadb3d84d','004b472cf566a3fac5c49bf07809d810:483a8089-9a65-44d5-a046-66edadb3d84d') ;
INSERT INTO UUID_TEMP_TABLE(COUNTER,UUID,FROM_UUID) VALUES(21,'004b472cf566a3fac5c49bf07809d810','004b472cf566a3fac5c49bf07809d810:5423673f-c9c0-4124-996b-cb3c07d36c83') ;
INSERT INTO UUID_TEMP_TABLE(COUNTER,UUID,FROM_UUID) VALUES(22,'5423673f-c9c0-4124-996b-cb3c07d36c83','004b472cf566a3fac5c49bf07809d810:5423673f-c9c0-4124-996b-cb3c07d36c83') ;
ALTER TABLE UUID_TEMP_TABLE ADD INDEX (COUNTER), ADD INDEX (UUID), ADD INDEX (FROM_UUID); 


UPDATE NODE T, UUID_TEMP_TABLE UIT, CONTENT CT set T.KEY_PARENT=CT.ID_INTERNAL WHERE T.KEY_PARENT=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;
UPDATE NODE T, UUID_TEMP_TABLE UIT, CONTENT CT set T.KEY_CHILD=CT.ID_INTERNAL WHERE T.KEY_CHILD=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;
UPDATE CONTENT T, UUID_TEMP_TABLE UIT, CONTENT CT set T.KEY_INITIAL_CONTENT=CT.ID_INTERNAL WHERE T.KEY_INITIAL_CONTENT=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;
UPDATE CONTENT T, UUID_TEMP_TABLE UIT, EXECUTION_PATH CT set T.KEY_EXECUTION_PATH_VALUE=CT.ID_INTERNAL WHERE T.KEY_EXECUTION_PATH_VALUE=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;
UPDATE CONTENT T, UUID_TEMP_TABLE UIT, CONTENT CT set T.KEY_PORTAL=CT.ID_INTERNAL WHERE T.KEY_PORTAL=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;
UPDATE CONTENT T, UUID_TEMP_TABLE UIT, AVAILABILITY_POLICY CT set T.KEY_AVAILABILITY_POLICY=CT.ID_INTERNAL WHERE T.KEY_AVAILABILITY_POLICY=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;
UPDATE AVAILABILITY_POLICY T, UUID_TEMP_TABLE UIT, CONTENT CT set T.KEY_CONTENT=CT.ID_INTERNAL WHERE T.KEY_CONTENT=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;
UPDATE EXECUTION_PATH T, UUID_TEMP_TABLE UIT, CONTENT CT set T.KEY_FUNCTIONALITY=CT.ID_INTERNAL WHERE T.KEY_FUNCTIONALITY=UIT.COUNTER AND T.CONTENT_ID = UIT.FROM_UUID AND CT.CONTENT_ID=UIT.UUID;
DROP TABLE UUID_TEMP_TABLE;



COMMIT;
