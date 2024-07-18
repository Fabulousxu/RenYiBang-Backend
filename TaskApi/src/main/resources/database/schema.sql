DROP DATABASE IF EXISTS renyibang_task;
CREATE DATABASE renyibang_task;
USE renyibang_task;

create table task
(
    task_id     bigint auto_increment comment '任务id'
        primary key,
    owner_id    bigint                              not null comment '任务发布者id',
    title       varchar(32)                         not null comment '任务标题',
    images      longtext                            null comment '任务图片',
    description text                                null comment '任务描述',
    price       bigint    default 0                 not null comment '任务价格(存储100倍价格)',
    created_at  timestamp default CURRENT_TIMESTAMP not null comment '任务发布时间',
    max_access  int       default 1                 not null comment '任务最大接取数',
    rating      tinyint   default 50                not null comment '任务评分(存储10倍评分,范围0~100)',
    collected   bigint                              not null comment '任务收藏数',
    status      tinyint   default 0                 not null comment '任务状态'
)
    comment '任务表';

create table task_access
(
    task_access_id bigint auto_increment comment '任务接取候选id'
        primary key,
    task_id        bigint                              not null comment '任务id',
    accessor_id    bigint                              not null comment '接取者id',
    created_at     timestamp default CURRENT_TIMESTAMP not null comment '接取时间',
    valid          tinyint(1)                          not null comment '接取者是否被拒绝',
    constraint task_access_ibfk_1
        foreign key (task_id) references task (task_id)
            on update cascade
)
    comment '任务接取候选表';

create index task_id
    on task_access (task_id);

create table task_collect
(
    task_collect_id bigint auto_increment comment '任务收藏id'
        primary key,
    task_id         bigint                              not null comment '任务id',
    collector_id    bigint                              not null comment '收藏者id',
    created_at      timestamp default CURRENT_TIMESTAMP not null comment '收藏时间',
    constraint task_collect_ibfk_1
        foreign key (task_id) references task (task_id)
            on update cascade
)
    comment '任务收藏表';

create index task_id
    on task_collect (task_id);

create table task_comment
(
    task_comment_id bigint auto_increment comment '任务评论id'
        primary key,
    task_id         bigint                              not null comment '任务id',
    commenter_id    bigint                              not null comment '任务评论者id',
    content         text                                not null comment '任务评论内容',
    created_at      timestamp default CURRENT_TIMESTAMP not null comment '任务评论时间',
    rating          tinyint   default 50                not null comment '任务评论评分(存储10倍评分,范围0~100)',
    liked_number    bigint                              not null comment '任务评论点赞数',
    constraint task_comment_ibfk_1
        foreign key (task_id) references task (task_id)
            on update cascade
)
    comment '任务评论表(仅有接取任务者才能评论)';

create index task_id
    on task_comment (task_id);

create table task_comment_like
(
    task_comment_like_id bigint auto_increment
        primary key,
    task_comment_id      bigint not null,
    liker_id             bigint not null,
    constraint task_comment_like_task_comment_task_comment_id_fk
        foreign key (task_comment_id) references task_comment (task_comment_id)
);

create table task_message
(
    task_message_id bigint auto_increment comment '任务留言id'
        primary key,
    task_id         bigint                              not null comment '任务id',
    messager_id     bigint                              not null comment '留言者id',
    content         text                                not null comment '消息内容',
    created_at      timestamp default CURRENT_TIMESTAMP not null comment '消息发送时间',
    liked_number    bigint                              not null comment '任务留言点赞数',
    constraint task_message_ibfk_1
        foreign key (task_id) references task (task_id)
            on update cascade
)
    comment '任务留言表';

create index task_id
    on task_message (task_id);

create table task_message_like
(
    task_message_id      bigint not null,
    liker_id             bigint not null,
    task_message_like_id bigint auto_increment
        primary key,
    constraint task_message_like_task_message_task_message_id_fk
        foreign key (task_message_id) references task_message (task_message_id)
);

