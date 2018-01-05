ptBossApp.factory("parameterService",function($rootScope){
	
	var parameterService={};
	
	parameterService.param1="";
	parameterService.param2="";
	parameterService.param3="";
	parameterService.param4="";
	parameterService.param5="";
	parameterService.param6="";
	
	
	
	parameterService.init=function(){
		parameterService.param1="";
		parameterService.param2="";
		parameterService.param3="";
		parameterService.param4="";
		parameterService.param5="";
		parameterService.param6="";
			
	}
	
	return parameterService;
});