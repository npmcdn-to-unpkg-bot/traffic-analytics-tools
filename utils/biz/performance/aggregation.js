exports.PerformanceLevel = {
	Daily: 0,
	Campaign: 1,
	AdGroup: 2,
	Keyword: 3
}

exports.getProject = function(level) {
	var project = {
		websiteId: 1,
		date: 1,
		accountId: 1,
		cost: 1,
		clicks: 1,
		impressions: 1,
		conversions: 1
	};
	switch (level) {
		case PerformanceLevel.Daily:
			break;
		case PerformanceLevel.Campaign:
			project.campaignId = 1;
			project.campaign = 1;
			break;
		case PerformanceLevel.AdGroup:
			project.campaignId = 1;
			project.campaign = 1;
			project.adgroupId = 1;
			project.adgroup = 1;
		case PerformanceLevel.Keyword:
			project.campaignId = 1;
			project.campaign = 1;
			project.adgroupId = 1;
			project.adgroup = 1;
			project.keywordId = 1;
			project.keyword = 1;
			project.source = 1;
			project.destinationUrl = 1;
		default:
			break;
	}
	return project;
}

exports.getMatch = function(websiteId, startDate, endDate) {
	var match = {
		'websiteId': websiteId,
		'date': {
			'$gte': startDate,
			'$lte': endDate
		}
	}
	return match;
}

exports.getGroup = function(level) {
	var group = {
		_id: {
			websiteId: '$websiteId',
			accountId: '$accountId'
		},
		cost: {
			'$sum': '$cost'
		},
		clicks: {
			'$sum': '$clicks'
		},
		impressions: {
			'$sum': '$impressions'
		},
		conversions: {
			'$sum': '$conversions'
		}
	};
	switch (level) {
		case PerformanceLevel.Daily:
			break;
		case PerformanceLevel.Campaign:
			group._id.campaignId = '$campaignId';
			group._id.campaign = '$campaign';
			break;
		case PerformanceLevel.AdGroup:
			group._id.campaignId = '$campaignId';
			group._id.campaign = '$campaign';
			group._id.adgroupId = '$adgroupId';
			group._id.adgroup = '$adgroup';
		case PerformanceLevel.Keyword:
			group._id.campaignId = '$campaignId';
			group._id.campaign = '$campaign';
			group._id.adgroupId = '$adgroupId';
			group._id.adgroup = '$adgroup';
			group._id.keywordId = '$keywordId';
			group._id.keyword = '$keyword';
			group._id.source = '$source';
			group._id.destinationUrl = '$destinationUrl';
		default:
			break;
	}
	return group;
};

exports.getHaving = function(mongoQueries){
	
}

exports.getTotalCount = function() {
	var totalCount = {
		'_id': null,
		'totalCount': {
			'$sum': 1
		}
	};
	return totalCount;
}