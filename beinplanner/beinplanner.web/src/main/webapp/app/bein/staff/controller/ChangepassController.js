ptBossApp.controller('ChangepassController', function($scope,$translate,parameterService,$location,homerService,commonService) {

	
	$scope.oldPassword;
	$scope.passwordControl;
	$scope.password;
	$scope.userEmail;
	
	$scope.init=function(){
		
	}
	
	$scope.changePassword=function(){
		
      if($scope.password!=$scope.passwordControl)	{
    	  toastr.error($translate.instant("passwordDifferent"));
    	  return;
      }else if($scope.password==""){
    	  toastr.error($translate.instant("passwordMustNotBeEmpty"));
    	  return;
      }	
		
		
		var frmDatum={'oldPassword':$scope.oldPassword
				,'password':$scope.password
				,'userEmail':$scope.userEmail}
	
	
	$.ajax({
		  type:'POST',
		  url: "../pt/ptusers/changePassword/",
		  contentType: "application/json; charset=utf-8",				    
		  data:JSON.stringify(frmDatum),
		  dataType: 'json', 
		  cache:false
		}).done(function(res) {
			if(res.resultStatu==1){
				toastr.success($translate.instant(res.resultMessage));
			}else{
				 toastr.error($translate.instant(res.resultMessage));  
			  }
			
		});
		
	}
	
	
	
});