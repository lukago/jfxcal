use pkcalendar;

create table date_events(
	id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
	date_time DATETIME NOT NULL,
	notify_time DATETIME,
	description VARCHAR(500),
	place VARCHAR(100)
);

select * from date_events