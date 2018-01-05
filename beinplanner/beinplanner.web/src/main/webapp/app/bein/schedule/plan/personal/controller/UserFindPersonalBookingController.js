ptBossApp.controller('UserFindPersonalBookingController', function($scope,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.filterUserName="";
	$scope.filterUserSurname="";
	
	
	
	$scope.searchUser = function(){
		var progId=0;
		if($scope.programPersonal!=null){
			progId=$scope.programPersonal.progId;
		}
		
		var frmDatum = {"userName":$scope.filterUserName,
						"userSurname":$scope.filterUserSurname,
						"userType":globals.USER_TYPE_MEMBER,
						}; 
	    
		if(progId==0){
			
			$.ajax({
				  type:'POST',
				  url: "../pt/ptusers/findByUserNameForSales/"+globals.PROGRAM_PERSONAL,
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
			$scope.setPreProgramed();
	    	$.ajax({
				  type:'POST',
				  url: "../pt/ptusers/findByUserNameAndSaleProgramWithPlan/"+globals.PROGRAM_PERSONAL+"/"+progId+"/"+$scope.schedulePlan.schId,
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
	    	$scope.setPreProgramed();
			$.ajax({
				  type:'POST',
				  url: "../pt/ptusers/findByUserNameAndSaleProgram/"+globals.PROGRAM_PERSONAL+"/"+progId+"/"+$scope.schedulePlan.schId,
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