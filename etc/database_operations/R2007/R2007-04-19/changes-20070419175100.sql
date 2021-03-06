--  SQL file representing changes to the functionalities model
--  Generated at Thu Apr 19 17:51:52 WEST 2007
--  DO NOT EDIT THIS FILE, run the generating script instead

--  Preamble
SET AUTOCOMMIT = 0;

START TRANSACTION;

-- 
--  Inserting new functionalities
-- 

--  ID: 117667 UUID: 'aade3f06-9faf-49cc-a28b-d2cf136f8469'
INSERT INTO `ACCESSIBLE_ITEM` (`UUID`, `OJB_CONCRETE_CLASS`, `KEY_ROOT_DOMAIN_OBJECT`, `KEY_PARENT`, `KEY_MODULE`, `KEY_AVAILABILITY_POLICY`, `NAME`, `TITLE`, `DESCRIPTION`, `PATH`, `PREFIX`, `RELATIVE`, `ENABLED`, `PARAMETERS`, `ORDER_IN_MODULE`, `VISIBLE`, `MAXIMIZED`, `PRINCIPAL`, `KEY_SUPERIOR_SECTION`, `SECTION_ORDER`, `KEY_SITE`, `LAST_MODIFIED_DATE_YEAR_MONTH_DAY`, `KEY_SECTION`, `ITEM_ORDER`, `INFORMATION`, `KEY_FUNCTIONALITY`) SELECT 'aade3f06-9faf-49cc-a28b-d2cf136f8469', 'net.sourceforge.fenixedu.domain.functionalities.Module', 1, `ID_INTERNAL`, `ID_INTERNAL`, NULL, 'en13:Research Sitept20:Site de Investigação', NULL, NULL, '', '/researchSite', 1, 1, NULL, 4, 1, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL FROM `ACCESSIBLE_ITEM` WHERE `UUID` = '0c52935e-37bc-413a-9d13-b40e9b13f242';

--  ID: 117668 UUID: '3ea20dd8-cbe0-4100-b341-7714dd3263ec'
INSERT INTO `ACCESSIBLE_ITEM` (`UUID`, `OJB_CONCRETE_CLASS`, `KEY_ROOT_DOMAIN_OBJECT`, `KEY_PARENT`, `KEY_MODULE`, `KEY_AVAILABILITY_POLICY`, `NAME`, `TITLE`, `DESCRIPTION`, `PATH`, `PREFIX`, `RELATIVE`, `ENABLED`, `PARAMETERS`, `ORDER_IN_MODULE`, `VISIBLE`, `MAXIMIZED`, `PRINCIPAL`, `KEY_SUPERIOR_SECTION`, `SECTION_ORDER`, `KEY_SITE`, `LAST_MODIFIED_DATE_YEAR_MONTH_DAY`, `KEY_SECTION`, `ITEM_ORDER`, `INFORMATION`, `KEY_FUNCTIONALITY`) SELECT '3ea20dd8-cbe0-4100-b341-7714dd3263ec', 'net.sourceforge.fenixedu.domain.functionalities.ConcreteFunctionality', 1, NULL, `ID_INTERNAL`, NULL, 'en12:Presentationpt12:Apresentação', NULL, NULL, '/viewResearchUnitSite.do?method=frontPage', NULL, 1, 1, 'siteID', 0, 1, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL FROM `ACCESSIBLE_ITEM` WHERE `UUID` = 'aade3f06-9faf-49cc-a28b-d2cf136f8469';

--  ID: 117669 UUID: '8d123cd3-b4ce-4152-9860-ab3b1b8ab900'
INSERT INTO `ACCESSIBLE_ITEM` (`UUID`, `OJB_CONCRETE_CLASS`, `KEY_ROOT_DOMAIN_OBJECT`, `KEY_PARENT`, `KEY_MODULE`, `KEY_AVAILABILITY_POLICY`, `NAME`, `TITLE`, `DESCRIPTION`, `PATH`, `PREFIX`, `RELATIVE`, `ENABLED`, `PARAMETERS`, `ORDER_IN_MODULE`, `VISIBLE`, `MAXIMIZED`, `PRINCIPAL`, `KEY_SUPERIOR_SECTION`, `SECTION_ORDER`, `KEY_SITE`, `LAST_MODIFIED_DATE_YEAR_MONTH_DAY`, `KEY_SECTION`, `ITEM_ORDER`, `INFORMATION`, `KEY_FUNCTIONALITY`) SELECT '8d123cd3-b4ce-4152-9860-ab3b1b8ab900', 'net.sourceforge.fenixedu.domain.functionalities.ConcreteFunctionality', 1, NULL, `ID_INTERNAL`, NULL, 'en24:Research Site Managementpt31:Gestão de Sites de Investigação', 'en29:Research Unit Site Managementpt43:Gestão de Sites de Unidades de Investigação', NULL, '/researchUnitSiteManagement.do?method=prepare', NULL, 1, 1, NULL, 3, 1, 0, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL FROM `ACCESSIBLE_ITEM` WHERE `UUID` = 'f0ce9502-abeb-4f54-96e8-0aafa607d119';

COMMIT;
