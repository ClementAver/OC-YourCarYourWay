create table users
(
    id         int auto_increment
        primary key,
    created_at datetime(6)                   null,
    email      varchar(256)                  not null,
    name       varchar(256)                  not null,
    password   varchar(255)                  not null,
    role       enum ('CUSTOMER', 'EMPLOYEE') not null,
    updated_at datetime(6)                   null
);

create table chats
(
    id          int auto_increment
        primary key,
    created_at  datetime(6) null,
    pending     bit         not null,
    updated_at  datetime(6) null,
    customer_id int         null,
    employee_id int         null,
    constraint FK6ehxf9bwgs5ooyj0vocw8rlg5
        foreign key (employee_id) references users (id),
    constraint FKd8bqp0pqgib0ysdh7qsrirova
        foreign key (customer_id) references users (id)
);

create table messages
(
    id         int auto_increment
        primary key,
    content    varchar(256) not null,
    created_at datetime(6)  null,
    chat_id    int          null,
    author_id  int          null,
    constraint FK64w44ngcpqp99ptcb9werdfmb
        foreign key (chat_id) references chats (id),
    constraint FKowtlim26svclkatusptbgi7u1
        foreign key (author_id) references users (id)
);

