-- 会员卡信息表
USE aixiuji;
DROP TABLE IF EXISTS card;
CREATE TABLE IF NOT EXISTS card(
  id int primary key AUTO_INCREMENT comment '自增主键，无具体含义',
  date varchar(20) comment '办卡日期',
  type int comment '卡类型:充值卡/剪发卡',
  amount int comment '卡初始金额',
  balance int comment '卡余额(元)',
  active boolean comment '是否激活',
  uid int comment '客户ID'
);
-- ALTER TABLE card ADD COLUMN active boolean AFTER balance;