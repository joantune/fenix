--  SQL file representing changes to the functionalities model
--  Generated at Tue Jun 12 15:25:06 WEST 2007
--  DO NOT EDIT THIS FILE, run the generating script instead

--  Preamble
SET AUTOCOMMIT = 0;

START TRANSACTION;

-- 
--  Deleting functionalities
-- 

-- Deleting functionality sections
DELETE FROM A USING ACCESSIBLE_ITEM A, ACCESSIBLE_ITEM F 
WHERE A.KEY_FUNCTIONALITY = F.ID_INTERNAL 
  AND F.UUID IN ('96b1a0d4-6469-41b9-8de7-71caebe8f98c', '3ea20dd8-cbe0-4100-b341-7714dd3263ec');

--  ID: 26326 UUID: '96b1a0d4-6469-41b9-8de7-71caebe8f98c'
DELETE FROM ap USING `AVAILABILITY_POLICY` AS ap, `ACCESSIBLE_ITEM` AS f WHERE ap.`KEY_ACCESSIBLE_ITEM` = f.`ID_INTERNAL` AND f.`UUID` = '96b1a0d4-6469-41b9-8de7-71caebe8f98c';
DELETE FROM `ACCESSIBLE_ITEM` WHERE `UUID` = '96b1a0d4-6469-41b9-8de7-71caebe8f98c';

--  ID: 133471 UUID: '3ea20dd8-cbe0-4100-b341-7714dd3263ec'
DELETE FROM ap USING `AVAILABILITY_POLICY` AS ap, `ACCESSIBLE_ITEM` AS f WHERE ap.`KEY_ACCESSIBLE_ITEM` = f.`ID_INTERNAL` AND f.`UUID` = '3ea20dd8-cbe0-4100-b341-7714dd3263ec';
DELETE FROM `ACCESSIBLE_ITEM` WHERE `UUID` = '3ea20dd8-cbe0-4100-b341-7714dd3263ec';

-- 
--  Updating existing functionalities
-- 

--  ID: 26327 UUID: 'c113952d-6c49-44c0-b30b-d56ab1d2d047'
DELETE FROM ap USING `AVAILABILITY_POLICY` AS ap, `ACCESSIBLE_ITEM` AS f WHERE ap.`KEY_ACCESSIBLE_ITEM` = f.`ID_INTERNAL` AND f.`UUID` = 'c113952d-6c49-44c0-b30b-d56ab1d2d047';
UPDATE `ACCESSIBLE_ITEM` AS own SET own.`KEY_AVAILABILITY_POLICY` = NULL, own.`ORDER_IN_MODULE` = 0, own.`VISIBLE` = 1 WHERE own.`UUID` = 'c113952d-6c49-44c0-b30b-d56ab1d2d047';

--  ID: 26328 UUID: '173e00ae-66ff-408b-a8ce-d4a4011fe3f5'
DELETE FROM ap USING `AVAILABILITY_POLICY` AS ap, `ACCESSIBLE_ITEM` AS f WHERE ap.`KEY_ACCESSIBLE_ITEM` = f.`ID_INTERNAL` AND f.`UUID` = '173e00ae-66ff-408b-a8ce-d4a4011fe3f5';
UPDATE `ACCESSIBLE_ITEM` AS own SET own.`KEY_AVAILABILITY_POLICY` = NULL, own.`ORDER_IN_MODULE` = 2 WHERE own.`UUID` = '173e00ae-66ff-408b-a8ce-d4a4011fe3f5';

--  ID: 26329 UUID: '3bd0759b-edca-4ff7-87d6-6e03b6be1b3c'
DELETE FROM ap USING `AVAILABILITY_POLICY` AS ap, `ACCESSIBLE_ITEM` AS f WHERE ap.`KEY_ACCESSIBLE_ITEM` = f.`ID_INTERNAL` AND f.`UUID` = '3bd0759b-edca-4ff7-87d6-6e03b6be1b3c';
UPDATE `ACCESSIBLE_ITEM` AS own SET own.`KEY_AVAILABILITY_POLICY` = NULL, own.`ORDER_IN_MODULE` = 3 WHERE own.`UUID` = '3bd0759b-edca-4ff7-87d6-6e03b6be1b3c';

--  ID: 26330 UUID: '85372e2e-ffe0-48ff-a758-1017a11c797f'
DELETE FROM ap USING `AVAILABILITY_POLICY` AS ap, `ACCESSIBLE_ITEM` AS f WHERE ap.`KEY_ACCESSIBLE_ITEM` = f.`ID_INTERNAL` AND f.`UUID` = '85372e2e-ffe0-48ff-a758-1017a11c797f';
UPDATE `ACCESSIBLE_ITEM` AS own SET own.`KEY_AVAILABILITY_POLICY` = NULL, own.`ORDER_IN_MODULE` = 4 WHERE own.`UUID` = '85372e2e-ffe0-48ff-a758-1017a11c797f';

