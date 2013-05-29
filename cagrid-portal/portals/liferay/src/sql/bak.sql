/*
============================================================================
  The Ohio State University Research Foundation, The University of Chicago -
  Argonne National Laboratory, Emory University, SemanticBits LLC, 
  and Ekagra Software Technologies Ltd.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
============================================================================
*/
-- Guest Home Tab
insert into Layout 
(
	plid, groupId, companyId, privateLayout, layoutId, parentLayoutId,
    name, 
    title, description,
    type_,
    typeSettings,
    hidden_, friendlyURL, iconImage, iconImageId,
    themeId, colorSchemeId, wapThemeId, wapColorSchemeId, css,
    priority, dlFolderId
)
values
(
	10091,14,1,0,1,0,
	'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <name>Home</name>\n</root>',
	'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <title></title>\n</root>','',
	'portlet',
	'state-max-previous=\ncolumn-3=cagridauthn_WAR_cagridportlets,cagridstatus_WAR_cagridportlets,\ncolumn-2=WelcomeToCaGridPortal_WAR_cagridportlets,cagridmap_WAR_cagridportlets\ncolumn-1=cagridnewssummary_WAR_cagridportlets,8,\nstate-max=\nstate-min=\nlayout-template-id=3_columns',
	0, '/home', 0,0,
	'','','','','',
	0,0
);

-- Guest Tools Tab
insert into Layout 
(
	plid, groupId, companyId, privateLayout, layoutId, parentLayoutId,
    name, 
    title, description,
    type_,
    typeSettings,
    hidden_, friendlyURL, iconImage, iconImageId,
    themeId, colorSchemeId, wapThemeId, wapColorSchemeId, css,
    priority, dlFolderId
)
values
(
	10136,14,1,0,2,0,
	'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <name>Tools</name>\n</root>',
	'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <title></title>\n</root>','',
	'portlet',
	'state-max-previous=\ncolumn-2=cagridquery_WAR_cagridportlets\ncolumn-1=cagriddiscovery_WAR_cagridportlets,\nstate-max=\nstate-min=\nlayout-template-id=2_columns_i',
	0,'/tools',0,0,
	'','','','','',
	1,0
);

-- Guest News and Events Tab
insert into Layout 
(
	plid, groupId, companyId, privateLayout, layoutId, parentLayoutId,
    name, 
    title, description,
    type_,
    typeSettings,
    hidden_, friendlyURL, iconImage, iconImageId,
    themeId, colorSchemeId, wapThemeId, wapColorSchemeId, css,
    priority, dlFolderId
)
values
(
	10142,14,1,0,3,0,
	'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <name>News and Events</name>\n</root>',
	'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <title></title>\n</root>','',
	'portlet',
	'state-max-previous=\ncolumn-1=cagridnews_WAR_cagridportlets,\nstate-max=\nstate-min=\nlayout-template-id=1_column',
	0,'/newsAndEvents',0,0,
	'','','','','',
	2,0
);

-- Guest Register Tab
insert into Layout 
(
	plid, groupId, companyId, privateLayout, layoutId, parentLayoutId,
    name, 
    title, description,
    type_,
    typeSettings,
    hidden_, friendlyURL, iconImage, iconImageId,
    themeId, colorSchemeId, wapThemeId, wapColorSchemeId, css,
    priority, dlFolderId
)
values
(
	10147,14,1,0,4,0,
	'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <name>Register</name>\n</root>',
	'<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n<root>\n  <title></title>\n</root>','',
	'portlet','state-max-previous=\ncolumn-1=cagridregistration_WAR_cagridportlets,\nstate-max=\nstate-min=\nlayout-template-id=2_columns_ii',
	0,'/register',0,0,
	'','','','','',
	3,0
);
