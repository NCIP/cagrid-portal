/*
============================================================================
  The Ohio State University Research Foundation, The University of Chicago -
  Argonne National Laboratory, Emory University, SemanticBits LLC, 
  and Ekagra Software Technologies Ltd.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
============================================================================
*/
alter table BookmarksEntry add priority integer;

alter table Layout add description varchar(1000) null;
alter table Layout add dlFolderId bigint;

alter table Organization_ add location tinyint;

commit;\g

update Organization_ set location = 0;
