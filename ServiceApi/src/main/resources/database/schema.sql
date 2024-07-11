DROP DATABASE IF EXISTS renyibang_service;
CREATE DATABASE renyibang_service;
USE renyibang_service;

create table service
(
    service_id  bigint auto_increment comment '服务id'
        primary key,
    owner_id    bigint                              not null comment '服务发布者id',
    title       varchar(32)                         not null comment '服务标题',
    images      longtext                            null comment '服务图片',
    description text                                null comment '服务描述',
    price       bigint    default 0                 not null comment '服务价格(存储100倍价格)',
    created_at  timestamp default CURRENT_TIMESTAMP not null comment '服务发布时间',
    max_access  int       default 1                 not null comment '服务最大购买数',
    rating      tinyint   default 50                not null comment '任务评分(存储10倍评分,范围0~100)',
    collected   bigint                              not null comment '服务收藏数',
    status      tinyint   default 0                 not null comment '服务状态'
)
    comment '服务表';

create table service_access
(
    service_access_id bigint auto_increment comment '服务购买候选id'
        primary key,
    service_id        bigint                              not null comment '服务id',
    accessor_id       bigint                              not null comment '购买者id',
    created_at        timestamp default CURRENT_TIMESTAMP not null comment '购买时间',
    valid             tinyint(1)                          not null comment '服务购买者是否取消购买',
    constraint service_access_ibfk_1
        foreign key (service_id) references service (service_id)
            on update cascade
)
    comment '服务购买候选表';

create index service_id
    on service_access (service_id);

create table service_collect
(
    service_collect_id bigint auto_increment comment '服务收藏id'
        primary key,
    service_id         bigint                              not null comment '服务id',
    collector_id       bigint                              not null comment '收藏者id',
    created_at         timestamp default CURRENT_TIMESTAMP not null comment '收藏时间',
    constraint service_collect_ibfk_1
        foreign key (service_id) references service (service_id)
            on update cascade
)
    comment '服务收藏表';

create index service_id
    on service_collect (service_id);

create table service_comment
(
    service_comment_id bigint auto_increment comment '服务评论id'
        primary key,
    service_id         bigint                              not null comment '服务id',
    commenter_id       bigint                              not null comment '服务评论者id',
    content            text                                not null comment '服务评论内容',
    created_at         timestamp default CURRENT_TIMESTAMP not null comment '服务评论时间',
    rating             tinyint   default 50                not null comment '服务评论评分(存储10倍评分,范围0~100)',
    liked_number       bigint                              not null comment '服务评论点赞数',
    constraint service_comment_ibfk_1
        foreign key (service_id) references service (service_id)
            on update cascade
)
    comment '服务评论表(仅有购买服务者才能评论)';

create index service_id
    on service_comment (service_id);

create table service_comment_like
(
    service_comment_like_id bigint auto_increment
        primary key,
    service_comment_id      bigint not null,
    liker_id                bigint not null,
    constraint service_comment_like_service_comment_service_comment_id_fk
        foreign key (service_comment_id) references service_comment (service_comment_id)
);

create table service_message
(
    service_message_id bigint auto_increment comment '服务留言id'
        primary key,
    service_id         bigint                              not null comment '服务id',
    messager_id        bigint                              not null comment '留言者id',
    content            text                                not null comment '消息内容',
    created_at         timestamp default CURRENT_TIMESTAMP not null comment '消息发送时间',
    liked_number       bigint                              not null comment '服务留言点赞数',
    constraint service_message_ibfk_1
        foreign key (service_id) references service (service_id)
            on update cascade
)
    comment '服务留言表';

create index service_id
    on service_message (service_id);

create table service_message_like
(
    service_message_id      bigint not null,
    liker_id                bigint not null,
    service_message_like_id bigint auto_increment
        primary key,
    constraint service_message_like_service_message_service_message_id_fk
        foreign key (service_message_id) references service_message (service_message_id)
);

