ptBossApp.controller('CalendarBookingController', function($scope,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.calendarDate;
	$scope.calendarDateName;
	$scope.day=0;
	$scope.scheduleTimePlansForCalendar;
	$scope.week=false;
	$scope.weekCalendar="";
	
	$scope.showDailyPopupDetail=false;
	
	$scope.search;
	
	$scope.$on("search",function(){
		$scope.search=commonService.search;
	});
	
	
	$scope.initCBC=function(){
		commonService.normalHeaderVisible=false;
		commonService.setNormalHeader();
		$scope.calendarDate=$scope.startDate;
		getTimePlans();
	}
	
	$scope.prevDate=function(){
		$scope.showDailyPopupDetail=false;
		$scope.day=$scope.day-1;
		getTimePlans();
	}
	
	$scope.nextDate=function(){
		$scope.showDailyPopupDetail=false;
		$scope.day=$scope.day+1;
		getTimePlans();
	}
	
	$scope.showWeek=function(){
		$scope.showDailyPopupDetail=false;
		$scope.week=true;
		$scope.weekCalendar="./schedule/plan/scheduled/calendar_week.html";
	}
	
	$scope.showDay=function(){
		$scope.showDailyPopupDetail=false;
		$scope.week=false;
		$scope.weekCalendar="";
	}
	
	$scope.editTimePlan=function(scheduleTimePlan){
		parameterService.param1="OLD";
		parameterService.param2=scheduleTimePlan.schId;
		parameterService.param3=scheduleTimePlan.schtId;
		var type=scheduleTimePlan.progType;
		
		if(type==globals.PROGRAM_CLASS){
			$(location).attr("href","#/schedule/plan/class/result");
		}else{
			$(location).attr("href","#/schedule/plan/personal/result");
		}
		
		
	}
	
	$scope.showSearch=function(){
		$scope.backToMain();
	}
	
	
	function getTimePlans(){
		var frmDatum={"calendarDate":$scope.calendarDate
			 	  	 ,"day":$scope.day
			 	  	,'staffId':$scope.staffId
			 	  };

		$scope.scheduledPlansForSearch="";
		$.ajax({
			  type:'POST',
			  url: "../pt/schedule/searchTimePlans",
			  contentType: "application/json; charset=utf-8",				    
			  data:JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				$scope.calendarDate=res.calendarDate;
				$scope.calendarDateName=res.calendarDateName;
				$scope.scheduleTimePlansForCalendar=res.scheduleTimePlans;
				console.log(res);
				$scope.$apply();
			});
	}
	
	
	
	
	$scope.showDailyDetail=function(event,scheduleTimePlan){
		
		$scope.scheduleTimePlan=scheduleTimePlan;
	
		
		$(".splash").css("display",'');
		$.ajax({
			  type:'POST',
			  url: "../pt/schedule/getSchedule/"+scheduleTimePlan.progType+"/"+scheduleTimePlan.schId,
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				$scope.schedulePlan=res;
				$scope.selectedTimePlans=res.scheduleTimePlans;
				$scope.showDailyPopupDetail=true;
				$scope.$apply();
				$(".splash").css("display",'none');
			});
		
		
		
	}
	$scope.hideDailyDetail=function(){
		 $scope.showDailyPopupDetail=false;
		
	}
	
	$scope.editTimePlanDaily=function(scheduleTimePlan){
		parameterService.param1="OLD";
		parameterService.param2=scheduleTimePlan.schId;
		parameterService.param3=scheduleTimePlan.schtId;
		
		var type=scheduleTimePlan.progType;
		if(type==globals.PROGRAM_CLASS){
			$(location).attr("href","#/schedule/plan/class/result");
		}else{
			$(location).attr("href","#/schedule/plan/personal/result");
		}
		
		
	}
	
	$scope.deleteTimePlanDaily=function(scheduleTimePlan){
		
		swal({
            title: $translate.instant("deleteTimePlan"),
            text: $translate.instant("deleteTimePlan"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yesContinue"),
            cancelButtonText: $translate.instant("no"),
            closeOnConfirm: true,
            closeOnCancel: true },
        function (isConfirm) {
            if (isConfirm) {
            	
      		$scope.scheduledPlansForSearch="";
      		$.ajax({
      			  type:'POST',
      			  url: "../pt/schedule/deleteTimePlan/"+scheduleTimePlan.progType,
      			  contentType: "application/json; charset=utf-8",				    
      			  data:JSON.stringify(scheduleTimePlan),
      			  dataType: 'json', 
      			  cache:false
      			}).done(function(res) {
      				if(res.resultStatu==1){
      					toastr.success($translate.instant("success"));
      				}else{
      					toastr.success($translate.instant(res.resultMessage));
      				}
      			});
			}
        });
		
		
		
	}
	
	
});