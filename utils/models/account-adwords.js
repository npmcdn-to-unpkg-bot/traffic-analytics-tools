var mongodb = require('./mongodb');
var mongoose = mongodb.mongoose;
var Schema = mongoose.Schema;

/**
 * google adwords的流量投放账号，用于竞价排名流量的花费，API Report Download & API Bidding
 * 
 */
var AdWordsAccountSchema = new Schema({
	websiteId:String,
	accountId:String,
	clientId:String,
	clientSecret:String,
	refreshToken:String,
	refreshOAuth2Token:false,
	email:String,
	password:String,
	userAgent:String,
	developerToken:String,
	skipHeader:false,
	skipSummary:false,
	reportFields:String,
	reportName:String
},{strict:true,collection:'AdWordsAccount'});

var AdWordsAccount = mongoose.model('AdWordsAccount', AdWordsAccountSchema);

module.exports = AdWordsAccount;