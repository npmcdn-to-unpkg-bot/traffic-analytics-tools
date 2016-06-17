var BingAccount = require('../models/account-bing');

exports.bindBingAccount = function(req, res) {
	var bingAccount = req.body;
	bingAccount.websiteId = req.params.id;
	
	BingAccount.create(bingAccount, function(err, bingAccount){
		if(err){
			throw new BizError('绑定Bing账号出错');
		}else{
			res.send(new JsonReturn(null));
		}
	});
}

exports.editBingAccount = function(req, res) {
	var bingAccount = req.body;
	bingAccount.websiteId = req.params.id;
	var $query = {
		_id: req.params.accountId
	};

	var $set = bingAccount;

	BingAccount.update($query, {$set: $set}, function(err, bingAccount){
		if(err){
			throw new BizError('修改已绑定Bing账号出错');
		}else{
			res.send(new JsonReturn(null));
		}
	});
}

exports.deleteBingAccount = function(req, res) {
	accountId = req.params.accountId;
	BingAccount.remove({
		_id: accountId
	}, function(err) {
		if (!err) {
			res.send(new JsonReturn(null));
		} else {
			throw new BizError('解除已绑定Bing账号出错');
		}
	});
}

exports.findBingAccountById = function(req, res) {
	accountId = req.params.accountId;

	BingAccount.findOne({
		_id: accountId
	}, function(err, bingAccount) {
		if (err) {
			throw new BizError('查询已绑定Bing账号出错');
		} else {
			res.send(new JsonReturn(bingAccount));
		}
	});
}

exports.findBingAccountsByWebsiteId = function(req, res) {
	var websiteId = req.params.id;
	BingAccount.find({
		websiteId: websiteId
	}, function(err, bingAccounts) {
		if (err) {
			throw new BizError('查询已绑定Bing账号出错');
		} else {
			res.send(new JsonReturn(bingAccounts));
		}
	});
}
