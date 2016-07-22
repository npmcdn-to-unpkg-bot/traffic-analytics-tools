var Website = require('../models/website');

exports.addWebsite = function(req, res) {
	var website = req.body;
	website.userId = req.user._id;
	Website.create(website,function(err,website) {
		if(err){
			throw new BizError('新增站点出错');
		}else{
			res.send(new JsonReturn(null));	
		}
	});
}

exports.editWebsite = function(req, res) {
	var website = req.body;
	var $query = {
		_id : req.params.id
	};

	var $set = website;

	Website.update($query, {$set: $set}, function(err, website){
		if(err){
			throw new BizError('修改站点出错');
		}else{
			res.send(new JsonReturn(null));
		}
	});
}

exports.deleteWebsite = function(req, res) {
	websiteId = req.params.id;
	Website.remove({
		_id: websiteId
	}, function(err) {
		if (!err) {
			res.send(new JsonReturn(null));
		} else {
			throw new BizError('删除站点出错');
		}
	});
}

exports.findWebsiteById = function(req, res) {
	websiteId = req.params.id;

	Website.findOne({
		_id: websiteId
	}, function(err, website) {
		if (err) {
			throw new BizError('查询站点出错');
		} else {
			res.send(new JsonReturn(website));
		}
	});
}

exports.findAllWebsites = function(req, res) {
	var userId = req.user._id;
	Website.find({
		userId: userId
	}, function(err, websites) {
		if (err) {
			throw new BizError('查询站点出错');
		} else {
			res.send(new JsonReturn(websites));
		}
	});
}