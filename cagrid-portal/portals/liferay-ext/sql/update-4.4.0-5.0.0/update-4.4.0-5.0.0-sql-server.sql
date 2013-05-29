/*
============================================================================
  The Ohio State University Research Foundation, The University of Chicago -
  Argonne National Laboratory, Emory University, SemanticBits LLC, 
  and Ekagra Software Technologies Ltd.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
============================================================================
*/
drop table ActivityTracker;

create table AnnouncementsDelivery (
	deliveryId bigint not null primary key,
	companyId bigint,
	userId bigint,
	type_ varchar(75) null,
	email bit,
	sms bit,
	website bit
);

create table AnnouncementsEntry (
	uuid_ varchar(75) null,
	entryId bigint not null primary key,
	companyId bigint,
	userId bigint,
	userName varchar(75) null,
	createDate datetime null,
	modifiedDate datetime null,
	classNameId bigint,
	classPK bigint,
	title varchar(75) null,
	content varchar(2000) null,
	url varchar(2000) null,
	type_ varchar(75) null,
	displayDate datetime null,
	expirationDate datetime null,
	priority int,
	alert bit
);

create table AnnouncementsFlag (
	flagId bigint not null primary key,
	userId bigint,
	createDate datetime null,
	entryId bigint,
	value int
);

create table ExpandoColumn (
	columnId bigint not null primary key,
	tableId bigint,
	name varchar(75) null,
	type_ int
);

create table ExpandoRow (
	rowId_ bigint not null primary key,
	tableId bigint,
	classPK bigint
);

create table ExpandoTable (
	tableId bigint not null primary key,
	classNameId bigint,
	name varchar(75) null
);

create table ExpandoValue (
	valueId bigint not null primary key,
	tableId bigint,
	columnId bigint,
	rowId_ bigint,
	classNameId bigint,
	classPK bigint,
	data_ varchar(75) null
);

alter table IGImage add name varchar(75) null;
alter table IGImage add custom1ImageId bigint null;
alter table IGImage add custom2ImageId bigint null;

update Group_ set type_ = 3 where type_ = 0;

update Image set type_ = 'jpg' where type_ = 'jpeg';

create table PortletItem (
	portletItemId bigint not null primary key,
	groupId bigint,
	companyId bigint,
	userId bigint,
	userName varchar(75) null,
	createDate datetime null,
	modifiedDate datetime null,
	name varchar(75) null,
	portletId varchar(75) null,
	classNameId bigint
);

create table SocialActivity (
	activityId bigint not null primary key,
	groupId bigint,
	companyId bigint,
	userId bigint,
	createDate datetime null,
	classNameId bigint,
	classPK bigint,
	type_ varchar(75) null,
	extraData text null,
	receiverUserId bigint
);

create table SocialRelation (
	uuid_ varchar(75) null,
	relationId bigint not null primary key,
	companyId bigint,
	createDate datetime null,
	userId1 bigint,
	userId2 bigint,
	type_ int
);

create table TasksProposal (
	proposalId bigint not null primary key,
	groupId bigint,
	companyId bigint,
	userId bigint,
	userName varchar(75) null,
	createDate datetime null,
	modifiedDate datetime null,
	classNameId bigint,
	classPK varchar(75) null,
	name varchar(75) null,
	description varchar(2000) null,
	publishDate datetime null,
	dueDate datetime null
);

create table TasksReview (
	reviewId bigint not null primary key,
	groupId bigint,
	companyId bigint,
	userId bigint,
	userName varchar(75) null,
	createDate datetime null,
	modifiedDate datetime null,
	proposalId bigint,
	assignedByUserId bigint,
	assignedByUserName varchar(75) null,
	stage int,
	completed bit,
	rejected bit
);

alter table WikiPage add parentTitle varchar(75) null;
alter table WikiPage add redirectTitle varchar(75) null;
