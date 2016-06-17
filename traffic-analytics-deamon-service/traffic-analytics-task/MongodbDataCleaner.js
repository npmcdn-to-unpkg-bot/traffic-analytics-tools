/**
 * 清空Mongodb的测试数据，调用此函数前请和各个开发人员协调同意后再操作
 * @author Sean
 */
var Cleaner = {
	/**
	 * 清空所有集合的数据，系统默认的集合不会被清空
	 */
	cleanAll:function(){
		var collectionNames = db.getCollectionNames();
		for(var x=0; x<collectionNames.length; x++){
		    var collectionName = collectionNames[x];
		    Cleaner.cleanOne(collectionName);
		}	
	},
	/**
	 * 清空单个集合的数据，系统默认的集合不会被清空
	 */
	cleanOne:function(collectionName){
		if(collectionName.indexOf("system") > -1){
	        print("waring : " + collectionName + " is a system default collection, could not be removed, execute continue operation");
	    	return;
		}
		var writeResult = db.getCollection(collectionName).remove();
		print("clear collection data success : " + collectionName + "("+writeResult.nRemoved+")");
	}
}

Cleaner.cleanAll();