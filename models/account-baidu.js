var mongodb = require('./mongodb');
var mongoose = mongodb.mongoose;
var Schema = mongoose.Schema;

/**
 * baidu的流量投放账号，用于竞价排名流量的花费，API Report Download & API Bidding
 * 
 */
var BaiduAccountSchema = new Schema({
	username:String,
	password:String,
	token:String,
	target:String,
	email:String
},{strict:true,collection:'BaiduAccount'});

var BaiduAccount = mongoose.model('BaiduAccount', BaiduAccountSchema);

module.exports = BaiduAccount;