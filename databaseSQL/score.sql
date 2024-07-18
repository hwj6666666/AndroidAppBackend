create table score
(
    id       int auto_increment
        primary key,
    objectId int           not null,
    score_2  int default 0 not null,
    score_4  int default 0 not null,
    score_6  int default 0 not null,
    score_8  int default 0 not null,
    score_10 int default 0 not null,
    constraint score_object_id_fk
        foreign key (objectId) references object (id)
            on update cascade on delete cascade
)
    comment '对于某个对象，各个评分的各有多少人';

