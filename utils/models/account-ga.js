var mongodb = require('./mongodb');
var mongoose = mongodb.mongoose;
var Schema = mongoose.Schema;

/**
 * Google Analytics流量转换数据的账号，API Conversion-Report Download
 * 
 */
var GaAccountSchema = new Schema({
	websiteId:String,
	accountId:String,
	propertyId:String,
	profileId:String,
	name:String
},{strict:true,collection:'GaAccount'});

var GaAccount = mongoose.model('GaAccount', GaAccountSchema);

module.exports = GaAccount;