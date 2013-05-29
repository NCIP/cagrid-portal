/*
============================================================================
  The Ohio State University Research Foundation, The University of Chicago -
  Argonne National Laboratory, Emory University, SemanticBits LLC, 
  and Ekagra Software Technologies Ltd.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
============================================================================
*/
alter table JournalArticle add indexable BOOLEAN;

COMMIT_TRANSACTION;

update JournalArticle set indexable = TRUE;

create table ServiceComponent (
	serviceComponentId LONG not null primary key,
	buildNamespace VARCHAR(75) null,
	buildNumber LONG,
	buildDate LONG,
	data_ TEXT null
);

delete from UserTracker;

drop table UserTrackerPath;
create table UserTrackerPath (
	userTrackerPathId LONG not null primary key,
	userTrackerId LONG,
	path_ STRING null,
	pathDate DATE null
);