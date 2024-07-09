create table class
(
    id    int auto_increment comment 'Primary Key'
        primary key,
    name  varchar(255) null comment '分类名',
    `key` char(50)     not null
);

INSERT INTO jiaoji.class (id, name, `key`) VALUES (1, '全部', '0');
INSERT INTO jiaoji.class (id, name, `key`) VALUES (2, '美食', '1');
INSERT INTO jiaoji.class (id, name, `key`) VALUES (3, '知识', '2');
INSERT INTO jiaoji.class (id, name, `key`) VALUES (4, '娱乐', '3');
INSERT INTO jiaoji.class (id, name, `key`) VALUES (5, '汽车', '4');
INSERT INTO jiaoji.class (id, name, `key`) VALUES (6, '影视', '5');
INSERT INTO jiaoji.class (id, name, `key`) VALUES (7, '人文', '6');
INSERT INTO jiaoji.class (id, name, `key`) VALUES (8, '体育', '7');
INSERT INTO jiaoji.class (id, name, `key`) VALUES (9, '动植物', '8');
INSERT INTO jiaoji.class (id, name, `key`) VALUES (10, '游戏', '9');
INSERT INTO jiaoji.class (id, name, `key`) VALUES (11, '科技', '10');
INSERT INTO jiaoji.class (id, name, `key`) VALUES (12, '其他', '11');
