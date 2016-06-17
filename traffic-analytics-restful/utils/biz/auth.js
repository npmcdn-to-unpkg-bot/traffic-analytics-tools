var util = require('util');
var redisStore = require('../models/redis-store');
var User = require('../models/user');
var WebToken = require('../models/webtoken');

var default_cookie_expires_mins = 30 * 60 * 1000;//30mins

var getRedisExpire = function(maxActiveInterval) {
	return maxActiveInterval / 1000;
};

var refreshCookie = function(res, webtoken) {
	console.log('refresh cookie:' + webtoken.token);
	res.cookie('accessToken', webtoken.token, {
		maxAge: webtoken.maxActiveInterval,
		httpOnly: true
	});
}

exports.checkAuth = function(req, res, next) {
	//if login or register request, then next
	var requestUrl = req.url;
	console.log('Request URL:' + requestUrl);
	if (requestUrl === '/login' || requestUrl === '/user/register') {
		next();
	} else {
		//get accessToken from cookie
		var accessToken = req.cookies.accessToken;
		if (accessToken) {
			redisStore.get(accessToken).then(function(reply) {
				if (reply) {
					reply = reply.toString();
					var webtoken = JSON.parse(reply);
					var redisExpire = getRedisExpire(webtoken.maxActiveInterval);
					//redisClient.expire(accessToken, redisExpire);
					redisStore.multi();
					redisStore.set(accessToken, JSON.stringify(webtoken));
					redisStore.expire(accessToken, redisExpire);
					redisStore.exec().then(function(reply) {
						refreshCookie(res, webtoken);
						req.user = webtoken.user;
						next();
					}).catch(function(err) {
						throw err;
					})
				} else {
					throw new BizError('拒绝访问,请检查您的访问权限');
				}
			});
		} else {
			//accessToken is null or undefined
			throw new BizError('拒绝访问,请检查您的访问权限');
		}
	}
}

exports.login = function(req, res, next) {
	//1.check the account and password is not null;
	req.check('email', ValidateMsg.auth_username_password_wrong).notEmpty().isEmail();
	req.check('pwd', ValidateMsg.auth_username_password_wrong).notEmpty();
	var errors = req.validationErrors();

	if (errors) {
		throw new BizError(errors[0].msg);
	};

	//2. load the account、pwd、remeberMe params from http-request-body
	var email = req.body.email;
	var pwd = req.body.pwd;
	
	//3. find user by account, and then validate the pwd is right or not,if right write accessToken to client browser, else send alert message to client
	var promises = User.findOne({
		'email': email
	}).exec();
	promises.then(function(user) {
		if (user && user.pwd === pwd) {
			var maxActiveInterval = default_cookie_expires_mins;
			var webtoken = new WebToken();
			webtoken.user = user;
			webtoken.maxActiveInterval = maxActiveInterval;
			webtoken.createTime = new Date();

			// save user's token to redis
			var accessToken = webtoken.token;
			var redisExpire = getRedisExpire(webtoken.maxActiveInterval);
			redisStore.multi();
			redisStore.set(accessToken, JSON.stringify(webtoken));
			redisStore.expire(accessToken, redisExpire);
			redisStore.exec().then(function(reply) {
				user.password = undefined; //don't show the password to client
				refreshCookie(res, webtoken);
				user.pwd = null;
				res.send(new JsonReturn({
					user: user
				}));
			})
			.catch(function(e) {
				throw e;
			});
		} else {
			res.send(new JsonReturn(errorMsg))
		}
	}).catch(function(e) {
		throw e;
	})
}

/**
 * user logout , clear cookie of the req client
 * 
 * @author ZhangYongjia
 */
exports.logout = function(req, res, next) {
	var accessToken = req.cookies.accessToken;
	//make the maxAge to zero, thenclient will remove the cookie
	var webtoken = {
		token: accessToken,
		maxActiveInterval: 0
	};

	redisStore.del(accessToken).then(function(reply) {
		refreshCookie(res);
		res.send(new JsonReturn(null));
	});
}