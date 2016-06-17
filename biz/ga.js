var googleapis = require('googleapis');
var JWT = googleapis.auth.JWT;
var analytics = googleapis.analytics('v3');

var SERVICE_ACCOUNT_EMAIL = 'mygadumper@my-ga-api.iam.gserviceaccount.com';
var SERVICE_ACCOUNT_KEY_FILE = __dirname + '/GaApiKey.p12';


var authClient = new JWT(
    SERVICE_ACCOUNT_EMAIL,
    SERVICE_ACCOUNT_KEY_FILE,
    null,
    ['https://www.googleapis.com/auth/analytics.readonly']
);


exports.getGaViewsByDomain = function(req, res){
	var websiteDomain = 'http://' + req.params.websiteDomain;
	analytics.management.accounts.list({
		auth:authClient
	}, function(err, accountsResult){
		if(err){
			throw new BizError('access google analytices api(get accounts) error.');
		}else{
			var accounts = accountsResult.items;
			for(var x = 0;x < accounts.length; x++){
				analytics.management.webproperties.list({
					auth:authClient,
					accountId:accounts[x].id
				},function(err, webPropertiesResult){
					if(err){
						throw new BizError('access google analytices api(get properties) error.');
					}else{
						var webproperties = webPropertiesResult.items;
						for(var y=0;y<webproperties.length;y++){
							if(websiteDomain !== webproperties[y].websiteUrl){
								continue;
							}
							
							analytics.management.profiles.list({
								auth:authClient,
								accountId:webproperties[y].accountId,
								webPropertyId:webproperties[y].id
							},function(err, profilesResult){
								if(err){
									throw new BizError('access google analytices api(get profiles) error.');
								}else{
									if(profilesResult){
										var profiles = profilesResult.items;
										res.send(new JsonReturn(profiles))	
									}else{
										res.send(new JsonReturn('没有找到匹配的GoogleAnalytics数据'));
									}
								}
							});
						}
					}
				});
			}
		}
	});
}