create table score
(
    id        int auto_increment
        primary key,
    object_id int           not null,
    score2    int default 0 not null,
    score4    int default 0 not null,
    score6    int default 0 not null,
    score8    int default 0 not null,
    score10   int default 0 not null,
    constraint score_object_id_fk
        foreign key (object_id) references object (id)
            on update cascade on delete cascade
)
    comment '对于某个对象，各个评分的各有多少人';

INSERT INTO jiaoji.score (id, object_id, score2, score4, score6, score8, score10) VALUES (1, 1, 0, 0, 0, 1, 0);
INSERT INTO jiaoji.score (id, object_id, score2, score4, score6, score8, score10) VALUES (2, 2, 0, 0, 1, 0, 1);
INSERT INTO jiaoji.score (id, object_id, score2, score4, score6, score8, score10) VALUES (3, 3, 0, 0, 1, 1, 0);
INSERT INTO jiaoji.score (id, object_id, score2, score4, score6, score8, score10) VALUES (4, 4, 0, 0, 0, 1, 1);
INSERT INTO jiaoji.score (id, object_id, score2, score4, score6, score8, score10) VALUES (5, 5, 0, 1, 1, 0, 0);
INSERT INTO jiaoji.score (id, object_id, score2, score4, score6, score8, score10) VALUES (6, 6, 1, 1, 0, 0, 0);
INSERT INTO jiaoji.score (id, object_id, score2, score4, score6, score8, score10) VALUES (7, 7, 0, 0, 0, 1, 1);
INSERT INTO jiaoji.score (id, object_id, score2, score4, score6, score8, score10) VALUES (8, 8, 0, 0, 1, 1, 0);
INSERT INTO jiaoji.score (id, object_id, score2, score4, score6, score8, score10) VALUES (9, 9, 0, 0, 1, 0, 1);
INSERT INTO jiaoji.score (id, object_id, score2, score4, score6, score8, score10) VALUES (10, 10, 1, 0, 0, 0, 1);
INSERT INTO jiaoji.score (id, object_id, score2, score4, score6, score8, score10) VALUES (11, 11, 1, 0, 0, 0, 1);
INSERT INTO jiaoji.score (id, object_id, score2, score4, score6, score8, score10) VALUES (12, 12, 1, 0, 1, 0, 0);
INSERT INTO jiaoji.score (id, object_id, score2, score4, score6, score8, score10) VALUES (13, 13, 0, 0, 1, 0, 1);
INSERT INTO jiaoji.score (id, object_id, score2, score4, score6, score8, score10) VALUES (14, 14, 0, 1, 0, 1, 0);
INSERT INTO jiaoji.score (id, object_id, score2, score4, score6, score8, score10) VALUES (15, 15, 0, 0, 0, 1, 1);
INSERT INTO jiaoji.score (id, object_id, score2, score4, score6, score8, score10) VALUES (16, 16, 0, 1, 0, 0, 1);
