CREATE TABLE RECEIPT_ENTRY (
  ID_INTERNAL int(11) unsigned NOT NULL auto_increment,
  KEY_RECEIPT int(11) NOT NULL,
  KEY_ENTRY int(11) NOT NULL,
  PRIMARY KEY  (`ID_INTERNAL`),
  KEY KEY_RECEIPT (KEY_RECEIPT),
  KEY KEY_ENTRY (KEY_ENTRY),
  UNIQUE (KEY_RECEIPT,KEY_ENTRY)
) ENGINE=InnoDB;


INSERT INTO RECEIPT_ENTRY (KEY_RECEIPT,KEY_ENTRY) SELECT KEY_RECEIPT,ID_INTERNAL FROM ACCOUNTING_ENTRY WHERE KEY_RECEIPT IS NOT NULL;


