create table chat
(
    chat_id    int          not null,
    send_id    int          not null,
    receive_id int          not null,
    content    varchar(500) not null,
    time       datetime     null,
    constraint chat_chat_item_id_fk
        foreign key (chat_id) references chat_item (id)
            on update cascade on delete cascade
);

INSERT INTO jiaoji.chat (chat_id, send_id, receive_id, content, time) VALUES (4, 1, 2, '1to2', '2024-07-01 16:21:24');
INSERT INTO jiaoji.chat (chat_id, send_id, receive_id, content, time) VALUES (5, 1, 3, '1to3', '2024-07-01 16:21:34');
INSERT INTO jiaoji.chat (chat_id, send_id, receive_id, content, time) VALUES (4, 2, 1, '2to1', '2024-07-01 16:21:48');
INSERT INTO jiaoji.chat (chat_id, send_id, receive_id, content, time) VALUES (6, 2, 3, '2to3', '2024-07-01 16:21:56');
INSERT INTO jiaoji.chat (chat_id, send_id, receive_id, content, time) VALUES (5, 3, 1, '3to1', '2024-07-01 16:22:07');
INSERT INTO jiaoji.chat (chat_id, send_id, receive_id, content, time) VALUES (6, 3, 2, '3to2', '2024-07-01 16:22:14');
INSERT INTO jiaoji.chat (chat_id, send_id, receive_id, content, time) VALUES (5, 1, 3, 'one', '2024-07-03 13:49:30');
INSERT INTO jiaoji.chat (chat_id, send_id, receive_id, content, time) VALUES (5, 1, 3, 'Q', '2024-07-03 13:49:59');
INSERT INTO jiaoji.chat (chat_id, send_id, receive_id, content, time) VALUES (5, 1, 3, 'here user1', '2024-07-03 14:00:31');
