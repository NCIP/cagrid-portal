DROP TABLE IF EXISTS `lportal`.`layout`;
CREATE TABLE  `lportal`.`layout` (
  `plid` bigint(20) NOT NULL,
  `groupId` bigint(20) default NULL,
  `companyId` bigint(20) default NULL,
  `privateLayout` tinyint(4) default NULL,
  `layoutId` bigint(20) default NULL,
  `parentLayoutId` bigint(20) default NULL,
  `name` longtext,
  `title` longtext,
  `description` longtext,
  `type_` varchar(75) default NULL,
  `typeSettings` longtext,
  `hidden_` tinyint(4) default NULL,
  `friendlyURL` varchar(100) default NULL,
  `iconImage` tinyint(4) default NULL,
  `iconImageId` bigint(20) default NULL,
  `themeId` varchar(75) default NULL,
  `colorSchemeId` varchar(75) default NULL,
  `wapThemeId` varchar(75) default NULL,
  `wapColorSchemeId` varchar(75) default NULL,
  `css` longtext,
  `priority` int(11) default NULL,
  `dlFolderId` bigint(20) default NULL,
  PRIMARY KEY  (`plid`),
  KEY `IX_FAD05595` (`dlFolderId`),
  KEY `IX_705F5AA3` (`groupId`,`privateLayout`),
  KEY `IX_BC2C4231` (`groupId`,`privateLayout`,`friendlyURL`),
  KEY `IX_7162C27C` (`groupId`,`privateLayout`,`layoutId`),
  KEY `IX_6DE88B06` (`groupId`,`privateLayout`,`parentLayoutId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `lportal`.`layout`
--

/*!40000 ALTER TABLE `layout` DISABLE KEYS */;
LOCK TABLES `layout` WRITE;
INSERT INTO `lportal`.`layout` VALUES  (10101,10098,10093,0,1,0,'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <name>Home</name>\n</root>','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <title>Home</title>\n</root>','','portlet','column-1=cagridstatus_WAR_cagridportlets,85_INSTANCE_hcsQ,\nlayout-template-id=cagrid_portal_3_columns\nstate-min=\nstate-max-previous=2\nsitemap-include=1\nstate-max=\nsitemap-changefreq=daily\nmeta-robots=\nmeta-description=\njavascript-3=\njavascript-2=\nsitemap-priority=\ncolumn-3=cagridgreeting_WAR_cagridportlets,cagridnewssummary_WAR_cagridportlets,108,cagridinterestinglinks_WAR_cagridportlets\ncolumn-2=WelcomeToCaGridPortal_WAR_cagridportlets,cagridmap_WAR_cagridportlets,8,\nmeta-keywords=\njavascript-1=',0,'/home',0,0,'cagridminimal_WAR_cagridminimal','01','','','',0,0),
 (10126,10112,10093,1,1,0,'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <name>Home</name>\n</root>','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <title></title>\n</root>','','portlet','column-1=29,9,79,\nmode-help=\nlayout-template-id=1_column\nstate-min=\nstate-max-previous=29\nstate-max=29,\nmode-edit=\nmode-print=\nmode-edit-defaults=\nmode-preview=\nmode-config=\nmode-about=\nmode-edit-guest=',0,'',0,0,'','','','','',0,0),
 (10143,10098,10093,0,2,0,'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <name>Services</name>\n</root>','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <title>Services</title>\n</root>','','portlet','column-1=cagriddiscovery_WAR_cagridportlets,diagnostics_WAR_cagridportlets,\nlayout-template-id=2_columns_i\nstate-min=\nstate-max-previous=88\nsitemap-include=1\nstate-max=\nsitemap-changefreq=daily\nmeta-robots=\nmeta-description=\njavascript-3=\njavascript-2=\nsitemap-priority=\njavascript-1=\nmeta-keywords=\ncolumn-2=cagridquery_WAR_cagridportlets',0,'/discovery',0,0,'cagridminimal_WAR_cagridminimal','01','','','',1,0),
 (10146,10098,10093,0,3,0,'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n\n<root>\n  <name>Tools</name>\n</root>','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <title>Tools</title>\n</root>','','portlet','column-1=cabigtools_WAR_cagridportlets,\nlayout-template-id=1_column\nstate-min=\nstate-max-previous=88\nsitemap-include=1\nstate-max=\nsitemap-changefreq=daily\nmeta-robots=\nmeta-description=\njavascript-3=\njavascript-2=\nsitemap-priority=\njavascript-1=\nmeta-keywords=',0,'/tools',0,0,'cagridminimal_WAR_cagridminimal','01','','','',1,0),
 (10148,10098,10093,0,5,0,'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n\n<root>\n  <name>Community</name>\n</root>','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <title>Community</title>\n</root>','','portlet','layout-template-id=2_columns_i\nstate-min=\nstate-max-previous=88\nsitemap-include=1\nstate-max=\nsitemap-changefreq=daily\nmeta-robots=\nmeta-description=\njavascript-3=\njavascript-2=\nsitemap-priority=\njavascript-1=\nmeta-keywords=',0,'/community',0,0,'cagridminimal_WAR_cagridminimal','01','','','',1,0),
 (10150,10098,10093,0,13,0,'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n \n\n<root>\n  <name>News</name>\n</root>','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <title>News</title>\n</root>','','portlet','column-1=cagridnews_WAR_cagridportlets,\nlayout-template-id=1_column\nstate-min=\nstate-max-previous=88\nsitemap-include=1\nstate-max=\nsitemap-changefreq=daily\nmeta-robots=\nmeta-description=\njavascript-3=\njavascript-2=\nsitemap-priority=\njavascript-1=\nmeta-keywords=',0,'/news',0,0,'cagridminimal_WAR_cagridminimal','01','','','',2,0),
 (10152,10098,10093,0,4,0,'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <name>Register</name>\n</root>','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <title>Register</title>\n</root>','','portlet','column-1=cagridregistration_WAR_cagridportlets,\nlayout-template-id=2_columns_iii\nstate-min=\nstate-max-previous=88\nsitemap-include=1\nstate-max=\nsitemap-changefreq=daily\nmeta-robots=\nmeta-description=\njavascript-3=\njavascript-2=\nsitemap-priority=\njavascript-1=\nmeta-keywords=',0,'/register',0,0,'cagridminimal_WAR_cagridminimal','01','','','',3,0),
 (10154,10098,10093,0,6,1,'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <name>Login</name>\n</root>','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <title></title>\n</root>','','portlet','column-1=cagriddirectauthn_WAR_cagridportlets,\nlayout-template-id=2_columns_ii\nstate-min=\nstate-max-previous=\nsitemap-include=1\nstate-max=\nsitemap-changefreq=daily\nmeta-robots=\nmeta-description=\njavascript-3=\nsitemap-priority=\njavascript-2=\njavascript-1=\nmeta-keywords=',0,'/login',0,0,'cagridminimal_WAR_cagridminimal','01','','','',0,0),
 (10156,10098,10093,0,7,1,'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <name>Help</name>\n</root>','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <title>Help</title>\n</root>','','embedded','url=/cagridportlets/content/portalhelp\nsitemap-include=1\nsitemap-changefreq=daily\nmeta-robots=\nmeta-description=\ndescription=Help page.\njavascript-3=\nsitemap-priority=\njavascript-2=\njavascript-1=\nmeta-keywords=',0,'/help',0,0,'cagridminimal_WAR_cagridminimal','01','','','',1,0),
 (10157,10098,10093,0,8,1,'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <name>Contact</name>\n</root>','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <title>Contact</title>\n</root>','','embedded','url=/cagridportlets/content/contact\nsitemap-include=1\nsitemap-changefreq=daily\nmeta-robots=\nmeta-description=\ndescription=Contact page.\njavascript-3=\nsitemap-priority=\njavascript-2=\njavascript-1=\nmeta-keywords=',0,'/contact',0,0,'cagridminimal_WAR_cagridminimal','01','','','',2,0),
 (10158,10098,10093,0,9,1,'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <name>Privacy</name>\n</root>','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <title>Privacy</title>\n</root>','','embedded','url=/cagridportlets/content/privacy\nsitemap-include=1\nsitemap-changefreq=daily\nmeta-robots=\nmeta-description=\ndescription=Privacy disclosure.\njavascript-3=\nsitemap-priority=\njavascript-2=\njavascript-1=\nmeta-keywords=',0,'/privacy',0,0,'cagridminimal_WAR_cagridminimal','01','','','',3,0),
 (10159,10098,10093,0,10,1,'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <name>Accessibility</name>\n</root>','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <title>Accessibility</title>\n</root>','','embedded','url=/cagridportlets/content/accessibility\nsitemap-include=1\nsitemap-changefreq=daily\nmeta-robots=\nmeta-description=\ndescription=Accessibility page.\njavascript-3=\nsitemap-priority=\njavascript-2=\njavascript-1=\nmeta-keywords=',0,'/accessibility',0,0,'cagridminimal_WAR_cagridminimal','01','','','',4,0),
 (10160,10098,10093,0,11,1,'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <name>Application Support</name>\n</root>','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <title>Application Support</title>\n</root>','','embedded','url=/cagridportlets/content/support\nsitemap-include=1\nsitemap-changefreq=daily\nmeta-robots=\nmeta-description=\ndescription=Support page.\njavascript-3=\nsitemap-priority=\njavascript-2=\njavascript-1=\nmeta-keywords=',0,'/support',0,0,'cagridminimal_WAR_cagridminimal','01','','','',5,0),
 (10161,10098,10093,0,12,1,'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <name>Disclaimer</name>\n</root>','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <title>Disclaimer</title>\n</root>','','embedded','url=/cagridportlets/content/disclaimer\nsitemap-include=1\nsitemap-changefreq=daily\nmeta-robots=\nmeta-description=\ndescription=Disclaimer page.\njavascript-3=\nsitemap-priority=\njavascript-2=\njavascript-1=\nmeta-keywords=',0,'/disclaimer',0,0,'cagridminimal_WAR_cagridminimal','01','','','',6,0);
UNLOCK TABLES;
/*!40000 ALTER TABLE `layout` ENABLE KEYS */;


--
-- Definition of table `lportal`.`layoutset`
--

DROP TABLE IF EXISTS `lportal`.`layoutset`;
CREATE TABLE  `lportal`.`layoutset` (
  `layoutSetId` bigint(20) NOT NULL,
  `groupId` bigint(20) default NULL,
  `companyId` bigint(20) default NULL,
  `privateLayout` tinyint(4) default NULL,
  `logo` tinyint(4) default NULL,
  `logoId` bigint(20) default NULL,
  `themeId` varchar(75) default NULL,
  `colorSchemeId` varchar(75) default NULL,
  `wapThemeId` varchar(75) default NULL,
  `wapColorSchemeId` varchar(75) default NULL,
  `css` varchar(75) default NULL,
  `pageCount` int(11) default NULL,
  `virtualHost` varchar(75) default NULL,
  PRIMARY KEY  (`layoutSetId`),
  KEY `IX_A40B8BEC` (`groupId`),
  KEY `IX_48550691` (`groupId`,`privateLayout`),
  KEY `IX_5ABC2905` (`virtualHost`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `lportal`.`layoutset`
--

/*!40000 ALTER TABLE `layoutset` DISABLE KEYS */;
LOCK TABLES `layoutset` WRITE;
INSERT INTO `lportal`.`layoutset` VALUES  (10099,10098,10093,1,0,0,'cagridminimal','01','mobile','01','',0,''),
 (10100,10098,10093,0,0,0,'cagridminimal','01','mobile','01','',13,''),
 (10113,10112,10093,1,0,0,'classic','01','mobile','01','',1,''),
 (10114,10112,10093,0,0,0,'cagridminimal','01','mobile','01','',0,''),
 (10117,10116,10093,1,0,0,'cagridminimal','01','mobile','01','',0,''),
 (10118,10116,10093,0,0,0,'cagridminimal','01','mobile','01','',0,''),
 (10121,10120,10093,1,0,0,'cagridminimal','01','mobile','01','',0,''),
 (10122,10120,10093,0,0,0,'cagridminimal','01','mobile','01','',0,'');
UNLOCK TABLES;
/*!40000 ALTER TABLE `layoutset` ENABLE KEYS */;


--