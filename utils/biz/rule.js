var schedule = require('node-schedule');
var Rule = require('../models/rule');

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
function checkRule(req, isUpdateRequest) {
	if (isUpdateRequest) {
		req.check('id', '优化规则的ID不可以为空').notEmpty();
	};
	req.check('name', '优化规则名称长度为4~32位长度(必填)').notEmpty().isEmail();
	req.check('websiteId', '用户名称长度为2~4位长度(必填)').notEmpty().len(2, 4);
	req.check('vender', '公司名称长度为4~32位长度(必填)').notEmpty().len(4, 32);
	req.check('account', '性别不可以为空(必填)').notEmpty();
	req.check('historyDays', '密码长度为8~16位长度(必填)').notEmpty().len(8, 16);
	req.check('conditions', '密码长度为8~16位长度(必填)').notEmpty().len(8, 16);
	req.check('operation', '密码长度为8~16位长度(必填)').notEmpty().len(8, 16);
	return check(req);
}


function check(req){
	var checkErrors = req.validationErrors();
	if (checkErrors) {
		throw new BizError(checkErrors[0].msg);
	};
}



exports.findRuleListByWebsiteId = function(req, res) {
	req.check('websiteId','站点ID不可以为空').notEmpty();
	check(req);
	var websiteId = req.websiteId;	
	var $query = {
		websiteId:websiteId
	}
	
	var promises = Rule.find($query).exec();
	promises.then(function(rules){
		res.send(new JsonReturn(rules));
	}).catch(function(err){
		throw err;
	});
};

exports.findRuleById = function(req, res) {
	req.check('id','优化规则的ID不可以为空').notEmpty();
	check(req);
	var ruleId = req.id;
	var $query = {
		id:ruleId
	};
	var promises = Rule.findOne($query).exec();
	promises.then(function(rule){
		res.send(new JsonReturn(rule));
	}).catch(function(err){
		throw err;
	})
}

exports.addRule = function(req, res) {
	
}


exports.editRule = function(req, res) {}
exports.removeRule = function(req, res) {}