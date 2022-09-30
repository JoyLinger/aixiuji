--VIP贵宾信息表
USE aixiuji;
DROP TABLE IF EXISTS vip;
CREATE TABLE IF NOT EXISTS vip(
  uid int primary key AUTO_INCREMENT comment '客户号',
  name varchar(20) comment '客户姓名',
  balance int comment '余额(元)',
  bonus varchar(50) comment '赠送',
  remark varchar(50) comment '备注',
  tel varchar(20) comment '手机号'
);