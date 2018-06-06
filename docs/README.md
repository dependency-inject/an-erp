## 电子光码盘生产管理系统

一个很优秀的 erp 系统。

### 项目结构

```shell
├── backend		———— 项目后端代码
├── docs		———— 项目文档
└── frontend	———— 项目前端代码
```

### 基础环境依赖

* jdk7 或 jdk8
* tomcat7 或 tomcat8
* npm <= 5.8

### 搭建开发环境

**后端部分：**

在 **IntelliJ IDEA 专业版** 中 Open Project，选择项目 `backend` 目录。

打开成功后（pom 包导入完成），对项目目录 `src/main/resources` 点击右键，选择 `Mark Directory as` > `Resources Root`。

!> 若配置成功，打开 Project Struct，进入到 Modules > Spring 选项下，应该能看到两个配置文件 spring-mybatis.xml 和 spring-mvc.xml 被自动检测成功。

然后，对项目新增配置 tomcat 运行环境。注意在 Deployment 选项中，选择 `erp:war exploded`。

**前端部分：**

命令行下，进入到项目跟目录，执行

```shell
cd frontend
npm install
```

等待至 `node_modules` 目录下载完成。

### 运行开发环境

开发环境运行时，先运行后端部分：在 IntelliJ IDEA 中启动 tomcat，运行项目；

再运行前端部分：命令行下执行

```shell
npm run watch
```

默认在 IntelliJ IDEA 中启动 tomcat 运行项目时，代码会打包发布到 `backend/target/erp-1.0-SNAPSHOT` 目录下，故前端开发环境下打包也会发布到这个位置。如果在你的本地项目表现有所不同，请修改 `frontend/webpack.mix.js` 文件中的配置。

```javascript
if (mix.inProduction()) {
	publicPath = '../backend/src/main/webapp/static';
} else {
	publicPath = '../backend/target/erp-1.0-SNAPSHOT/static'; // 修改这一行
}
```

开发环境运行后，前端部分若有代码修改，保存后会自动重新打包代码。代码完成时，计算机右下角会有 **Laravel Mix** 的提示，此时需要刷新网页才能看到效果。

?> 由于打包后的资源文件名是相同的，可能会出现资源文件缓存不更新的情况。出现这种情况时，请打开你的浏览器开发者工具（Chrome 下按 F12 可打开），切换到 Network 标签下，并勾选 Disable cache，再刷新网页即可。

### 测试账号（全权限）

登录名：admin

密码：admin

### 数据库配置

数据库连接信息位于 `backend/src/main/resources/jdbc.properties` 文件中。

### 项目部署


