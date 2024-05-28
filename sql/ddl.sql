create table user (
    user_number bigint auto_increment,
    user_id varchar(255) unique not null,
    user_password varchar(255) not null,
    user_birth_date date not null,
    user_phone_number varchar(255) null,
    user_gender varchar(2) not null,
    user_name varchar(255) not null,
    user_email varchar(255) null,
    user_sns varchar(255) null,
    primary key(user_number)
);

alter table user change user user_birth_Date user_birth_date date not null;

create table chat (
    user_number bigint,
    native_number bigint,
    chat_content varchar(255) null,
    chat_date date null,
    primary key(user_number, native_number),
    foreign key(user_number) references user(user_number),
    foreign key(native_number) references native(native_number)
);

create table native (
    native_number bigint auto_increment,
    native_id varchar(255) not null,
    native_password varchar(255) not null,
    native_phone_number varchar(255) not null,
    native_email varchar(255) not null,
    native_location varchar(255) not null,
    native_profile varchar(255) null,
    native_gender varchar(2) not null,
    primary key(native_number)
);

alter table native modify column native_gender varchar(2) not null;

create table reservation (
    reservation_number bigint auto_increment,
    reservation_date date null,
    reservation_people bigint null,
    user_number bigint,
    room_number bigint null,
    primary key(reservation_number),
    foreign key(user_number) references user(user_number),
    foreign key(room_number) references room(room_number)
);

alter table reservation modify column user_number bigint not null;

create table native_page (
    native_page_number bigint auto_increment,
    native_page_title varchar(255) not null,
    native_page_writer varchar(255) not null,
    native_page_content varchar(255) not null,
    native_page_like bigint null,
    native_page_date date not null,
    native_number bigint not null,
    hotel_number bigint not null,
    primary key(native_page_number),
    foreign key(native_number) references native(native_number),
    foreign key(hotel_number) references hotel(hotel_number)
);

alter table native_page modify column native_page_like bigint not null;
alter table native_page modify column native_number bigint not null;
alter table native_page modify column hotel_number bigint not null;

create table room (
    room_number bigint auto_increment,
    room_people varchar(255) null,
    room_cost bigint null,
    room_reservation_info varchar(255) null,
    hotel_number bigint not null,
    primary key(room_number),
    foreign key(hotel_number) references hotel(hotel_number)
);

alter table room modify column hotel_number bigint not null;


create table hotel (
    hotel_number bigint auto_increment,
    hotel_name varchar(255) null,
    hotel_phone varchar(255) null,
    hotel_email varchar(255) null,
    hotel_site varchar(255) null,
    hotel_latitude double null,
    hotel_longitude double null,
    hotel_type varchar(255) null,
    hotel_address varchar(255) null,
    city_number bigint not null,
    primary key(hotel_number),
    foreign key(city_number) references city(city_number)
);

alter table hotel modify column city_number bigint not null;

create table city (
    city_number bigint auto_increment,
    city_name varchar(255) null,
    city_latitude double null,
    city_longitude double null,
    city_information double null,
    primary key(city_number)
);

create table city_picture (
    city_picture_number bigint auto_increment,
    city_number bigint,
    city_picture_url varchar(255) null,
    primary key(city_picture_number, city_number),
    foreign key(city_number) references city(city_number)
);

create table room_picture (
    room_picture_number bigint auto_increment,
    room_number bigint,
    room_picture_url varchar(255) null,
    primary key(room_picture_number, room_number),
    foreign key(room_number) references room(room_number)
);

create table hotel_picture (
    hotel_picture_number bigint auto_increment,
    hotel_number bigint,
    hotel_picture_url varchar(255) null,
    primary key(hotel_picture_number, hotel_number),
    foreign key(hotel_number) references hotel(hotel_number)
);

create table nation (
    nation_number bigint,
    nation_name varchar(255) null,
    primary key(nation_number)
);

alter table city add column nation_number bigint null, add constraint fk_city_nation foreign key (nation_number) references nation(nation_number);
alter table city modify column nation_number bigint not null;

