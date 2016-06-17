var mongoose = require('mongoose');
var mongo_url = 'mongodb://semtools:S1.m-tOo!s@localhost:27017/semtools';
mongoose.connect(mongo_url,function(err){
	if(err){
		throw err;
	}
});

exports.mongoose = mongoose;