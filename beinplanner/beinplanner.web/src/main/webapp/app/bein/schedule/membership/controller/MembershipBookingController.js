ptBossApp.controller('MembershipBookingController', function($scope,$translate,parameterService,$location,homerService,commonService,globals) {

	
	$scope.scheduleFactory;
	$scope.scheduleTimePlans;
	$scope.freeze=false;
	
	$scope.freezeStartDate;
	$scope.freezeComment;
	
	$scope.progDuration=$scope.packetSale.progDuration;
	$scope.progDurationType=$scope.packetSale.progDurationType;
	$scope.progDurationTypeStr;
	
	$scope.initMembershipBooking=function(){
		findSmpPlaning();
		$.fn.editable.defaults.mode = 'inline';
		
		if($scope.progDurationType==1){
			$scope.progDurationTypeStr=$translate.instant('daily');
		}else if($scope.progDurationType==2){
			$scope.progDurationTypeStr=$translate.instant('weekly');
		}else if($scope.progDurationType==3){
			$scope.progDurationTypeStr=$translate.instant('monthly');
		}
		
	}
	
	
	$scope.openFreezeModal=function(){
		$scope.freeze=true;
	}
	
	$scope.saveFreeze=function(){
		
		var frmDatum={'smpStartDateStr':$scope.freezeStartDate
				,'smpId':$scope.smpId
				,'smpComment':$scope.freezeComment
				,'type':'smp'};
		
		
		$.ajax({
			  type:'POST',
			  url: "../pt/scheduleMembership/freezeSchedule",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res.resultStatu==1){
					toastr.success($translate.instant("success"));
					findSmpPlaning();
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
		   });
	}
	
	$scope.unFreeze=function(smtpId){
		
		
		$.ajax({
			  type:'POST',
			  url: "../pt/scheduleMembership/unFreezeSchedule/"+smtpId+"/"+$scope.smpId,
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res.resultStatu==1){
					toastr.success($translate.instant(res.resultMessage));
					findSmpPlaning();
				}else{
					toastr.fail($translate.instant(res.resultMessage));
				}
		   });
	}
	
	function findSmpPlaning(){
		$.ajax({
			  type:'POST',
			  url: "../pt/schedule/findScheduleFactoryPlanById/"+$scope.smpId+"/"+globals.SCHEDULE_TYPE_MEMBERSHIP,
			  contentType: "application/json; charset=utf-8",				    
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res!=null){
					$scope.scheduleFactory=res;
					$scope.scheduleTimePlans=res.scheduleMembershipTimePlans;
					$scope.$apply();
					$('#datepicker').datepicker({language: $scope.ptLang,todayHighlight:true});
					$('#freezeStartDate').datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat}).datepicker("setDate", new Date());
					
				}
			});
	}
	
});