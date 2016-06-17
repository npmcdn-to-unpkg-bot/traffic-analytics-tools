var AdwordsAccount = require('../models/account-adwords');

exports.bindAdwordsAccount = function(req, res) {
	var adwordsAccount = req.body;
	adwordsAccount.websiteId = req.params.id;
	AdwordsAccount.create(adwordsAccount, function(err, adwordsAccount){
		if(err){
			throw new BizError('绑定adwords账号出错');
		}else{
			res.send(new JsonReturn(null));
		}
	});
}

exports.editAdwordsAccount = function(req, res) {
	var adwordsAccount = req.body;
	adwordsAccount.websiteId = req.params.id;
	adwordsAccount.reportFields = 'CampaignId,CampaignName,AdGroupId,AdGroupName,Id,Criteria,FinalUrls,Clicks,Cost,Impressions';
	adwordsAccount.reportFields = 'Adwords-Keyword-Report';
	adwordsAccount.userAgent = 'SEMTools';
	var $query = {
		_id: req.params.accountId
	};

	var $set = adwordsAccount;

	AdwordsAccount.update($query, {$set: $set}, function(err, adwordsAccount){
		if(err){
			throw new BizError('修改已绑定adwords账号出错');
		}else{
			res.send(new JsonReturn(null));
		}
	});
}

exports.deleteAdwordsAccount = function(req, res) {
	accountId = req.params.accountId;
	AdwordsAccount.remove({
		_id: accountId
	}, function(err) {
		if (!err) {
			res.send(new JsonReturn(null));
		} else {
			throw new BizError('解除已绑定adwords账号出错');
		}
	});
}

exports.findAdwordsAccountById = function(req, res) {
	accountId = req.params.accountId;
	AdwordsAccount.findOne({
		_id: accountId
	}, function(err, adwordsAccount) {
		if (err) {
			throw new BizError('查询已绑定adwords账号出错');
		} else {
			res.send(new JsonReturn(adwordsAccount));
		}
	});
}

exports.findAdwordsAccountsByWebsiteId = function(req, res) {
	var websiteId = req.params.id;
	AdwordsAccount.find({
		websiteId: websiteId
	}, function(err, adwordsAccounts) {
		if (err) {
			throw new BizError('查询已绑定adwords账号出错');
		} else {
			res.send(new JsonReturn(adwordsAccounts));
		}
	});
}
