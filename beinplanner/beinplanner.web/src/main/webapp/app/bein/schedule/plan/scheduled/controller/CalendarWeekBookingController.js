ptBossApp.controller('CalendarWeekBookingController', function($scope,$translate,$window,parameterService,$location,homerService,commonService,globals) {

	$scope.scheduleTimePlan;
	$scope.selectedTimePlans;
	
	
	
	$scope.calendarDate;
	$scope.calendarDateName;
	$scope.day=0;
	$scope.scheduleTimePlansForCalendar1;
	$scope.calendarDateName1;
	$scope.calendarDate1;
	
	
	
	$scope.scheduleTimePlansForCalendar2;
	$scope.calendarDateName2;
	$scope.calendarDate2;
	
	$scope.scheduleTimePlansForCalendar3;
	$scope.calendarDateName3;
	$scope.calendarDate3;
	
	$scope.scheduleTimePlansForCalendar4;
	$scope.calendarDateName4;
	$scope.calendarDate4;
	
	$scope.showPopupDetail=false;
	
	$scope.showDetail=function(event,scheduleTimePlan){
	
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
				$scope.showPopupDetail=true;
				$scope.$apply();
				
				$(".splash").css("display",'none');
			});
		
		
		
	}
	$scope.hideDetail=function(){
		 $scope.showPopupDetail=false;
		
	}
	

	
	$scope.search;
	
	$scope.$on("search",function(){
		$scope.search=commonService.search;
	});
	
	
	$scope.initCBC=function(){
		commonService.normalHeaderVisible=false;
		commonService.setNormalHeader();
		$scope.calendarDate=$scope.startDate;
		getWeekTimePlans();
		
		
		
	}
	
	$scope.prevDate=function(){
		$scope.showPopupDetail=false;
		$scope.scheduleTimePlansForCalendar4=$scope.scheduleTimePlansForCalendar3;
		$scope.scheduleTimePlansForCalendar3=$scope.scheduleTimePlansForCalendar2;
		$scope.scheduleTimePlansForCalendar2=$scope.scheduleTimePlansForCalendar1;
		$scope.scheduleTimePlansForCalendar1="";
		
		
		$scope.calendarDateName4=$scope.calendarDateName3;
		$scope.calendarDate4=$scope.calendarDate3;
		
		$scope.calendarDateName3=$scope.calendarDateName2;
		$scope.calendarDate3=$scope.calendarDate2;
		
		$scope.calendarDateName2=$scope.calendarDateName1;
		$scope.calendarDate2=$scope.calendarDate1;
		
		
		
		getTimePlans($scope.day,1);
		$scope.day=$scope.day-1;
		
	}
	
	
	
	
	$scope.nextDate=function(){
		
		$scope.showPopupDetail=false;
		$scope.scheduleTimePlansForCalendar1=$scope.scheduleTimePlansForCalendar2;
		$scope.scheduleTimePlansForCalendar2=$scope.scheduleTimePlansForCalendar3;
		$scope.scheduleTimePlansForCalendar3=$scope.scheduleTimePlansForCalendar4;
		$scope.scheduleTimePlansForCalendar4="";
		
		
		$scope.calendarDateName1=$scope.calendarDateName2;
		$scope.calendarDate1=$scope.calendarDate2;
		
		$scope.calendarDateName2=$scope.calendarDateName3;
		$scope.calendarDate2=$scope.calendarDate3;
		
		$scope.calendarDateName3=$scope.calendarDateName4;
		$scope.calendarDate3=$scope.calendarDate4;
		getTimePlans($scope.day+4,4);
		$scope.day=$scope.day+1;
	}
	
	function getWeekTimePlans(){
		getTimePlans($scope.day,1);
		getTimePlans($scope.day+1,2);
		getTimePlans($scope.day+2,3);
		getTimePlans($scope.day+3,4);
		
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
	
	$scope.deleteTimePlan=function(scheduleTimePlan){
		
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
		
	$scope.showSearch=function(){
		$scope.backToMain();
	}
	
	function getTimePlans(day,i){
		var frmDatum={"calendarDate":$scope.calendarDate
			 	  ,"day":day
			 	 ,'staffId':$scope.staffId};

		$scope.scheduledPlansForSearch="";
		$.ajax({
			  type:'POST',
			  url: "../pt/schedule/searchTimePlans",
			  contentType: "application/json; charset=utf-8",				    
			  data:JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				
				if(i==1){
				  $scope.scheduleTimePlansForCalendar1=res.scheduleTimePlans;
				  $scope.calendarDateName1=res.calendarDateName;
				  $scope.calendarDate1=res.calendarDate;
				}else if(i==2){
					  $scope.scheduleTimePlansForCalendar2=res.scheduleTimePlans;
					  $scope.calendarDateName2=res.calendarDateName;
					  $scope.calendarDate2=res.calendarDate;
				}else if(i==3){
					  $scope.scheduleTimePlansForCalendar3=res.scheduleTimePlans;
					  $scope.calendarDateName3=res.calendarDateName;
					  $scope.calendarDate3=res.calendarDate;
				}else if(i==4){
					  $scope.scheduleTimePlansForCalendar4=res.scheduleTimePlans;
					  $scope.calendarDateName4=res.calendarDateName;
					  $scope.calendarDate4=res.calendarDate;
				}	
				
				$scope.$apply();
			});
	}
	
	
	
	
});