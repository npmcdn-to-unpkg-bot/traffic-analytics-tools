var uuid = require('node-uuid');

/**
 * 生成WebToken对象，用于返回Client,每次请求在http header中带过来token进行访问权限的验证,redis存储的key为WebToken.token
 * @param {Object} maxActiveInterval 有效时长
 * @param {Object} user	用户
 */
function WebToken(maxActiveInterval, user){
	this.token = uuid.v4();//create uuid for restful interface access token
	this.user = user;//token-user one-to-one
	this.maxActiveInterval = maxActiveInterval;//expires time
	this.createTime = null;
	this.lastAccessTime = null;
	this.lastAccessUrl = null;
}

module.exports = WebToken;