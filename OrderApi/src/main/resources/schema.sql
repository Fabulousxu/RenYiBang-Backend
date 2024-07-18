DROP DATABASE IF EXISTS renyibang_order;
CREATE DATABASE renyibang_order;
USE renyibang_order;

CREATE TABLE `order`
(
    order_id    BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单id',
    type        TINYINT UNSIGNED DEFAULT 0 NOT NULL COMMENT '订单类型(0:任务订单,1:服务订单)',
    owner_id    BIGINT                     NOT NULL COMMENT '订单发布者id',
    accessor_id BIGINT                     NOT NULL COMMENT '订单接取者id',
    status      TINYINT UNSIGNED DEFAULT 0 NOT NULL COMMENT '订单状态(0:未付款,1:已付款,任务/服务进行中,2:接取者已完成，等待发布者确认,3:发布者已确认完成,4:订单已取消)',
    cost        BIGINT           DEFAULT 0 NOT NULL COMMENT '订单价格(存储100倍价格)',
    item_id     BIGINT                     NOT NULL COMMENT '任务/服务id'
) COMMENT '订单表';

