-- 充值、消费记录表
USE aixiuji;
DROP TABLE IF EXISTS bill;
CREATE TABLE IF NOT EXISTS bill(
  id int primary key AUTO_INCREMENT comment '自增主键，无具体含义',
  date varchar(20) comment '日期',
  operation int comment '操作',
  role int comment '角色',
  uid int comment '客户号',
  cid int comment '会员卡ID',
  pay_method int comment '付款方式:微信/支付宝/现金'
);