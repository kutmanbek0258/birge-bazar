alter table users.user_profiles alter column user_id type varchar(32) using user_id::varchar(32);
alter table users.seller_profiles alter column user_id type varchar(32) using user_id::varchar(32);
alter table users.addresses alter column user_id type varchar(32) using user_id::varchar(32);