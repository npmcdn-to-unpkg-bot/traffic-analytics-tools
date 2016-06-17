var mongodb = require('./mongodb');
var mongoose = mongodb.mongoose;
var Schema = mongoose.Schema;

var UserSchema = new Schema({
	email:String,
	pwd:String,
	name:String,
	tel:Number,
	state:Number,
	type:Number
},{strict:true,collection:'User'});

var User = mongoose.model('User', UserSchema);

module.exports = User;