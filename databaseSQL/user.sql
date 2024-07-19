create table user
(
    id       int auto_increment comment 'ID,唯一标识'
        primary key,
    level    int         default 0                                                                              not null comment '等级',
    username varchar(20) default '交小集'                                                                       not null comment '用户名',
    note     varchar(50) default '由所有属于集合A且属于集合B的元素所组成的集合，叫做集合A与集合B的交集，记作A∩B。' not null comment '个性签名',
    email    varchar(50)                                                                                        not null comment '邮箱',
    state    int         default 0                                                                              not null,
    avatar   mediumtext                                                                                         not null comment '头像',
    constraint email
        unique (email)
)
    comment '用户表';

INSERT INTO jiaoji.user (id, level, username, note, email, state, avatar) VALUES (1, 1, 'Manager', 'I am the manager, managing everything!', '11111111@qq.com', 0, 'https://randomuser.me/api/portraits/men/36.jpg');
INSERT INTO jiaoji.user (id, level, username, note, email, state, avatar) VALUES (2, 3, '交小集', '由所有属于集合A且属于集合B的元素所组成的集合，叫做集合A与集合B的交集，记作A∩B。', '22222222@qq.com', 0, 'https://randomuser.me/api/portraits/men/36.jpg');
INSERT INTO jiaoji.user (id, level, username, note, email, state, avatar) VALUES (3, 6, '李华', '我是李华，英语水平不好。', '33333333@qq.com', 0, 'https://randomuser.me/api/portraits/men/36.jpg');
INSERT INTO jiaoji.user (id, level, username, note, email, state, avatar) VALUES (12, 0, 'sunday', '由所有属于集合A且属于集合B的元素所组成的集合，叫做集合A与集合B的交集，记作A∩B。', '824987073@qq.com', 0, 'https://randomuser.me/api/portraits/men/36.jpg');
