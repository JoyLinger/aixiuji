# 爱秀季 - 美发店管理系统

## 项目简介
爱秀季是一个功能完善的美发店管理系统，用于管理美发店的日常运营，包括客户管理、会员卡管理、账单记录、充值消费、统计分析等功能。

## 技术栈

### 后端技术
- **Spring Boot 2.3.0** - Web 应用框架
- **Spring Data JPA** - 数据持久化框架
- **MyBatis** - ORM 框架
- **MySQL 8.0** - 关系型数据库
- **Spring Session** - Session 管理（JDBC 存储）
- **Lombok** - 简化 Java 代码
- **FastJSON** - JSON 处理
- **Apache Commons Lang3** - 工具类库

### 前端技术
- **Bootstrap 5.1.3** - UI 框架
- **Thymeleaf** - 模板引擎
- **DataTables** - 数据表格插件
- **ECharts** - 图表可视化库
- **My97DatePicker** - 日期选择器
- **Vue 2.6.11** - 前端框架

### 其他工具
- **Maven** - 项目构建工具
- **SLF4J + Logback** - 日志框架

## 主要功能

### 1. 前台收银
- 散户收银：非会员用户消费记账
- 会员刷卡：会员用户（充值过的用户）消费记账

### 2. 开户办卡
- 开户：会员登记
- 办卡：会员卡充值

### 3. 后台管理
- 会员管理：会员用户管理
- 消费查询：客户订单查询
- 页面数据管理：页面配置项管理

### 4. 经营统计
- 营业额统计：按月统计收入和客户量，表格展现
- 顾客量统计：按月、客户类型统计客户量，表格展现
- 收入走势：按年/月/日统计收入和顾客人数，走势图可视化
- 顾客走势：按年/月/日统计会员、新顾客、老顾客人数，走势图可视化

## 项目结构

```
aixiuji/
├── src/main/java/com/jt/customer/
│   ├── controller/          # 控制器层
│   │   ├── BillController.java
│   │   ├── CardController.java
│   │   ├── ChargeController.java
│   │   ├── ContrastController.java
│   │   ├── LoginController.java
│   │   ├── StatController.java
│   │   └── VipController.java
│   ├── dao/                 # 数据访问层
│   ├── entity/              # 实体类
│   ├── filter/              # 过滤器
│   ├── service/             # 服务层
│   └── util/                # 工具类
├── src/main/resources/
│   ├── static/              # 静态资源
│   │   ├── css/
│   │   ├── js/
│   │   ├── img/
│   │   └── plugins/         # 第三方插件
│   ├── templates/           # Thymeleaf 模板
│   ├── sql/                 # 数据库脚本
│   ├── application.yml      # 配置文件
│   └── logback-spring.xml   # 日志配置
├── deploy/                  # 部署相关文件
├── logs/                    # 日志文件
└── pom.xml                  # Maven 配置
```

## 快速开始

### 环境要求
- JDK 8+
- Maven 3.6+
- MySQL 8.0+

### 数据库配置
1. 创建数据库 `aixiuji`
2. 修改 `application.yml` 中的数据库配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/aixiuji?characterEncoding=utf8
    username: root
    password: root
```

### 运行项目
1. 克隆项目到本地
2. 使用 Maven 编译：`mvn clean compile`
3. 启动应用：`mvn spring-boot:run`
4. 访问：`http://localhost:8080`

### 打包部署

#### 方式一：JAR 包方式
```bash
# 打包成 JAR
mvn package

# 运行 JAR
java -jar target/customer-1.0.0.jar
```

#### 方式二：EXE 可执行文件（Windows）

项目提供了使用 exe4j 将 JAR 打包成 Windows 可执行文件的方式。

**打包步骤：**
1. 先去任务管理器把当前运行的"爱秀季.exe"结束进程
2. 备份"爱秀季.exe"
3. 打开 `deploy/exe4j/aixiuji.exe4j` 配置文件
4. 直接点击"完成"，将会重新生成"爱秀季.exe"
5. 运行"爱秀季.exe"

**文件位置：**
- exe4j 配置文件：`deploy/exe4j/aixiuji.exe4j`
- 生成的 exe 文件：`deploy/exe4j/爱秀季.exe`

**注意事项：**
- 确保已安装 JDK 或 JRE（Java 运行环境）
- 确保 MySQL 数据库已启动并配置正确
- exe 运行时会在同目录下生成日志文件

## 配置说明

### application.yml 主要配置
- **server.port**: 服务端口（默认 8080）
- **spring.session.timeout**: Session 超时时间（默认 1800 秒）
- **spring.jpa.hibernate.ddl-auto**: 数据库表更新策略（update）

## 数据库表结构
项目包含以下主要数据表：
- **bill**: 账单表
- **card**: 会员卡表
- **vip**: VIP 表
- **user**: 用户表
- **contrast_key** / **contrast_value**: 数据对比表
- **SPRING_SESSION**: Session 存储表（自动创建）

## 特色功能

1. **多周期统计** - 支持按年、月、日统计收入和客户数量
2. **可视化图表** - 使用 ECharts 展示收入走势图和顾客走势图
3. **Session 持久化** - 使用 JDBC 存储 Session，支持集群部署
4. **数据过滤** - 支持多条件筛选和查询
5. **分页查询** - DataTables 支持的数据表格分页

## 许可证
本项目仅供学习和参考使用。

## 联系方式
如有问题或建议，请联系项目维护者。

---
*爱秀季 - 让美发店管理更简单！*
