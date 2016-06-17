var express = require('express');
var favicon = require('serve-favicon');
var bodyParser = require('body-parser');
var cookieParser = require('cookie-parser');
var validator = require('express-validator');
var expressDomainMiddleware = require('express-domain-middleware'); //used to process error/exception
var log4js = require('log4js');
log4js.configure('log4js.json');
var logger = log4js.getLogger();
logger.setLevel('INFO');

require('./utils/app-error');
require('./utils/app-json-return');
require('./utils/app-message');

var app = express();

app.use(favicon('./favicon.ico'));
app.use(bodyParser.json()); //application/json
app.use(bodyParser.urlencoded({
	extended: true
})); //x-urlencoding-form-data
app.use(cookieParser());
app.use(validator());
app.use(expressDomainMiddleware);

require('./routes')(app);

app.use('/', function(req, res) {
	res.status(404).send(new JsonReturn('请求URL有误'));
});

app.use('/', function(err, req, res, next) {
	if (err) {
		logger.error(err.stack);
		var errorMsg = err instanceof BizError ? err.message : '系统错误';
		res.status(500).send(new JsonReturn(errorMsg));
	}
});

app.listen(8080, function() {
	logger.info("server has been started successfully (port:8080) ")
});