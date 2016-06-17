var mongodb = require('./mongodb');
var mongoose = mongodb.mongoose;
var Schema = mongoose.Schema;

/**
 * bing adwords的流量投放账号，用于竞价排名流量的花费，API Report Download & API Bidding
 * 
 */
var BingAccountSchema = new Schema({
	websiteId:String,
	username:String,
	password:String,
	customerId:String,
	accountId:String,
	developerToken:String,
	clientId:String,
	clientSecret:String,
	redirectUrl:String,
	refreshToken:String,
	accessToken:String
},{strict:true,collection:'BingAccount'});

var BingAccount = mongoose.model('BingAccount', BingAccountSchema);

module.exports = BingAccount;