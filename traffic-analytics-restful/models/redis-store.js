var redis = require('then-redis');
var client = redis.createClient('6379', '127.0.0.1');

client.on("error", function(error) {
	console.log(error);
});

module.exports = client;