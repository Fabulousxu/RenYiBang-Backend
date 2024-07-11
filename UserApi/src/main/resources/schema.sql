DROP DATABASE IF EXISTS renyibang_user;
CREATE DATABASE renyibang_user;
USE renyibang_user;

CREATE TABLE user
(
    user_id   BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户id',
    type      TINYINT      DEFAULT 0  NOT NULL COMMENT '用户类型(0:普通用户,1:客服,2:管理员)',
    nickname  VARCHAR(16)  DEFAULT '' NOT NULL COMMENT '用户昵称',
    avatar    LONGTEXT                NOT NULL COMMENT '用户头像',
    intro     TEXT                    NOT NULL COMMENT '用户介绍',
    rating    TINYINT      DEFAULT 50 NOT NULL COMMENT '用户评分(存储10倍评分,范围0~100)',
    balance   BIGINT       DEFAULT 0  NOT NULL COMMENT '用户余额(存储100倍余额)',
    phone     VARCHAR(14)  DEFAULT '' NOT NULL COMMENT '用户手机号',
    email     VARCHAR(254) DEFAULT '' NOT NULL COMMENT '用户邮箱',
    following INT          DEFAULT 0  NOT NULL COMMENT '关注数',
    follower  INT          DEFAULT 0  NOT NULL COMMENT '粉丝数'
) comment '用户表';

CREATE TABLE auth
(
    user_id  BIGINT PRIMARY KEY COMMENT '用户id',
    password VARCHAR(16) NOT NULL COMMENT '用户密码',
    FOREIGN KEY (user_id) REFERENCES user (user_id) ON UPDATE CASCADE ON DELETE CASCADE
) COMMENT '用户密码表';

CREATE TABLE follow
(
    follower_id BIGINT NOT NULL COMMENT '关注者id',
    followee_id BIGINT NOT NULL COMMENT '被关注者id',
    PRIMARY KEY (follower_id, followee_id),
    FOREIGN KEY (follower_id) REFERENCES user (user_id) ON UPDATE CASCADE,
    FOREIGN KEY (followee_id) REFERENCES user (user_id) ON UPDATE CASCADE
) COMMENT '用户关注表';