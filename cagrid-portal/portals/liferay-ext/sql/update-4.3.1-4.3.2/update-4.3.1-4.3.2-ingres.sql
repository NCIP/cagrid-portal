/*
============================================================================
  The Ohio State University Research Foundation, The University of Chicago -
  Argonne National Laboratory, Emory University, SemanticBits LLC, 
  and Ekagra Software Technologies Ltd.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
============================================================================
*/
alter table Group_ add typeSettings varchar(1000) null;

alter table SCProductEntry add tags varchar(300) null;
alter table SCProductEntry add author varchar(75) null;

create table SCProductScreenshot (
	productScreenshotId bigint not null primary key,
	companyId bigint,
	groupId bigint,
	productEntryId bigint,
	thumbnailId bigint,
	fullImageId bigint,
	priority integer
);

alter table TagsAsset add description varchar(1000) null;
alter table TagsAsset add summary varchar(1000) null;
