--码表value表
USE aixiuji;
DROP TABLE IF EXISTS contrast_value;
CREATE TABLE IF NOT EXISTS contrast_value(
  id int comment '自增主键',
  name varchar(20) comment '名称',
  value_id int comment 'value id',
  key_id int comment 'key id,外键'
);
-- 初始化数据
-- 1,美发项目
INSERT INTO contrast_value(key_id,value_id,name) VALUES(1,0,'-');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(1,1,'剪发');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(1,2,'造型');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(1,3,'洗头');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(1,4,'护理');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(1,5,'染发');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(1,6,'烫发');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(1,7,'补根');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(1,8,'焗黑油');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(1,9,'剪+焗黑油');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(1,10,'烫刘海');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(1,11,'美发产品');
-- 2,账单类型
INSERT INTO contrast_value(key_id,value_id,name) VALUES(2,0,'-');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(2,1,'充值');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(2,2,'消费');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(2,3,'使用赠送项目');
-- 3,支付方式
INSERT INTO contrast_value(key_id,value_id,name) VALUES(3,0,'-');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(3,1,'微信');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(3,2,'支付宝');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(3,3,'现金');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(3,4,'划卡');
-- 4,客户类型
INSERT INTO contrast_value(key_id,value_id,name) VALUES(4,0,'-');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(4,1,'会员');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(4,2,'新客户');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(4,3,'老客户');
-- 5,会员卡类型
INSERT INTO contrast_value(key_id,value_id,name) VALUES(5,0,'-');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(5,1,'充值卡');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(5,2,'剪发卡');
-- 6,赠送次数
INSERT INTO contrast_value(key_id,value_id,name) VALUES(6,0,'-');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(6,1,'1');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(6,2,'2');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(6,3,'3');
INSERT INTO contrast_value(key_id,value_id,name) VALUES(6,4,'4');
select * from contrast_value;
