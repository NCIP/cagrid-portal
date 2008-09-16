 delete from sem_meta_map
   where sem_meta_id not in (select id from sem_meta);