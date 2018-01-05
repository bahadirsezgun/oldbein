ptBossApp.controller('UserFindClassBookingController', function($scope,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.filterUserName="";
	$scope.filterUserSurname="";
	
	
	$scope.searchUser = function(){
		var progId=0;
		if($scope.programClass!=null){
			progId=$scope.programClass.progId;
		}
		var frmDatum = {"userName":$scope.filterUserName,
						"userSurname":$scope.filterUserSurname,
						"userType":globals.USER_TYPE_MEMBER,
						}; 
		if(progId==0){
			
			$.ajax({
				  type:'POST',
				  url: "../pt/ptusers/findByUserNameForSales/"+globals.PROGRAM_CLASS,
				  contentType: "application/json; charset=utf-8",				    
				  data: JSON.stringify(frmDatum),
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					if(res!=null){
						$scope.viewResult(res);
					}
				});
			
			
		}else if($scope.upperSaleId==0 && $scope.upperSchId==0){
	    	
	    	$.ajax({
				  type:'POST',
				  url: "../pt/ptusers/findByUserNameAndSaleProgramWithPlan/"+globals.PROGRAM_CLASS+"/"+progId+"/"+$scope.schedulePlan.schId,
				  contentType: "application/json; charset=utf-8",				    
				  data: JSON.stringify(frmDatum),
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					if(res!=null){
						$scope.viewResult(res);
					}
				});

	    	
	    }else{
			$.ajax({
				  type:'POST',
				  url: "../pt/ptusers/findByUserNameAndSaleProgram/"+globals.PROGRAM_CLASS+"/"+progId+"/"+$scope.schedulePlan.schId,
				  contentType: "application/json; charset=utf-8",				    
				  data: JSON.stringify(frmDatum),
				  dataType: 'json', 
				  cache:false
				}).done(function(res) {
					if(res!=null){
						$scope.viewResult(res);
					}
				});
	    }
	}
	
	
});