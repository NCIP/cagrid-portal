/*
============================================================================
  The Ohio State University Research Foundation, The University of Chicago -
  Argonne National Laboratory, Emory University, SemanticBits LLC, 
  and Ekagra Software Technologies Ltd.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
============================================================================
*/
alter table JournalArticle add indexable boolean;



update JournalArticle set indexable = 'T';

create table ServiceComponent (
	serviceComponentId int8 not null primary key,
	buildNamespace varchar(75),
	buildNumber int8,
	buildDate int8,
	data_ text
)
extent size 16 next size 16
lock mode row;

delete from UserTracker;

drop table UserTrackerPath;
create table UserTrackerPath (
	userTrackerPathId int8 not null primary key,
	userTrackerId int8,
	path_ lvarchar,
	pathDate datetime YEAR TO FRACTION
)
extent size 16 next size 16
lock mode row;
