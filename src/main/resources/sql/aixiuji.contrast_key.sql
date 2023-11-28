-- 码表key表
USE aixiuji;
DROP TABLE IF EXISTS contrast_key;
CREATE TABLE IF NOT EXISTS contrast_key(
  id int primary key AUTO_INCREMENT comment '自增主键,key id',
  name varchar(20) comment '名称',
  attr varchar(20) comment '会话中的变量名'
);
-- 初始化数据
INSERT INTO contrast_key(attr,name) VALUES('projects','美发项目');
INSERT INTO contrast_key(attr,name) VALUES('operations','账单类型');
INSERT INTO contrast_key(attr,name) VALUES('payMethods','支付方式');
INSERT INTO contrast_key(attr,name) VALUES('roles','客户类型');
INSERT INTO contrast_key(attr,name) VALUES('cardTypes','会员卡类型');
INSERT INTO contrast_key(attr,name) VALUES('bonusTimes','赠送次数');
select * from contrast_key;
