ptBossApp.controller('ScheduledPlansBookingController', function($scope,$translate,parameterService,$location,homerService,commonService,globals) {

	
	$scope.scheduledPlansForSearch;
	$scope.planDateStrIn;
	
	$scope.initSPCB=function(){
		
		var frmDatum={"startDateStr":$scope.startDate,
					  "endDateStr":$scope.endDate,
					  "staffId":$scope.staffId,
					  "studios":$scope.studios};
		
		$scope.scheduledPlansForSearch="";
		$.ajax({
			  type:'POST',
			  url: "../pt/schedule/searchPlans",
			  contentType: "application/json; charset=utf-8",				    
			  data:JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				$("#planDateStrIn").datepicker({language: $scope.ptLang,autoclose: true,format: $scope.ptDateFormat}).datepicker("setDate", new Date());
				$scope.planDateStrIn=$scope.startDate;
				
				$scope.scheduledPlansForSearch=res;
				console.log(res);
				
				$scope.$apply();
			});
		
	};
	
	
	
	
	
	$scope.backBtn=function(){
		$scope.backToMain();
	}
	
	$scope.findProperTimeIn=function(){
		var frmDatum={"startDateStr":$scope.planDateStrIn,
				  "endDateStr":$scope.planDateStrIn,
				  "staffId":$scope.staffId,
				  "studios":$scope.studios};
	
	
	$.ajax({
		  type:'POST',
		  url: "../pt/schedule/searchPlans",
		  contentType: "application/json; charset=utf-8",				    
		  data:JSON.stringify(frmDatum),
		  dataType: 'json', 
		  cache:false
		}).done(function(res) {
			$scope.scheduledPlansForSearch=res;
			$scope.$apply();
		});
	};
		
	
});