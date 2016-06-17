var mongodb = require('./mongodb');
var mongoose = mongodb.mongoose;
var Schema = mongoose.Schema;

/**
 * <pre>
 * 
 * 站点，用户使用系统的时候，需要绑定如下账号：
 * 1、GA的账号(必填)
 * 2、Baidu/Adwords/Bing的账号(可选，这些作为Cost的花费账号，调用第三方的API)
 * 
 * domain:是用户绑定GA账号时的验证
 * industry:行业，用于数据的积累
 * introduction:简介
 * </pre>
 */
var WebsiteSchema = new Schema({
	userId:String,
	domain:String,
	industry:String,
	introduction:String
},{strict:true,collection:'Website'});

var Website = mongoose.model('Website', WebsiteSchema);

module.exports = Website;