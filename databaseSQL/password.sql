create table password
(
    id       int auto_increment
        primary key,
    email    varchar(64)  not null,
    password varchar(256) not null,
    uid      int          not null,
    salt     varchar(32)  not null,
    constraint password_user_email_id_fk
        foreign key (email, uid) references user (email, id)
            on update cascade on delete cascade
);

INSERT INTO jiaoji.password (id, email, password, uid, salt) VALUES (1, '11111111@qq.com', '88888888', 1, 'abc');
INSERT INTO jiaoji.password (id, email, password, uid, salt) VALUES (2, '22222222@qq.com', '88888888', 2, 'abc');
INSERT INTO jiaoji.password (id, email, password, uid, salt) VALUES (3, '33333333@qq.com', '88888888', 3, 'abc');
INSERT INTO jiaoji.password (id, email, password, uid, salt) VALUES (6, '824987073@qq.com', 'n0wvbmEKvOvyvFysXlNF2rewbjmATWpshE3HiLy1Q6Q=', 12, 'tmQPlAyYuQvVz9PtcsVfxw==');
