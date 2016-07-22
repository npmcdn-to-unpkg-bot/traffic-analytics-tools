##traffic-analytics-tools
---------------------------------------------------------------------------------

@author : balabala.sean@gmail.com
@author : wilson.shang@gmail.com

#### traffic-analytics-console (nodejs提供的admin管理平台)

traffic-analytics-console是采用的angular.js前端单页的形式开发，nodejs提供restful service

在运行这个项目之前，你需要做如下的步骤：

(以下两步此处略过，安装请另查阅相关资料)
install nodejs
install mongodb


(此处已将node_modules、bower_components提交到git上了，有的lib不好下载，npm install和bower install可跳过)
$ cd traffic-analytics-console
$ npm install -g grunt-cli
$ npm install
$ bower install

# to build the 'dist' folder
$ grunt build:dist

$ node app.js


#### traffic-analytics-service (java负责数据处理，下载报告，清洗、串联、对接Cost/Revenue，实时出价)

* 此项目为maven项目
* 技术栈：maven + spring mvc + mongodb + redis
