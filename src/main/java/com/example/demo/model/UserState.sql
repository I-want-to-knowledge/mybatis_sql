-- auto Generated on 2019-02-15
-- DROP TABLE IF EXISTS user_state;
CREATE TABLE user_state(
	id BIGINT (15) NOT NULL AUTO_INCREMENT COMMENT 'id',
	user_id BIGINT (15) NOT NULL DEFAULT -1 COMMENT 'userId',
	type INT (11) NOT NULL DEFAULT -1 COMMENT 'type',
	last_time DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT 'lastTime',
	PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'user_state';
