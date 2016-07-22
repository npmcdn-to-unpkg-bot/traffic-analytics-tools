var uuid = require('node-uuid');

/**
 * 生成WebToken对象，用于返回Client,每次请求在http header中带过来token进行访问权限的验证,redis存储的key为WebToken.token
 * @param {Object} maxActiveInterval 有效时长
 * @param {Object} user	用户
 */
function WebToken(maxActiveInterval, user){
	this.token = createJWT(user.email);//create jwt token for restful access
	this.user = user;//token-user one-to-one
	this.maxActiveInterval = maxActiveInterval;//expires time
	this.createTime = null;
	this.lastAccessTime = null;
	this.lastAccessUrl = null;
}


//todo temp
function createJWT(email) {
	var payload = '';
	// payload format:
	// {"uid":"1234567890","name":"John Doe"}
	return 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIn0.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ';
}



module.exports = WebToken;