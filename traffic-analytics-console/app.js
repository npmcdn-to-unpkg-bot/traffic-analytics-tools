var express = require('express');
var favicon = require('serve-favicon');
var bodyParser = require('body-parser');
var cookieParser = require('cookie-parser');
var validator = require('express-validator');
var expressDomainMiddleware = require('express-domain-middleware'); //used to process error/exception

require('./utils/app-error');
require('./utils/app-json-return');
require('./utils/app-message');

var app = express();

//TODO temp icon
app.use(favicon('./favicon.ico'));

app.use(bodyParser.json()); //application/json
app.use(bodyParser.urlencoded({
	extended: true
})); //x-urlencoding-form-data
app.use(cookieParser());
app.use(validator());
app.use(expressDomainMiddleware);



var dir;
if (process.argv.length > 2 && process.argv[2] == 'dist') {
	dir = 'dist';
	app.use('/assets', express.static(__dirname + '/dist/assets'));
	app.use('/tpl', express.static(__dirname + '/dist/tpl'));
} else {
	dir = 'src';
	app.use('/js', express.static(__dirname + '/src/js'));
	app.use('/assets', express.static(__dirname + '/src/assets'));
	app.use('/tpl', express.static(__dirname + '/src/tpl'));
	app.use('/bower_components', express.static(__dirname + '/bower_components'));
}

app.get('/', function(req, res) {
	res.sendFile('index.html', { root: __dirname + '/' + dir });
});


require('./routes')(app);

app.use('/', function(req, res) {
	res.status(404).send(new JsonReturn('请求URL有误'));
});

app.use('/', function(err, req, res, next) {
	if (err) {
		console.error(err.stack);
		res.sendFile('index.html', { root: __dirname + '/' + dir });
		//var errorMsg = err instanceof BizError ? err.message : '系统错误';
		//res.status(500).send(new JsonReturn(errorMsg));
	}
});

app.listen(8080, function() {
	console.log("server has been started successfully (port:8080) ")
});