create table chat
(
    chat_id    int          not null,
    send_id    int          not null,
    receive_id int          not null,
    content    varchar(500) not null,
    time       datetime     null,
    constraint chat_chat_item_id_fk
        foreign key (chat_id) references chat_item (id)
            on update cascade on delete cascade,
    constraint chat_user_id_fk
        foreign key (receive_id) references user (id)
            on update cascade on delete cascade,
    constraint chat_user_id_fk_2
        foreign key (send_id) references user (id)
            on update cascade on delete cascade
);

