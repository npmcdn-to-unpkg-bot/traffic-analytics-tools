var User = require('../models/user');

/**
 * 
 * @param {Object} req
 * 						the request object
 * @param {Boolean} isUpdateRequest 
 * 						if true, check the user.id is not-null, else ignore
 * @param {Boolean} isEditUserInfo
 * 						if true, check email/username/company/gender, else ignore
 * @param {Boolean} isEditUserPwd
 * 						if true, check pwd/comfirmPwd, else ignore
 */
function validateUser(req, isUpdateRequest, isEditUserInfo, isEditUserPwd) {
	if (isUpdateRequest) {
		req.check('id', '用户ID不可以为空').notEmpty();
	};
	if (isEditUserInfo) {
		req.check('account', '邮箱格式不对').notEmpty().isEmail();
		req.check('username', '用户名称长度为2~4位长度(必填)').notEmpty().len(2, 4);
		req.check('company', '公司名称长度为4~32位长度(必填)').notEmpty().len(4, 32);
		req.check('gender', '性别不可以为空(必填)').notEmpty();
	};
	if (isEditUserPwd) {
		req.check('password', '密码长度为8~16位长度(必填)').notEmpty().len(8, 16);
		req.check('confirmPassword', '密码长度为8~16位长度(必填)').notEmpty().len(8, 16);
		if (req.body.password !== req.body.confirmPassword) {
			throw new BizError('两次输入的密码不一致');
		}
	};
	return check(req);
}

function check(req){
	var checkErrors = req.validationErrors();
	if (checkErrors) {
		throw new BizError(checkErrors[0].msg);
	};
	var user = req.body;
	user.confirmPwd = undefined;
	return user;
}


//add user
exports.register = function(req, res) {
	var user = validateUser(req, false, true, true);
	var promises = User.create(user);
	promises.then(function(user) {
		res.send(new JsonReturn(null));
	}).catch(function(e) {
		throw e;
	});
}


exports.getUserById = function(req, res) {
	req.check('id', '用户ID不可以为空').notEmpty();
	check(req);
	var id = req.query.id;
	console.log(id);
	var promises = User.findOne({'_id': id}).exec();
	promises.then(function(user){
		if(user){
			user.password = undefined;
		}
		res.send(new JsonReturn({user:user}));	
	}).catch(function(e) {
		throw e;
	});
}

exports.getUserByEmail = function(req, res){
	req.check('email', '邮箱格式不对').notEmpty().isEmail();
	check(req);	
	var account = req.query.account;
	var promises = User.findOne({'account': account}).exec();
	promises.then(function(user){
		if(user){
			user.password = undefined;
		}
		res.send(new JsonReturn(user));
	}).catch(function(e) {
		throw e;
	});
}

//edit user info
exports.editUserInfo = function(req, res) {
	var user = validateUser(req, true, true, false);
	var $query = {
		_id: user.id
	};
	var $set = {
		account: user.account,
		username: user.username,
		company: user.company,
		gender: user.gender
	};
	var promises = User.update($query, {$set : $set});
	promises.then(function(user) {
		res.send(new JsonReturn(null));
	}).catch(function(e) {
		throw e;
	});
};

//edit user password
exports.editUserPwd = function(req, res) {
	var user = validateUser(req, true, false, true);
	var $query = {
		_id: user.id
	};
	var $set = {
		password: user.password,
	};
	var promises = User.update($query, {$set : $set});
	promises.then(function(user) {
		res.send(new JsonReturn(null));
	}).catch(function(e) {
		throw e;
	});
};