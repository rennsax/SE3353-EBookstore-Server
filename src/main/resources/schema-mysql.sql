USE bookstore;

-- drop original tables
DROP TABLE IF EXISTS `order_item`;
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `user_account`;
DROP TABLE IF EXISTS `book`;

-- create tables
create table `book` (
    id integer not null auto_increment,
    author varchar(255),
    date DATE,
    description longtext,
    isbn CHAR(13),
    pic_id CHAR(15),
    `price` DECIMAL(5, 2),
    title varchar(255) not null,
    uuid BINARY(16) DEFAULT(UUID_TO_BIN(UUID())) not null,
    stock integer DEFAULT("100") not null,
    primary key (id)
) engine=InnoDB;

create table `order` (
    id integer not null auto_increment,
    state CHAR(20) not null,
    time TIMESTAMP DEFAULT(NOW()) not null,
    `user_id` integer not null,
    primary key (id),
    check (`state` IN ('PENDING', 'COMPLETE', 'TRANSPORTING'))
) engine=InnoDB;

create table `order_item` (
    id integer not null auto_increment,
    quantity integer default 1 not null,
    `item_id` BINARY(16) DEFAULT(UUID_TO_BIN(UUID())),
    `order_id` integer not null,
    primary key (id)
) engine=InnoDB;

create table `user` (
    id integer not null auto_increment,
    avatar_id CHAR(15) default 'default_user' not null,
    name varchar(255),
    user_type varchar(255) not null,
    account_id integer not null,
    primary key (id),
    check (`user_type` in ('NORMAL', 'SUPER', 'FORBIDDEN'))
) engine=InnoDB;

create table `user_account` (
    id integer not null auto_increment,
    account varchar(255) not null,
    passwd varchar(255) not null,
    primary key (id)
) engine=InnoDB;

alter table `book`
    add constraint UK_e3h0bpkf6nvswp9it9w4918ra unique (uuid);

alter table `book`
    add constraint UK_ehpdfjpu1jm3hijhj4mm0hx9h unique (isbn);

alter table `book`
    add constraint UK_9fwk0l2m1jq18rhv2jgsoxc7p unique (pic_id);

alter table `user`
    add constraint UK_nrrhhb0bsexvi8ch6wnon9uog unique (account_id);

alter table `user_account`
    add constraint UK_1qxc5w4wrub9kn7hxr3o07adl unique (account);

alter table `order`
    add constraint order_user_fk
    foreign key (`user_id`)
    references `user` (id);

alter table `order_item`
    add constraint item_uuid_fk
    foreign key (`item_id`)
    references `book` (uuid);

alter table `order_item`
    add constraint order_item_fk
    foreign key (`order_id`)
    references `order` (id);

alter table `user`
    add constraint FK64r1kf5w3vu06sstrv3mumctx
    foreign key (account_id)
    references `user_account` (id);