--  ID: 133474 UUID: '931438dc-b0e2-4d2a-b017-1a449adf59e3'
DELETE FROM ap USING `AVAILABILITY_POLICY` AS ap, `ACCESSIBLE_ITEM` AS f WHERE ap.`KEY_ACCESSIBLE_ITEM` = f.`ID_INTERNAL` AND f.`UUID` = '931438dc-b0e2-4d2a-b017-1a449adf59e3';
UPDATE `ACCESSIBLE_ITEM` AS own SET own.`KEY_AVAILABILITY_POLICY` = NULL, own.`ORDER_IN_MODULE` = 1, own.`VISIBLE` = 1 WHERE own.`UUID` = '931438dc-b0e2-4d2a-b017-1a449adf59e3';

--  ID: 140670 UUID: 'e9ab1a2b-cfd2-4c6d-9476-27c7c3caf4c6'
DELETE FROM ap USING `AVAILABILITY_POLICY` AS ap, `ACCESSIBLE_ITEM` AS f WHERE ap.`KEY_ACCESSIBLE_ITEM` = f.`ID_INTERNAL` AND f.`UUID` = 'e9ab1a2b-cfd2-4c6d-9476-27c7c3caf4c6';
UPDATE `ACCESSIBLE_ITEM` AS own SET own.`KEY_AVAILABILITY_POLICY` = NULL, own.`VISIBLE` = 1 WHERE own.`UUID` = 'e9ab1a2b-cfd2-4c6d-9476-27c7c3caf4c6';

--  ID: 140671 UUID: '8481d1a7-1b95-43f6-b091-e0d582aa86e5'
DELETE FROM ap USING `AVAILABILITY_POLICY` AS ap, `ACCESSIBLE_ITEM` AS f WHERE ap.`KEY_ACCESSIBLE_ITEM` = f.`ID_INTERNAL` AND f.`UUID` = '8481d1a7-1b95-43f6-b091-e0d582aa86e5';
UPDATE `ACCESSIBLE_ITEM` AS own SET own.`KEY_AVAILABILITY_POLICY` = NULL, own.`ORDER_IN_MODULE` = 0, own.`VISIBLE` = 1 WHERE own.`UUID` = '8481d1a7-1b95-43f6-b091-e0d582aa86e5';

--  ID: 140674 UUID: '95b0bf15-1f4d-4430-815a-dfc1fb373ea6'
DELETE FROM ap USING `AVAILABILITY_POLICY` AS ap, `ACCESSIBLE_ITEM` AS f WHERE ap.`KEY_ACCESSIBLE_ITEM` = f.`ID_INTERNAL` AND f.`UUID` = '95b0bf15-1f4d-4430-815a-dfc1fb373ea6';
UPDATE `ACCESSIBLE_ITEM` AS own SET own.`KEY_AVAILABILITY_POLICY` = NULL, own.`ORDER_IN_MODULE` = 5 WHERE own.`UUID` = '95b0bf15-1f4d-4430-815a-dfc1fb373ea6';

--  ID: 155487 UUID: '2a487bf2-9986-40cf-923d-c571c211491f'
DELETE FROM ap USING `AVAILABILITY_POLICY` AS ap, `ACCESSIBLE_ITEM` AS f WHERE ap.`KEY_ACCESSIBLE_ITEM` = f.`ID_INTERNAL` AND f.`UUID` = '2a487bf2-9986-40cf-923d-c571c211491f';
UPDATE `ACCESSIBLE_ITEM` AS own SET own.`KEY_AVAILABILITY_POLICY` = NULL, own.`ORDER_IN_MODULE` = 2 WHERE own.`UUID` = '2a487bf2-9986-40cf-923d-c571c211491f';

--  ID: 155489 UUID: '5a35c10e-2108-412d-94f5-8ba12f051305'
DELETE FROM ap USING `AVAILABILITY_POLICY` AS ap, `ACCESSIBLE_ITEM` AS f WHERE ap.`KEY_ACCESSIBLE_ITEM` = f.`ID_INTERNAL` AND f.`UUID` = '5a35c10e-2108-412d-94f5-8ba12f051305';
UPDATE `ACCESSIBLE_ITEM` AS own SET own.`KEY_AVAILABILITY_POLICY` = NULL, own.`ORDER_IN_MODULE` = 3 WHERE own.`UUID` = '5a35c10e-2108-412d-94f5-8ba12f051305';

COMMIT;
