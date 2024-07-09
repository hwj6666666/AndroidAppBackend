create table chat_item
(
    id             int auto_increment
        primary key,
    mem_1          int          not null,
    mem_2          int          not null,
    newest_content varchar(500) not null,
    newest_time    datetime     not null
);

INSERT INTO jiaoji.chat_item (id, mem_1, mem_2, newest_content, newest_time) VALUES (4, 1, 2, 'enter', '2024-07-02 15:11:08');
INSERT INTO jiaoji.chat_item (id, mem_1, mem_2, newest_content, newest_time) VALUES (5, 1, 3, 'here user1', '2024-07-03 14:00:31');
INSERT INTO jiaoji.chat_item (id, mem_1, mem_2, newest_content, newest_time) VALUES (6, 2, 3, '3to2', '2024-07-01 16:22:14');
