CREATE TABLE EXECUTION_PATH (
	ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
	EXECUTION_PATH varchar(255),
	KEY_FUNCTIONALITY int(11),
	KEY_ROOT_DOMAIN_OBJECT int(11) NOT NULL default '1',	
	KEY KEY_FUNCTIONALITY (`KEY_FUNCTIONALITY`),
	KEY KEY_ROOT_DOMAIN_OBJECT (`KEY_ROOT_DOMAIN_OBJECT`),
	KEY ID_INTERNAL (`ID_INTERNAL`)
);
