module.exports = function(app){
	var auth = require('./biz/auth');
	var user = require('./biz/user');
	var website = require('./biz/website');
	var bing = require('./biz/account-bing');
	var baidu = require('./biz/account-baidu');
	var adwords = require('./biz/account-adwords');
	var ga = require('./biz/ga');

	//------------------------------------------------------------------------------------auth---[start]
	app.all('*', auth.checkAuth);
	app.post('/login', auth.login);
	app.post('/logout', auth.logout);
	//------------------------------------------------------------------------------------auth---[end]
	
	//------------------------------------------------------------------------------------user---[start]
	app.post('/user/register', user.register);
	app.get('/user/get/id', user.getUserById);
	app.get('/user/get/email', user.getUserByEmail);
	app.put('/user/edit/info', user.editUserInfo);
	app.put('/user/edit/pwd', user.editUserPwd);
	//------------------------------------------------------------------------------------user---[end]
	
	//------------------------------------------------------------------------------------website---[start]
	app.get('/website', website.findAllWebsites);
	app.get('/website/:id', website.findWebsiteById);
	app.post('/website', website.addWebsite);
	app.put('/website/:id', website.editWebsite);
	app.delete('/website/:id', website.deleteWebsite);
	//------------------------------------------------------------------------------------website---[end]
	
	//------------------------------------------------------------------------------------website-bind-google-analytics-[start]
	app.get('/website/bind/gaview/:websiteDomain', ga.getGaViewsByDomain);
	//------------------------------------------------------------------------------------website-bind-google-analytics-[end]
	
	//------------------------------------------------------------------------------------website-bind-bing-[start]
	app.get('/website/:id/bind/bing/:accountId', bing.findBingAccountById);
	app.get('/website/:id/bind/bing/', bing.findBingAccountsByWebsiteId);
	app.post('/website/:id/bind/bing', bing.bindBingAccount);
	app.put('/website/:id/bind/bing/:accountId', bing.editBingAccount);
	app.delete('/website/:id/bind/bing/:accountId', bing.deleteBingAccount);
	//------------------------------------------------------------------------------------website-bind-bing-[end]
	
	//------------------------------------------------------------------------------------website-bind-baidu-[start]
	app.get('/website/:id/bind/baidu/:accountId', baidu.findBaiduAccountById);
	app.get('/website/:id/bind/baidu/', baidu.findBaiduAccountsByWebsiteId);
	app.post('/website/:id/bind/baidu', baidu.bindBaiduAccount);
	app.put('/website/:id/bind/baidu/:accountId', baidu.editBaiduAccount);
	app.delete('/website/:id/bind/baidu/:accountId', baidu.deleteBaiduAccount);
	//------------------------------------------------------------------------------------website-bind-baidu-[end]
	
	//------------------------------------------------------------------------------------website-bind-adwords-[start]
	app.get('/website/:id/bind/adwords/:accountId', adwords.findAdwordsAccountById);
	app.get('/website/:id/bind/adwords/', adwords.findAdwordsAccountsByWebsiteId);
	app.post('/website/:id/bind/adwords', adwords.bindAdwordsAccount);
	app.put('/website/:id/bind/adwords/:accountId', adwords.editAdwordsAccount);
	app.delete('/website/:id/bind/adwords/:accountId', adwords.deleteAdwordsAccount);
	//------------------------------------------------------------------------------------website-bind-adwords-[end]
	
	
};