var KeywordPereformance = require('../../models/performance/keyword');
var PerformanceAggregation = require('./aggregation');

exports.findKeywordsReportWithPage = function(req, res) {
	var level = PerformanceAggregation.PerformanceLevel.Keyword;
	var websiteId = req.param.websiteId;
	var startDate = req.param.startDate;
	var endDate = req.param.endDate;
	
	websiteId = '5703aa6b2a54b7cbbfdedae9';
	startDate = '20160415';
	endDate = '20160420';

	var project = {
		$project: PerformanceAggregation.getProject(PerformanceAggregation.PerformanceLevel.Keyword)
	};
	var match = {
		$match: PerformanceAggregation.getMatch(websiteId, startDate, endDate)
	};
	var group = {
		$group: PerformanceAggregation.getGroup(PerformanceAggregation.PerformanceLevel.Keyword)
	};
	var totalCount = {
		$group: PerformanceAggregation.getTotalCount()
	};
	
	//var pipeline = [project, match, group, totalCount];
	var pipeline = [project, match, group];

	KeywordPereformance.aggregate(pipeline).exec(function(err, docs) {
		if (err) {
			throw err;
		}
		res.send(new JsonReturn(docs));
	});
}
