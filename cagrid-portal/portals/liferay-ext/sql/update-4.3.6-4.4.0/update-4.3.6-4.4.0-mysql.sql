/*
============================================================================
  The Ohio State University Research Foundation, The University of Chicago -
  Argonne National Laboratory, Emory University, SemanticBits LLC, 
  and Ekagra Software Technologies Ltd.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
============================================================================
*/
create table ActivityTracker (
	activityTrackerId bigint not null primary key,
	groupId bigint,
	companyId bigint,
	userId bigint,
	userName varchar(75) null,
	createDate datetime null,
	classNameId bigint,
	classPK bigint,
	activity varchar(75) null,
	extraData longtext null,
	receiverUserId bigint,
	receiverUserName varchar(75) null
) engine InnoDB;

alter table BlogsEntry add uuid_ varchar(75) null;

alter table BookmarksEntry add uuid_ varchar(75) null;

alter table BookmarksFolder add uuid_ varchar(75) null;

alter table CalEvent add uuid_ varchar(75) null;

alter table DLFileEntry add uuid_ varchar(75) null;

alter table DLFileShortcut add uuid_ varchar(75) null;

alter table DLFolder add uuid_ varchar(75) null;

update Group_ set type_ = '0' where type_ is null;
update Group_ set type_ = '0' where type_ = '';
update Group_ set type_ = '1' where type_ = 'COMMUNITY_OPEN';
update Group_ set type_ = '2' where type_ = 'COMMUNITY_RESTRICTED';
update Group_ set type_ = '3' where type_ = 'COMMUNITY_CLOSED';
update Group_ set type_ = '3' where type_ = '0';

alter table IGFolder add uuid_ varchar(75) null;

alter table IGImage add uuid_ varchar(75) null;

alter table JournalArticle add uuid_ varchar(75) null;
alter table JournalArticle add smallImage tinyint;
alter table JournalArticle add smallImageId bigint;
alter table JournalArticle add smallImageURL varchar(75) null;

alter table JournalArticleImage add uuid_ varchar(75) null;

create table JournalFeed (
	uuid_ varchar(75) null,
	id_ bigint not null primary key,
	groupId bigint,
	companyId bigint,
	userId bigint,
	userName varchar(75) null,
	createDate datetime null,
	modifiedDate datetime null,
	feedId varchar(75) null,
	name varchar(75) null,
	description longtext null,
	type_ varchar(75) null,
	structureId varchar(75) null,
	templateId varchar(75) null,
	rendererTemplateId varchar(75) null,
	delta integer,
	orderByCol varchar(75) null,
	orderByType varchar(75) null,
	targetLayoutFriendlyUrl varchar(75) null,
	targetPortletId varchar(75) null,
	contentField varchar(75) null,
	feedType varchar(75) null,
	feedVersion double
) engine InnoDB;

alter table JournalStructure add uuid_ varchar(75) null;

alter table JournalTemplate add uuid_ varchar(75) null;
alter table JournalTemplate add cacheable tinyint;

commit;

update JournalTemplate set cacheable = 1;

alter table MBCategory add uuid_ varchar(75) null;

alter table MBMessage add uuid_ varchar(75) null;

create table MembershipRequest (
	membershipRequestId bigint not null primary key,
	companyId bigint,
	userId bigint,
	createDate datetime null,
	groupId bigint,
	comments longtext null,
	replyComments longtext null,
	replyDate datetime null,
	replierUserId bigint,
	statusId integer
) engine InnoDB;

alter table PollsChoice add uuid_ varchar(75) null;

alter table PollsQuestion add uuid_ varchar(75) null;

alter table TagsAsset add groupId bigint;
alter table TagsAsset add priority double;
alter table TagsAsset add viewCount integer;

commit;

update TagsAsset set priority = 0, viewCount = 0;

alter table User_ add uuid_ varchar(75) null;

create table WebDAVProps (
	webDavPropsId bigint not null primary key,
	companyId bigint,
	createDate datetime null,
	modifiedDate datetime null,
	classNameId bigint,
	classPK bigint,
	props longtext null
) engine InnoDB;

alter table WikiNode add uuid_ varchar(75) null;

alter table WikiPage add uuid_ varchar(75) null;
