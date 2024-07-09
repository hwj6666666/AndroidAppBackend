create table remarks
(
    id           int auto_increment
        primary key,
    user_id      int           not null,
    object_id    int           not null,
    content      varchar(100)  null,
    `like`       int default 0 null,
    score        tinyint       not null,
    publish_time datetime      null
);

INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (1, 1, 1, '挺不错的，很好吃', 2, 10, '2023-08-19 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (2, 2, 1, '虽说不是最好的，但也凑合', 3, 8, '2023-05-17 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (3, 3, 2, '高峰找不到位置坐', 5, 6, '2023-08-20 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (4, 1, 2, '好吃好吃好吃好吃好吃好吃', 7, 10, '2023-08-18 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (5, 2, 3, '离宿舍近', 11, 8, '2023-04-23 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (6, 3, 3, '非常一般，凑合吃吃吧', 13, 6, '2023-09-17 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (7, 1, 4, '下院都是水课，挺爽的', 17, 8, '2023-08-18 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (8, 2, 4, '离一餐最近，win', 19, 10, '2023-08-01 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (9, 3, 5, '普普通通的教学教室', 23, 6, '2023-06-01 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (10, 1, 5, '不想上课', 29, 4, '2023-09-17 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (11, 2, 6, '离餐馆太远，离硬课太近', 31, 2, '2023-07-27 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (12, 3, 6, '教室插头太少', 37, 4, '2023-04-21 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (13, 1, 7, '香香香香香，最喜欢的一集', 41, 10, '2023-03-23 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (14, 2, 7, '挺好吃的', 43, 8, '2023-08-22 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (15, 3, 8, '还行吧，大肉丸子', 47, 8, '2023-10-22 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (16, 1, 8, '部分餐厅淀粉放太多了', 1, 6, '2023-04-19 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (17, 2, 9, '香，但是有点贵', 4, 6, '2023-11-17 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (18, 3, 9, '羊肉串yyds', 9, 10, '2023-12-20 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (19, 1, 10, '依托答辩，交大金课', 16000, 2, '2023-03-18 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (20, 2, 10, '鼠鼠组队的时候脱单了捏', 2, 10, '2023-02-23 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (21, 3, 11, '积累小组开发经验，讲解软件架构', 36, 10, '2023-01-17 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (22, 1, 11, '还没上完web就来学这个，答辩', 49, 2, '2023-02-18 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (23, 2, 12, '金课，下一个', 67, 2, '2023-07-01 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (24, 3, 12, '做不来实验', 3, 6, '2023-04-01 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (25, 1, 13, '你交特色，可惜我学不会', 8, 6, '2023-03-17 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (26, 2, 13, '学会了很多东西，挺好的课', 27, 10, '2023-10-23 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (27, 3, 14, 'C++经典教材，伟大无需多言', 64, 8, '2023-04-17 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (28, 1, 14, '不如plus', 2, 4, '2023-12-27 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (29, 2, 15, '读完醍醐灌顶', 6, 10, '2023-07-21 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (30, 3, 15, '感谢这本书帮我走出精神内耗', 12, 8, '2023-01-23 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (31, 1, 16, '傍晚散步的好去处', 20, 10, '2023-05-22 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (32, 2, 16, '楼上那位不是单身吧，傍晚全是情侣', 30, 4, '2023-06-22 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (33, 3, 17, '桌游圣地', 42, 8, '2023-08-09 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (34, 1, 17, '有没有打日麻的一起', 56, 10, '2023-05-20 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (35, 2, 18, '下雨天就来这b地方上强度', 72, 2, '2023-12-22 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (36, 3, 18, '杨氏太极拳yyds', 100, 10, '2023-11-11 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (37, 1, 19, '呜呜呜网易云什么时候买版权', 2, 10, '2023-09-18 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (38, 2, 19, '神作', 3, 10, '2023-12-23 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (39, 3, 20, '不错', 4, 8, '2023-10-17 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (40, 1, 20, '谦友集合！！！！', 5, 10, '2023-07-23 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (41, 2, 21, '最喜欢的一首英文歌', 6, 8, '2023-04-16 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (42, 3, 21, '哎，那个夏天回不去了', 7, 10, '2023-08-17 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (43, 1, 22, '星期四固定节目', 12, 8, '2023-01-24 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (44, 2, 22, '越卖越贵', 13, 2, '2023-06-10 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (45, 3, 23, '学校里有，win', 13, 8, '2023-01-19 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (46, 1, 23, '巅峰期经常中断营业', 14, 2, '2023-01-29 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (47, 2, 24, '薄纱必胜客', 15, 10, '2023-01-24 00:00:00');
INSERT INTO jiaoji.remarks (id, user_id, object_id, content, `like`, score, publish_time) VALUES (48, 3, 24, '便宜就是王道', 0, 8, '2023-06-21 00:00:00');
