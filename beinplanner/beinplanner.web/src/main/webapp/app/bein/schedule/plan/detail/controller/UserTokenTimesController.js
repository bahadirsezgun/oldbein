ptBossApp.controller('UserTokenTimesController', function($scope,$translate,$window,parameterService,$location,homerService,commonService,globals) {

	$scope.scheduleTimePlans;
	$scope.schedulePlan;

	$scope.turnToUp=function(){
		$scope.turnFromUp();
	}
	
   $scope.initTT=function(){
	
	  /*
		$.ajax({
			  type:'POST',
			  url: "../pt/schedule/findSchedulePlanBySaleId/"+$scope.saleIdforBooking+"/"+$scope.progTypeforBooking, // 1- Personal 2-Class
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				$scope.schedulePlan=res;
				$scope.selectedTimePlans=res.scheduleTimePlans;
				$scope.$apply();
				
			});
			*/
	   refreshPage();
	}
   
   
   function refreshPage(){

		$.ajax({
			  type:'POST',
			  url: "../pt/schedule/findSchedulePlanBySaleId/"+$scope.saleIdforBooking+"/"+$scope.progTypeforBooking, // 1- Personal 2-Class
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				
				$scope.schedulePlan=res;
				$scope.scheduleTimePlans=res.scheduleTimePlans;
				$scope.$apply();
				
			});
   }
   
   $scope.editTimePlan=function(scheduleTimePlan){
		parameterService.param1="OLD";
		parameterService.param2=scheduleTimePlan.schId;
		var type=scheduleTimePlan.progType;
		
		if(type==globals.PROGRAM_CLASS){
			$(location).attr("href","#/schedule/plan/class/result");
		}else if(type==globals.PROGRAM_PERSONAL){
			$(location).attr("href","#/schedule/plan/personal/result");
		}else{
			parameterService.init();
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
     					 refreshPage();
     					toastr.success($translate.instant("success"));
     				}else{
     					toastr.success($translate.instant(res.resultMessage));
     				}
     			});
			}
       });
	}
   
});