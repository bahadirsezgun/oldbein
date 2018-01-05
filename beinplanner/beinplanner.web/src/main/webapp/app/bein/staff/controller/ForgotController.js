ptBossApp.controller('ForgotController', function($scope,$translate,parameterService,$location,homerService,commonService) {
	
	$scope.email="";
	
	$scope.sendPassword=function(){
		
		
		if($scope.email=="" | $scope.email.match(/@/g).length==0){
			toastr.error($translate.instant("emailRequired"));
			return;
		}

		var frmDatum={'content':$translate.instant("yourPasswordContent")
					,'subject':$translate.instant("yourPasswordSubject")
					,'toPerson':$scope.email}
		
		
		$.ajax({
  		  type:'POST',
  		  url: "../pt/ptusers/forgotPassword/",
  		  contentType: "application/json; charset=utf-8",				    
  		  data:JSON.stringify(frmDatum),
  		  dataType: 'json', 
  		  cache:false
  		}).done(function(res) {
  			
  			if(res.resultStatu==1){
  				toastr.success("passwordSendToYourMail");
  			}else{
  				toastr.error($translate.instant(res.resultMessage));
  			}
  			
  			
  			
  		});
		
	}
	
	
});