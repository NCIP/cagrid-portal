INSERT INTO portal.ORM_UNIQUE_KEY
(NEXT_VALUE) 
VALUES (1);

select DISTINCT(*) from portal.RESEARCH_CENTER rc group by longitude,latitude;