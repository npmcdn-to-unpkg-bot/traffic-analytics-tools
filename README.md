#traffic-analytics tools(流量优化工具)

这是一个用nodejs开发的用于分析Google Analytics流量的后台，运行这个项目前，你需要执行如下的步骤：

*  安装Mongodb
*  安装Redis
*  在Google Analytics后台申请API代码访问的权限（生成密钥文件并download）


项目的目录结构：

*  biz:业务逻辑层
*  models:实体类，用于访问Mongodb && Redis
*  routes.js:路由信息


在做完以上步骤之后，需要把models下的mongodb.js和redis-store.js中的URL修改掉，并且把GoogleAnalytics的权限验证文件放在biz目录下。


运行项目，执行如下命令：

*  npm install

*  node app.js



















