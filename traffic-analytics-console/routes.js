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
	app.post('/auth/login', auth.login);
	app.post('/auth/signup', auth.logout);
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



	app.get('/profile', function(req, res) {
		var result = {
			sites: [
				{ id: 1, name: "Book Site", vendors: [
					{id:1, name:'Bing1'}, {id:2, name:'Google2'}, {id:3, name:'Bing3 a long name'} ]},
				{ id: 2, name: "Gift Site", vendors: [
					{id:4, name:'Bing4'}, {id:5, name:'Google5'}, {id:6, name:'Bing6 a long name'} ]},
				{ id: 3, name: "Tech Site", vendors: [
					{id:7, name:'Bing7'}, {id:8, name:'Google8'} ]},
				{ id: 4, name: "Tiki Site", vendors: [] }
			]
		};
		res.status(200).send(result);
	});

	app.get('/sites/:id/performance/:dimension', function(req, res) {
		/**
		 * url: /sites/3/performance/[keyword|adgroup|campaign|date]
		 * queries:
		 *   vendor: vendor id
		 *   startdate: 2016-05-01
		 *   enddate: 2016-05-11
		 *   order: clicks or -clicks for descent sort
		 *   page: page start from 1
		 *   limit: return list size
		 *   filter: e.g. keyword-eq-aa,clicks-gt-15
		 */
		var result;
		if (req.params.dimension == 'date') {
			result = {
				rows: [
					{ date: '2015-07-01', clicks: 123, cost: 532.332, revenue: 21.536,  cpc: 3.6383 },
					{ date: '2015-07-02', clicks: 62,  cost: 251.934, revenue: 10.921,  cpc: 1.2593 },
					{ date: '2015-07-03', clicks: 249, cost: 418.275, revenue: 52.734,  cpc: 2.9172 },
					{ date: '2015-07-04', clicks: 549, cost: 918.275, revenue: 152.734, cpc: 2.9172 },
					{ date: '2015-07-05', clicks: 549, cost: 918.275, revenue: 152.734, cpc: 2.9172 }
				]
			};
		} else {
			result = {
				rows: [
					{ keyword: 'Gift card',  clicks: 123, cost: 532.332, revenue: 21.536,  cpc: 3.6383 },
					{ keyword: 'Paper card', clicks: 62,  cost: 251.934, revenue: 10.921,  cpc: 1.2593 },
					{ keyword: 'Glass gift', clicks: 549, cost: 918.275, revenue: 152.734, cpc: 2.9172 },
					{ keyword: 'Glass gift', clicks: 549, cost: 918.275, revenue: 152.734, cpc: 2.9172 },
					{ keyword: 'Glass gift', clicks: 549, cost: 918.275, revenue: 152.734, cpc: 2.9172 }
				],
				count: 98
			};
		}
		res.status(200).send(result);
	});

	app.get('/sites/:id/bindings', function(req, res) {
		/**
		 * get all binding, including vendors and conversions bindings
		 */
		var result = {
			conversion: { accountId: 'Aaaaxxxxyyy', customerId: 'Xbcdxaxoyzy' },
			vendors: [
				{ id: 1, name: "AdWords Sam", type: "adwords", bindTime: '2016-04-22 15:23' },
				{ id: 2, name: "Bing Lye",    type: "bingads", bindTime: '2016-04-22 15:23' },
				{ id: 3, name: "Bing San",    type: "bingads", bindTime: '2016-04-22 15:23' }
			]
		};
		res.status(200).send(result);
	});

	app.get('/sites/:sid/vendors/:vid', function(req, res) {
		/**
		 * get vendor bingding detail
		 */
		var result = {
			data: {
				id: 123,
				name: 'Adwords 2',
				site: 1,
				type: 'adwords',
				username: 'abcd@example.com',
				customerid: 'the-customer-id',
				accountid: 'the-account-id',
				clientid: 'the-client-id',
				clientsecret: 'the-client-secret',
				refreshtoken: 'the-refresh-token',
				accesstoken: 'the-access-token',
				devtoken: 'the-dev-token'
			}
		};
		res.status(200).send(result);
	});

	app.post('/sites/:sid/vendors', function(req, res) {
		/**
		 * create a vendor binding
		 *
		 * result: the new created vendor
		 */
		var item = req.body;
		item.id = 333;
		res.status(200).send({ data: item });
	});

	app.post('/sites/:sid/vendors/:vid', function(req, res) {
		/**
		 * update a verdor binding
		 */
		res.status(200).send({ data: req.body });
	});

	app.get('/sites/:id/rules', function(req, res) {
		/**
		 * get rules list
		 * queries:
		 *   page: page start from 1
		 *   limit: return list size
		 */
		var result = {
			rows: [{
				id: 1, name:'bid up', vendor: 2, frequency: 'mon', action: 'bidup', bidby: '20%',
				filters: [
					{ indicator: 'clicks',  days: 3, operator: 'gt', value: 35 },
					{ indicator: 'revenue', days: 3, operator: 'lt', value: 13 }
				]
			}, {
				id: 2, name:'pause bid', vendor: 1, frequency: 'fri', action: 'pause', bidby: '',
				filters: [
					{ indicator: 'clicks',  days: 3, operator: 'gt', value: 28 },
					{ indicator: 'revenue', days: 3, operator: 'lt', value: 15 }
				]
			}, {
				id: 3, name:'bid down', vendor: 3, frequency: 'tue', action: 'biddown', bidby: '10%',
				filters: [
					{ indicator: 'cpc',  days: 7, operator: 'gt', value: 2.35 },
					{ indicator: 'revenue', days: 3, operator: 'lt', value: 10 }
				]
			}],
			count: 98
		};
		res.status(200).send(result);
	});

	app.get('/sites/:sid/rules/:rid', function(req, res) {
		/**
		 * get rule detail
		 */
		var result = {
			data: {
				id: 1, name:'bid up', vendor: 2, frequency: 'mon', action: 'bidup', bidby: '20%',
				filters: [
					{ indicator: 'clicks',  days: 3, operator: 'gt', value: 35 },
					{ indicator: 'revenue', days: 3, operator: 'lt', value: 13 }
				]
			}
		};
		res.status(200).send(result);
	});

	app.post('/sites/:sid/rules', function(req, res) {
		/**
		 * create a rule
		 */
		var item = req.body;
		item.id = 333;
		res.status(200).send({ rule: item });
	});

	app.post('/sites/:sid/rules/:rid', function(req, res) {
		/**
		 * update a rule
		 */
		res.status(200).send({ vendor: req.body });
	});

	app.delete('/sites/:sid/rules/:rid', function(req, res) {
		/**
		 * delete a rule
		 */
		res.status(200).send({ item: { id: req.params.rid } });
	});

	app.get('/sites/:sid/rules/:rid/logs', function(req, res) {
		/**
		 * get rules run logs
		 * caution: not sure which is better for logs of all rules: /sites/1/rules/logs or /sites/1/rules/0/logs
		 * queries:
		 *   vendor: vendor id
		 *   sort: clicks or -clicks for descent sort
		 *   page: page start from 1
		 *   limit: return list size
		 */
		var result = {
			rows: [
				{ ruleid: 1, date: '2016-05-20', name: 'bid up',    vendor: 2, vendorName: 'Google2', affected: 15 },
				{ ruleid: 1, date: '2016-05-21', name: 'bid up',    vendor: 2, vendorName: 'Google2', affected: 25 },
				{ ruleid: 1, date: '2016-05-22', name: 'bid up',    vendor: 2, vendorName: 'Google2', affected: 30 },
				{ ruleid: 2, date: '2016-05-22', name: 'pause bid', vendor: 1, vendorName: 'Bing1',   affected: 12 },
				{ ruleid: 2, date: '2016-05-23', name: 'pause bid', vendor: 1, vendorName: 'Bing1',   affected: 32 }
			],
			count: 98
		};
		res.status(200).send(result);
	});

	app.get('/sites/:id/rules/:rid/result', function(req, res) {
		/**
		 * get rules run logs
		 * queries:
		 *   startdate: 2016-05-01
		 *   enddate: 2016-05-11
		 *   sort: clicks or -clicks for descent sort
		 *   page: page start from 1
		 *   limit: return list size
		 */
		var result = {
			rows: [
				{ date: '2016-05-11', keyword: 'Gift card',  clicks: 123, cost: 532.332, revenue: 21.536,  cpc: 3.6383 },
				{ date: '2016-05-11', keyword: 'Paper card', clicks: 62,  cost: 251.934, revenue: 10.921,  cpc: 1.2593 },
				{ date: '2016-05-12', keyword: 'Glass gift', clicks: 549, cost: 918.275, revenue: 152.734, cpc: 2.9172 },
				{ date: '2016-05-12', keyword: 'Glass gift', clicks: 549, cost: 918.275, revenue: 152.734, cpc: 2.9172 },
				{ date: '2016-05-13', keyword: 'Glass gift', clicks: 549, cost: 918.275, revenue: 152.734, cpc: 2.9172 }
			],
			count: 98
		};
		res.status(200).send(result);
	});

	app.get('/sites', function(req, res) {
		var result = {
			rows: [
				{ id: 1, name: 'Book Site', url: 'http://www.book.com' },
				{ id: 2, name: 'Gift Site', url: 'http://www.gift.com' },
				{ id: 3, name: 'Tech Site', url: 'http://www.tech.com' },
				{ id: 4, name: 'Tiki Site', url: 'http://www.tiki.com' }
			],
			count: 4
		};
		res.status(200).send(result);
	});

	app.get('/sites/:id', function(req, res) {
		/**
		 * get site detail
		 */
		var result = {
			data: { id:1, name:'Book site' }
		};
		res.status(200).send(result);
	});

	app.post('/sites', function(req, res) {
		/**
		 * create a new site
		 */
		var item = req.body;
		item.id = 333;
		res.status(200).send({ site: item });
	});

	app.post('/sites/:id', function(req, res) {
		/**
		 * update a site
		 */
		res.status(200).send({ site: req.body });
	});

	app.delete('/sites/:id', function(req, res) {
		/**
		 * delete a site
		 */
		res.status(200).send({ item: { id: req.params.id } });
	});

	app.get('/notifications', function(req, res) {
		var result = {
			newCount: 3,
			messages: [
				// message can optionally has a `thumb` attribute indicates the thumb image of this message
				{ id:5406, title:"This is a new notification", createTime:"2016-06-24 12:09:28", isRead:0, type:0 },
				{ id:5407, title:"This is the second notification", createTime:"2016-06-23 13:23:19", isRead:1, type:0 },
				{ id:5408, title:"This is another notification", createTime:"2016-06-22 22:13:42", isRead:0, type:0 }
			]
		};
		res.status(200).send(result);
	});

	app.get('/async-check', function(req, res) {
		/**
		 * url: /async-check?type=account.bound&data=abcd
		 *
		 * form async validator
		 * queries:
		 *   type, required, the data type to check, eg:
		 *     account.bound - check if accountid is already bound when account biniding
		 *   data, required, the data value to check
		 *
		 * result:
		 *   {result: check_result}
		 */
		var type = req.query.type;
		var data = req.query.data;
		res.status(200).send({ result: 1 });
	});
	

};