/*
============================================================================
  The Ohio State University Research Foundation, The University of Chicago -
  Argonne National Laboratory, Emory University, SemanticBits LLC, 
  and Ekagra Software Technologies Ltd.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
============================================================================
*/
alter table BookmarksEntry add priority number(30,0);

alter table Layout add description varchar2(4000) null;
alter table Layout add dlFolderId number(30,0);

alter table Organization_ add location number(1, 0);

commit;

update Organization_ set location = 0;
