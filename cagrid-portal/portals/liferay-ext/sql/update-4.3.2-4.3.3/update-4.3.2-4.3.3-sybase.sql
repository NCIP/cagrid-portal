/*
============================================================================
  The Ohio State University Research Foundation, The University of Chicago -
  Argonne National Laboratory, Emory University, SemanticBits LLC, 
  and Ekagra Software Technologies Ltd.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
============================================================================
*/
alter table JournalArticle add indexable int;

go

update JournalArticle set indexable = 1;

create table ServiceComponent (
	serviceComponentId decimal(20,0) not null primary key,
	buildNamespace varchar(75) null,
	buildNumber decimal(20,0),
	buildDate decimal(20,0),
	data_ text null
)
go

delete from UserTracker;

drop table UserTrackerPath;
create table UserTrackerPath (
	userTrackerPathId decimal(20,0) not null primary key,
	userTrackerId decimal(20,0),
	path_ varchar(1000) null,
	pathDate datetime null
)
go
