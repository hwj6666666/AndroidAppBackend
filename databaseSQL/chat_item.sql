create table chat_item
(
    id             int auto_increment
        primary key,
    mem_1          int          not null,
    mem_2          int          not null,
    newest_content varchar(500) not null,
    newest_time    datetime     not null,
    constraint chat_item_user_id_fk
        foreign key (mem_1) references user (id)
            on update cascade on delete cascade,
    constraint chat_item_user_id_fk_2
        foreign key (mem_2) references user (id)
            on update cascade on delete cascade
);

