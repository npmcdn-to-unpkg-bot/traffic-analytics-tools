var mongoose = require('mongoose');
var mongo_url = 'mongodb://localhost:27017/TrafficAnalyticsTools';
mongoose.connect(mongo_url,function(err){
	if(err){
		throw err;
	}
});

exports.mongoose = mongoose;