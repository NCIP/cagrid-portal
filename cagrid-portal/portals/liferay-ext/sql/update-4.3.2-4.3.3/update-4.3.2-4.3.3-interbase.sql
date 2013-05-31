/*
============================================================================
  The Ohio State University Research Foundation, The University of Chicago -
  Argonne National Laboratory, Emory University, SemanticBits LLC, 
  and Ekagra Software Technologies Ltd.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
============================================================================
*/
alter table JournalArticle add indexable smallint;

commit;


create table ServiceComponent (
	serviceComponentId int64 not null primary key,
	buildNamespace varchar(75),
	buildNumber int64,
	buildDate int64,
	data_ blob
);

delete from UserTracker;

drop table UserTrackerPath;
create table UserTrackerPath (
	userTrackerPathId int64 not null primary key,
	userTrackerId int64,
	path_ varchar(4000),
	pathDate timestamp
);
