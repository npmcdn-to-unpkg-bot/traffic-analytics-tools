function BizError(message){
	this.name = 'BizError';
	this.message = (message || '');
}

BizError.prototype = new Error();

global.BizError = BizError;