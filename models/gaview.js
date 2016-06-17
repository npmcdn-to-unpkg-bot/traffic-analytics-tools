var mongodb = require('./mongodb');
var mongoose = mongodb.mongoose;
var Schema = mongoose.Schema;

/**
 * <pre>
 * 
 * GaView(Ga Goal的转化数据)，Google Analytics的结构如下：
 * - Account
 * -- Property
 * --- Profile
 * ---- GaView
 * 一个站点绑定了GA之后，轮询这个树状结构，得到它所有的GaView
 * 
 * </pre>
 */
var GaViewSchema = new Schema({
	accountId: String,
	propertyId: String,
	profileId: String,
	name: String
}, {
	strict: true,
	collection: 'GaView'
});


var GaView = mongoose.model('GaView', GaViewSchema);

module.exports = GaView;