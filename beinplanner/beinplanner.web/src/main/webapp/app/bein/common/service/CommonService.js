ptBossApp.factory("commonService",function($rootScope){
	var sharedService={};
	sharedService.loaderValue="true";
	sharedService.search;
	sharedService.searchBoxPH;
	
	sharedService.modal=function(lval){
		this.loaderValue=lval;
		this.broadCastItem();
	};
	
	sharedService.broadCastItem = function(){
		$rootScope.$broadcast("handlebroadcast");
	};
	
	sharedService.searchItem = function(){
		$rootScope.$broadcast("search");
	};
	
	sharedService.searchBoxPHItem =function(){
		$rootScope.$broadcast("searchBoxPH");
	};
	
	sharedService.helpItem = function(){
		$rootScope.$broadcast("help");
	};

	sharedService.pageName;
	sharedService.pageComment;
	sharedService.normalHeaderVisible=false;
	
	sharedService.setNormalHeader = function(){
		$rootScope.$broadcast("setNormalHeader");
	};
	
	sharedService.changeLang = function(lang){
		$rootScope.lang=lang;
		$rootScope.$broadcast("changeLang");
	};
	
	return sharedService;
});