var BaiduAccount = require('../models/account-baidu');

exports.bindBaiduAccount = function(req, res) {
	var baiduAccount = req.body;
	baiduAccount.websiteId = req.params.id;
	BaiduAccount.create(baiduAccount, function(err, baiduAccount){
		if(err){
			throw new BizError('绑定Baidu账号出错');
		}else{
			res.send(new JsonReturn(null));
		}
	});
}

exports.editBaiduAccount = function(req, res) {
	var baiduAccount = req.body;
	baiduAccount.websiteId = req.params.id;
	var $query = {
		_id: req.params.accountId
	};

	var $set = baiduAccount;

	BaiduAccount.update($query, {$set: $set}, function(err, baiduAccount){
		if(err){
			throw new BizError('修改已绑定baidu账号出错');
		}else{
			res.send(new JsonReturn(null));
		}
	});
}

exports.deleteBaiduAccount = function(req, res) {
	accountId = req.params.accountId;
	BaiduAccount.remove({
		_id: accountId
	}, function(err) {
		if (!err) {
			res.send(new JsonReturn(null));
		} else {
			throw new BizError('解除已绑定baidu账号出错');
		}
	});
}

exports.findBaiduAccountById = function(req, res) {
	accountId = req.params.accountId;

	BaiduAccount.findOne({
		_id: accountId
	}, function(err, baiduAccount) {
		if (err) {
			throw new BizError('查询已绑定baidu账号出错');
		} else {
			res.send(new JsonReturn(baiduAccount));
		}
	});
}

exports.findBaiduAccountsByWebsiteId = function(req, res) {
	var websiteId = req.params.id;
	BaiduAccount.find({
		websiteId: websiteId
	}, function(err, baiduAccounts) {
		if (err) {
			throw new BizError('查询已绑定baidu账号出错');
		} else {
			res.send(new JsonReturn(baiduAccounts));
		}
	});
}
