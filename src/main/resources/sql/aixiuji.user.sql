--登录用户信息表
USE aixiuji;
DROP TABLE IF EXISTS user;
CREATE TABLE IF NOT EXISTS user(
  uid int primary key AUTO_INCREMENT comment '账号id,自增主键',
  name varchar(20) comment '账号',
  password varchar(20) comment '密码',
  role int comment '角色',
  remark varchar(50) comment '备注'
);
INSERT INTO user VALUES(null,'jt','jitong8556863',0,'管理员');