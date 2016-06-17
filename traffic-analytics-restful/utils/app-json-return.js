function JsonReturn(param) {
	var default_msg = 'ok';
	var default_data = {};

	if (typeof(param) === 'string') {
		this.msg = param;
	}

	if (typeof(param) === 'object') {
		if (param instanceof Error) {
			this.msg = param.message;
		} else {
			this.msg = default_msg;
			this.data = param;
		}
	}
}


global.JsonReturn = JsonReturn;






