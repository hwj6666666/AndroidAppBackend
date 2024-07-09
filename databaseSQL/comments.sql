create table comments
(
    id           int auto_increment
        primary key,
    user_id      int          not null,
    remark_id    int          not null,
    content      varchar(100) null,
    publish_time datetime     null
);

INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (1, 3, 1, '感觉配不上5星', '2023-08-20 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (2, 2, 3, '位置也没那么夸张吧', '2023-08-25 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (3, 1, 5, '离我宿舍不近', '2024-01-06 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (4, 3, 8, '还真是', '2024-04-12 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (5, 2, 10, '绷', '2024-03-24 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (6, 1, 12, '确实少了点', '2024-04-17 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (7, 3, 13, '兄弟，你好香', '2024-02-10 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (8, 2, 16, '确实，感觉口感挺一般的', '2024-01-15 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (9, 1, 17, '你在哪买的觉得贵', '2024-03-04 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (10, 3, 20, '谁问你了', '2024-02-25 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (11, 2, 22, '下一届就改革了，哈哈大笑了', '2024-04-12 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (12, 1, 24, '做不来实验+1，点了', '2024-01-16 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (13, 3, 25, '鼠鼠也学不会捏（大哭', '2024-04-04 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (14, 2, 28, '这也能拉踩？奇葩真多', '2024-02-15 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (15, 1, 30, '挺喜欢这种南美小说的', '2024-03-25 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (16, 3, 32, '老哥看破不说破', '2024-04-01 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (17, 2, 34, '我我我', '2024-01-04 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (18, 1, 35, '还真是，一想到就痛苦面具了', '2024-02-05 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (19, 3, 37, '网易云是这样的', '2024-03-30 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (20, 2, 40, '我我我我我！！', '2024-01-14 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (21, 1, 42, '敬你一杯，说出你的故事', '2024-02-24 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (22, 3, 44, '还真是，一开始疯四还有点性价比', '2024-03-14 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (23, 2, 46, '你牛你来开？', '2024-04-11 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (24, 1, 48, '最喜欢下沉市场的一集', '2024-01-21 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (25, 3, 50, '上市是这样的，先是叔叔现在是阿姨捏', '2024-01-18 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (26, 2, 52, '看B站看的', '2024-02-10 00:00:00');
INSERT INTO jiaoji.comments (id, user_id, remark_id, content, publish_time) VALUES (27, 1, 54, '我也', '2024-01-29 00:00:00');
