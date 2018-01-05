ptBossApp.factory("calendarTimesService",function($rootScope){
	
	$rootScope.calendarTimes=new Object();
	
	$rootScope.calendarTimes.morningTimes;
	$rootScope.calendarTimes.afternoonTimes;
	$rootScope.calendarTimes.nightTimes;
	$rootScope.calendarTimes.allDayTimes;
	
	var sharedTimes=new Object();
	
	sharedTimes.getMorningTimes=function(){
		if($rootScope.calendarTimes.morningTimes==null || $rootScope.calendarTimes.morningTimes.length==0 ){
			$.ajax({
				  type:'POST',
				  url: "../pt/schedule/getMorningTimes",
				  contentType: "application/json; charset=utf-8",				    
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					$rootScope.calendarTimes.morningTimes=res;
					return $rootScope.calendarTimes.morningTimes;
				});
		}else{
			return $rootScope.calendarTimes.morningTimes;
		}
	}
	
	sharedTimes.getAfternoonTimes=function(){
		if($rootScope.calendarTimes.afternoonTimes==null || $rootScope.calendarTimes.afternoonTimes.length==0){
			$.ajax({
				  type:'POST',
				  url: "../pt/schedule/getAfternoonTimes",
				  contentType: "application/json; charset=utf-8",				    
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					$rootScope.calendarTimes.afternoonTimes=res;
					return $rootScope.calendarTimes.afternoonTimes;
				});
		}else{
			return $rootScope.calendarTimes.afternoonTimes;
		}
	}
	
	sharedTimes.getNightTimes=function(){
		if($rootScope.calendarTimes.nightTimes==null || $rootScope.calendarTimes.nightTimes.length==0){
			$.ajax({
				  type:'POST',
				  url: "../pt/schedule/getNightTimes",
				  contentType: "application/json; charset=utf-8",				    
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					$rootScope.calendarTimes.nightTimes=res;
					return $rootScope.calendarTimes.nightTimes;
				});
		}else{
			return $rootScope.calendarTimes.nightTimes;
		}
	}
	
	sharedTimes.getAllDayTimes=function(){
		if($rootScope.calendarTimes.allDayTimes==null || $rootScope.calendarTimes.allDayTimes.length==0){
			$.ajax({
				  type:'POST',
				  url: "../pt/schedule/getAllDayTimes",
				  contentType: "application/json; charset=utf-8",				    
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					$rootScope.calendarTimes.allDayTimes=res;
					return $rootScope.calendarTimes.allDayTimes;
				});
		}else{
			return $rootScope.calendarTimes.allDayTimes;
		}
	}
	
	
	return sharedTimes;
});