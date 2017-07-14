create database BikeSystem;

use BikeSystem;
alter database BikeSystem  character set utf8;
create table adminInfo(
	adminID char(11),
	adminPassword char(32),
	primary key (adminID)
);

INSERT INTO adminInfo (adminID, adminPassword) VALUES ("admin", "admin");

create table LocationInfo(
	LocationID char(11),
	bikeNum integer default 0,
	stubNum integer default 0,
	cord_x float default 0,
	cord_y float default 0,
	primary key (LocationID)
);
create table UserInfo(
	UserID char(11),
	UserPassword char(32),
	Balance integer not null default 0,
	RendTime bigint not null default 0,
	RendBikeID char(11) default null,
	Gender char(1) default "M",
	NickName char(32) default null,
	Age integer default 0,
	primary key (UserID)
);
create table StubInfo(
	StubID char(11),
	LocationID char(11),
	BikeID char(11) default null,
	primary key (StubID),
	foreign key (LocationID) references LocationInfo(LocationID)
);
create table BikeInfo(
	BikeID char(11),
	StubID char(11) default null,
	RendUserID char(11) default null,
	RendTime bigint not null default 0,
	primary key (BikeID),
	foreign key (StubID) references StubInfo(StubID),
	foreign key (RendUserID) references UserInfo(UserID)
);
create table reportInfo(
	reportTime TIMESTAMP NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
	reportID int not null auto_increment,
	reporterID char(11),
	reportContent varchar(1000),
	primary key (reportID),
	foreign key (reporterID) references UserInfo(UserID)
);
alter table UserInfo add constraint user_bike foreign key (RendBikeID) references BikeInfo(BikeID);
alter table StubInfo add constraint stub_bike foreign key (BikeID) references BikeInfo(BikeID);
