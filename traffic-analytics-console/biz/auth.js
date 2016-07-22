var jwt = require('jwt-simple');
var jwtSecretToken = 'xxx';
var util = require('util');
var User = require('../models/user');



var getJwtToken = function(req){
	try{
		var jwtToken = req.header('Authorization').split(' ')[1];
		return jwtToken;
	}catch(e){
		return null;
	}
}

var parseJwtToken = function(jwtToken){
	return jwt.decode(jwtToken, jwtSecretToken);
}


exports.checkAuth = function(req, res, next) {;
	var requestUrl = req.url;
	console.log('Request URL:' + requestUrl);
	//todo requestUrl === '/user/register'
	if (requestUrl === '/auth/login') {
		return next();
	} else {
		//get auth token from header
		var jwtToken = getJwtToken(req);
		if(jwtToken){
			var payload = parseJwtToken(jwtToken);
			var user = payload.user;
			console.log(user);
			req.user = user;
			if(user){
				next();
			}else{
				res.status(401).send({message:'No permission operation'});
				return;
			}
		}else{
			res.status(401).send({message:'No permission operation'});
			return;
		}
	}
}


exports.login = function(req, res) {
	//1.check the account and password is not null;
	//req.check('email', '用户名或者密码错误').notEmpty().isEmail();
	//req.check('password', '用户名或者密码错误').notEmpty();
	//var errors = req.validationErrors();
    //
	//if (errors) {
	//	throw new BizError(errors[0].msg);
	//};


	//2. load the account、pwd、remeberMe params from http-request-body
	var email = req.body.email;
	var pwd = req.body.password;

	//3. find user by account, and then validate the pwd is right or not,if right write accessToken to client browser, else send alert message to client
	User.findOne({'email': email}, function (err, user) {
		if(err){
			throw err;
		}
		if (user && user.pwd === pwd) {
			user.pwd = null;
			var payload = {
				user:user
			};
			var token = jwt.encode(payload,jwtSecretToken);
			res.send({ token: token });
		} else {
			res.status(401).send({message: 'Invalid email and/or password!'});
		}
	});
}

/**
 * sign up
 * 
 * @author ZhangYongjia
 */
exports.logout = function(req, res) {
	res.send({ token: '' });
}